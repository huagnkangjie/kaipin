/**
 *
 */
package com.kaipin.search.demo;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.search.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kaipin.search.core.index.IndexHolder;
import com.kaipin.search.core.index.SearchHelper;
import com.kaipin.search.core.index.Searchable;
import com.kaipin.search.demo.domain.Person;
import com.kaipin.search.demo.domain.Post;

/**
 * 测试索引过程
 
 */
public class LuceneTester {


    Post post1 = new Post("1", "post title1", "post body1");
    Post post2 = new Post("2", "阿里云携手开源中国众包平台发布百万悬赏项目", "阿里云与开源中国达成战略合作，首期将通过开源中国众包平台发布近百万元悬赏项目");
    Post post3 = new Post("3", "SQLite 3.9.0 发布，数据库服务器", "SQLite是遵守ACID的关联式数据库管理系统，它包含在一个相对小的C库中。它是D.RichardHipp建立的公有领域项目。");

    Person person1 = new Person("1", "红薯", "深圳市");
    Person person2 = new Person("2", "马云", "杭州市");
    Person person3 = new Person("3", "蜘蛛侠", "US");
    IndexHolder holder;
    String path;

    @Before
    public void setUp() throws Exception {
        String indexFolder = LuceneTester.class.getClassLoader().getResource("./").getFile() + "index";
        path=indexFolder;
        
        FileUtils.deleteDirectory(new File(indexFolder));
        FileUtils.forceMkdir(new File(indexFolder));
        holder = IndexHolder.init(indexFolder);
    }

//    @Test
//    public void testIndex() throws Exception {
//        holder.add(path,Arrays.asList(post1, post2, post3));
//        holder.
//        
//        for (int i = 0; i < 10; i++)
//            holder.optimize(path );
//
//        
//        Query query = SearchHelper.makeQuery("title", "数据库", 10.f);
//        List<Long> ids =(  List<Long>) holder.find(Post.class, query, null, null, 1, 10);
//        Assert.assertEquals(1, ids.size());
//        Assert.assertEquals(3l, ids.get(0).longValue());
//
//        holder.delete(Arrays.asList(post3));
//        for (int i = 0; i < 10; i++)
//            holder.optimize(Post.class);
//
//        List<Long> afterDeleted =  (  List<Long>)  holder.find(Post.class, query, null, null, 1, 10);
//        Assert.assertEquals(0, afterDeleted.size());
//    }
//
//    @Test
//    public void testMultiIndex() throws Exception {
//        holder.add(Arrays.asList(post1, post2, post3));
//        holder.add(Arrays.asList(person1, person2, person3));
//        Query query = SearchHelper.makeQuery("title", "蜘蛛侠", 10.f);
//        List<Searchable> results = holder.find(Arrays.asList(Post.class, Person.class), query, null, null, 1, 10);
//        Assert.assertEquals(1, results.size());
//        Assert.assertEquals(3l, results.get(0).id());
//
//        person3.setTitle("数据库");
//        holder.update(Arrays.asList(person3));
//        for (int i = 0; i < 10; i ++){
//            holder.optimize(Person.class);
//        }
//
//        List<Searchable> results1 = holder.find(Arrays.asList(Post.class, Person.class),
//                SearchHelper.makeQuery("title", "数据库", 10.f), null, null, 1, 10);
//        Assert.assertEquals(2, results1.size());
//
//        Assert.assertEquals(2, holder.count(Arrays.asList(Post.class, Person.class), SearchHelper.makeQuery("title", "数据库", 10.f), null));
//        for (int i = 0; i < 10; i ++){
//            holder.optimize(Person.class);
//        }
//
//        Assert.assertEquals(0, holder.count(Arrays.asList(Post.class, Person.class), SearchHelper.makeQuery("title", "蜘蛛侠", 10.f), null));
//    }
}
