package com.kaipin.search.core.index;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.kaipin.search.core.lucene.PublicSimilarity;

/**
 * 索引库管理
 *
 * 
 */
public class IndexHolder {

    private final static Log log = LogFactory.getLog(IndexHolder.class);
    private final static IKAnalyzer analyzer = new IKAnalyzer();
    private final static int MAX_COUNT = 1000;
    private String indexPath;
    
    private   PublicSimilarity   similarity;

    /**
     * 构造索引库管理实例
     *
     * @param idx_path
     * @return
     * @throws IOException
     */
    public static IndexHolder init(String idx_path) throws IOException {
        IndexHolder holder = new IndexHolder();
        idx_path = FilenameUtils.normalize(idx_path);
        File file = new File(idx_path);
        if (!file.exists() || !file.isDirectory()) {
           // throw new FileNotFoundException(idx_path);
            file.mkdir();
            
        }
        if (!idx_path.endsWith(File.separator)) {
            idx_path += File.separator;
        }
        holder.indexPath = idx_path;
        holder.similarity = new PublicSimilarity();  
        return holder;
    }


    /**
     * 优化索引库
     *
     * @param objClass
     * @throws IOException
     */
    public void optimize(String path) throws IOException {
        FSDirectory dir = FSDirectory.open(new File(indexPath + path));
        dir.setReadChunkSize(104857600);//100兆
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, config);
        try {
            writer.forceMerge(1);
            writer.commit();
        } finally {
            close(writer);
            close(dir);
        }
    }

    /**
     * 多库搜索
     *
     * @param objClasses
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    public List<Searchable> find(List< String> paths, Query query, Filter filter, Sort sort, int page, int count) throws IOException {
        List<IndexReader> readers = new ArrayList<IndexReader>();
        List<FSDirectory> dirs = new ArrayList<FSDirectory>();
        try {
            for (String objClass : paths) {
                FSDirectory dir = FSDirectory.open(new File(objClass  ));
                log.debug("open dir:" + dir.toString());
                dir.setReadChunkSize(104857600);//100兆
                dirs.add(dir);
                readers.add(DirectoryReader.open(dir));
            }
            IndexSearcher searcher = new IndexSearcher(new MultiReader(readers.toArray(new IndexReader[readers.size()]), true));
            return find(searcher, query, filter, sort, page, count);
        } finally {
            for (IndexReader reader : readers) {
                close(reader);
            }
            for (FSDirectory dir : dirs) {
                close(dir);
            }

        }
    }

    /**
     * 单库搜索
     *
     * @param objClass
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    public List<? extends Object> find( String path,Query query, Filter filter, Sort sort, int page, int count) throws IOException {
        FSDirectory dir = FSDirectory.open(new File(indexPath+path ));
        dir.setReadChunkSize(104857600);//100兆
        IndexReader reader = DirectoryReader.open(dir);

        try {
            IndexSearcher searcher = new IndexSearcher(reader);
   
            
            List<Searchable> results = find(searcher, query, filter, sort, page, count);
            List<Object> ids = new ArrayList<Object>();
            for (Searchable obj : results) {
            	
                if (obj != null)
                    ids.add(obj.id());
            }
            return ids;
        } finally {
            close(reader);
            close(dir);
        }
    }

    /**
     * 多库搜索
     *
     * @param objClasses
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    public int count(List< String> paths, Query query, Filter filter) throws IOException {
        List<IndexReader> readers = new ArrayList<IndexReader>();
        List<FSDirectory> dirs = new ArrayList<FSDirectory>();
        try {
        	
        	
        	
        	  for (String objClass : paths) {
                  FSDirectory dir = FSDirectory.open(new File(objClass  ));
                  log.debug("open dir:" + dir.toString());
                  dir.setReadChunkSize(104857600);//100兆
                  dirs.add(dir);
                  readers.add(DirectoryReader.open(dir));
              }
        	

            IndexSearcher searcher = new IndexSearcher(new MultiReader(readers.toArray(new IndexReader[readers.size()]), true));
            return count(searcher, query, filter);
        } finally {
            for (IndexReader reader : readers) {
                close(reader);
            }
            for (FSDirectory dir : dirs) {
                close(dir);
            }

        }
    }

    /**
     * 搜索
     *
     * @param objClass
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    public int count(String path, Query query, Filter filter) throws IOException {
        FSDirectory dir = FSDirectory.open(new File(indexPath +path ));
        dir.setReadChunkSize(104857600);//100兆
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
       try {
    	   return count(searcher, query, filter);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally {
            close(reader);
            close(dir);
        }
    }

    /**
     * 搜索
     *
     * @param searcher
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    private List<Searchable> find(IndexSearcher searcher, Query query, Filter filter, Sort sort, int page, int count) throws IOException {
        try {
            searcher.setSimilarity(similarity);
   
            TopDocs hits = null;
//            if (filter != null && sort != null) {
//                hits = searcher.search(query, filter, MAX_COUNT, sort);
//            } else if (filter != null) {
//                hits = searcher.search(query, filter, MAX_COUNT);
//            } else if (sort != null) {
//                hits = searcher.search(query, MAX_COUNT, sort);
//            } else {
//                hits = searcher.search(query, MAX_COUNT);
//            }
            
            
            
            TopFieldCollector collector ;
            
            
            if (filter != null && sort != null) {
              //  hits = searcher.search(query, filter, page  * count, sort);
                
                collector    = TopFieldCollector.create(sort ,  page  * count  ,  false , true ,  false ,  false);  
                 searcher.search(query, filter,collector);
                 hits=collector.topDocs();
                 
                 
            } else if (filter != null) {
             //   hits = searcher.search(query, filter, page  * count);
                
                collector    = TopFieldCollector.create(null ,  page  * count  ,  false , true ,  false ,  false);  
                searcher.search(query, filter,collector);
                hits=collector.topDocs();
            } else if (sort != null) {
              //  hits = searcher.search(query, page  * count, sort);
                
                collector    = TopFieldCollector.create(sort ,  page  * count  ,  false , true ,  false ,  false);  
                searcher.search(query,  collector);
                hits=collector.topDocs();
                
            } else {
              //  hits = searcher.search(query, page  * count);
                
                collector    = TopFieldCollector.create(null ,  page  * count  ,  false , true ,  false ,  false);  
                searcher.search(query,  collector);
                
            }
            
            
            if (hits == null) {
                return null;
            }
            
            
            
            List<Searchable> results = new ArrayList<Searchable>();
            int nBegin = (page - 1) * count;
            
            int nEnd = Math.min(nBegin + count, hits.scoreDocs.length);
            for (int i = nBegin; i < nEnd; i++) {
                ScoreDoc s_doc = (ScoreDoc) hits.scoreDocs[i];
                Document doc = searcher.doc(s_doc.doc);
                Searchable obj = SearchHelper.doc2obj(doc);
    
                if (obj != null && !results.contains(obj)) {
                    results.add(obj);
                }
            }
            return results;

        } catch (IOException e) {
            log.error("Unabled to find via query: " + query, e);
        }
        return null;
    }

    /**
     * 根据查询条件统计搜索结果数
     *
     * @param searcher
     * @param query
     * @param filter
     * @return
     * @throws IOException
     */
    private int count(IndexSearcher searcher, Query query, Filter filter) throws IOException {
        try {
            TotalHitCountCollector thcc = new TotalHitCountCollector();
            if (filter != null) {
                searcher.search(query, filter, thcc);
            } else {
                searcher.search(query, thcc);
            }
            
         //   return Math.min(MAX_COUNT, thcc.getTotalHits());
            return thcc.getTotalHits();
        } catch (IOException e) {
            log.error("Unabled to find via query: " + query, e);
            return -1;
        }
    }

    /**
     * 批量添加索引
     *
     * @param objs
     * @throws IOException
     */
    public int add(String path,List<? extends Searchable> objs) throws IOException {
       if (objs == null || objs.size() == 0 || objs.get(0) == null) {
            return 0;
        }
        delete(path,objs);//避免重复添加
        
        int doc_count = 0;
        
        FSDirectory dir = FSDirectory.open(new File(indexPath+path));
 
        dir.setReadChunkSize(104857600);//100兆
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, config);
        config.setSimilarity(similarity);
        try {
            for (Searchable obj : objs) {
                Document doc = SearchHelper.obj2doc(obj);
                writer.addDocument(doc);
                doc_count++;
            }
            writer.commit();
        }  finally {
            close(writer);
            close(dir);
        }
        return doc_count;
        
        
        
    }
    
    
    
    
    
    

    /**
     * 批量删除索引
     *
     * @param objs
     * @throws IOException
     */
    public int delete(String path,List<? extends Searchable> objs) throws IOException {
        if (objs == null || objs.size() == 0 || objs.get(0) == null) {
            return 0;
        }
        FSDirectory dir = FSDirectory.open(new File(indexPath + path));
        dir.setReadChunkSize(104857600);//100兆
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir, config);
        try {
            int doc_count = 0;
            for (Searchable obj : objs) {
                log.debug(String.format("will delete %s with id %s", obj.getClass().getSimpleName(), obj.id()));
                writer.deleteDocuments(new Term(SearchHelper.FN_ID, String.valueOf(obj.id())));
                doc_count++;
            }
            writer.commit();
            return doc_count;
        } finally {
            close(writer);
            close(dir);
        }
    }

    /**
     * 批量更新索引
     *
     * @param objs
     * @throws IOException
     */
    public int update(String path,List<? extends Searchable> objs) throws IOException {
        delete(path,objs);
        return        add(path,objs);
    }

    private void close(FSDirectory dir) {
        if (dir != null) {
            dir.close();
            dir = null;
        }
    }

    private void close(IndexReader reader) throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    private void close(IndexWriter writer) throws IOException {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }


}
