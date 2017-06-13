package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.ItemSearchDao;
import com.taotao.search.service.SearchService;


@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private ItemSearchDao itemSearchDao;
	
	@Override
	public SearchResult search(String queryString, Integer page,
			Integer rows) throws Exception {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		query.setRows(rows);
		if (page<1) {
			page = 1;
		}
		query.setStart((page-1)*rows);
		
		query.set("df","item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		SearchResult searchResult = itemSearchDao.search(query);
		long recordCount = searchResult.getRecordCount();
		long pageCount = recordCount / rows;
		if (recordCount % rows > 0) {
			pageCount++;
		}
		searchResult.setPageCount(pageCount);
		// 返回SearchResult
		return searchResult;
	}
	

}
