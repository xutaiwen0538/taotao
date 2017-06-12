package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;


public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(Long parentId);

	TaotaoResult insertCategory(Long parentId, String name);

	TaotaoResult updateName(Long id, String name);

	TaotaoResult delete(Long id);

	
}
