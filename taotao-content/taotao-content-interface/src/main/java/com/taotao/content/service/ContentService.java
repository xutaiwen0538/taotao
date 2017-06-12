package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;


public interface ContentService {

	EasyUIDataGridResult getContentList(Integer page, Integer rows,
			Long categoryId);

	TaotaoResult insertContent(TbContent content);

	List<TbContent> getContentList(Long categoryId);

	
}
