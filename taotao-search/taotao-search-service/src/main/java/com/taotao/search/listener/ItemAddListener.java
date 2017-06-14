package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.ItemMapper;

public class ItemAddListener implements MessageListener {


	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		//取消息的内容
				try {
					TextMessage textMessage = (TextMessage) message;
					//取内容
					String itmeIdstr = textMessage.getText();
					Long itemId = new Long(itmeIdstr);
					//根据商品Id查询商品消息
					SearchItem searchItem = itemMapper.getItemById(itemId);
					//把商品消息添加到索引库
					SolrInputDocument document = new SolrInputDocument();
					// 4、为文档添加域
					document.addField("id", searchItem.getId());
					document.addField("item_title", searchItem.getTitle());
					document.addField("item_sell_point", searchItem.getSell_point());
					document.addField("item_price", searchItem.getPrice());
					document.addField("item_image", searchItem.getImage());
					document.addField("item_category_name", searchItem.getCategory_name());
					document.addField("item_desc", searchItem.getItem_des());
					// 5、向索引库中添加文档。
					solrServer.add(document);
					//提交
					solrServer.commit();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		
	}

}
