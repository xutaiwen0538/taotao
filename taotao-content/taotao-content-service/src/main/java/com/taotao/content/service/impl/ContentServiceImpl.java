package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;

/**
 * 内容管理Service
 * <p>Title: ContentServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;

	@Override
	public EasyUIDataGridResult getContentList(Integer page,
			Integer rows, Long categoryId) {
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExample(example);
		result.setRows(list);
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public TaotaoResult insertContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentList(Long categoryId) {
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		return list;
	}
	
}
