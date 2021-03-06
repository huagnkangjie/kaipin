package com.kaipin.search.repository.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kaipin.search.common.page.IGenericPage;
import com.kaipin.search.repository.dao.IndexBuildDao;
import com.kaipin.search.repository.mybatis.dao.MybatisBaseDAO;
import com.kaipin.search.repository.mybatis.dao.MybatisBaseDAO.IBasePageSql;

@Repository(value = "companyInfoDao")
public class CompanyInfoBuildDaoImpl extends MybatisBaseDAO<Map, String>implements IndexBuildDao {

	@Override
	public IGenericPage getSearchAll(Map<String, Object> param, int pageNo, int pageSize) {

		IBasePageSql sql = new IBasePageSql() {

			@Override
			public String getPageListSqlName() {

				return "getSearchAll";
			}

			@Override
			public String getPageCountSqlName() {

				return "getSearchAllCount";
			}
		};

		return findPageBy(sql, param, pageNo, pageSize, null, null);
	}

	@Override
	public Map<String, Object> selectByPrimaryKey(String id) {

		return this.getById("selectByPrimaryKey", id);

	}

	public List<Map<String, Object>> selectAppSearchRecommend(List<String> list) {

		return this.selectList("selectAppSearchRecommend", list);
	}

	public List<Map<String, Object>> selectAppSearchResult(List<String> list) {
		return this.selectList("selectAppSearchResult", list);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> selectWebSearchResult(List<String> list) {
		try {
			return this.selectList("selectWebSearchResult", list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getDefaultSqlNamespace() {

		return "CompanyInfoMapper";
	}

	@Override
	public List<Map<String, Object>> selectWebSearchResultByMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
