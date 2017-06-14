package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="topicDestination")
	private Topic topicDestination;
	
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;
	
	

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		
		//分页处理
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取总记录数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}



	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		final long id = IDUtils.genItemId();
		item.setId(id);
		item.setCreated(new Date());
		item.setStatus((byte) 1);
		item.setUpdated(new Date());
		itemMapper.insert(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(id);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//插入商品描述
		itemDescMapper.insert(itemDesc);
		jmsTemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(id + "");
				
				return message;
			}
		});
		return TaotaoResult.ok();
	}






	

	
	

}
