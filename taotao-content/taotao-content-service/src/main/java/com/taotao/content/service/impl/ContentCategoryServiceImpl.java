package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容管理Service
 * <p>Title: ContentServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper categoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for(TbContentCategory category:list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(category.getId());
			node.setText(category.getName());
			node.setState(category.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}

	@Override
	public TaotaoResult insertCategory(Long parentId, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setIsParent(false);
		category.setName(name);
		category.setParentId(parentId);
		category.setSortOrder(1);
		category.setUpdated(new Date());
		category.setCreated(new Date());
		categoryMapper.insert(category);
		TbContentCategory parent = categoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			categoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok(category);
	}

	@Override
	public TaotaoResult updateName(Long id, String name) {
		TbContentCategory category = categoryMapper.selectByPrimaryKey(id);
		category.setName(name);
		categoryMapper.updateByPrimaryKey(category);
		return TaotaoResult.ok(category);
	}

	@Override
	public TaotaoResult delete(Long id) {
		//查询parentId
		Long parentId = categoryMapper.selectByPrimaryKey(id).getParentId();
		//查询parentId下是否还有子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		if (list.size()==1) {
			TbContentCategory category = categoryMapper.selectByPrimaryKey(parentId);
			category.setIsParent(false);
			categoryMapper.updateByPrimaryKey(category);
		}
		//判断是否子元素
		deleteCategory(id);
		return TaotaoResult.ok();
	}

	private void deleteCategory(Long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> zList = categoryMapper.selectByExample(example);
		if (zList.size()==0) {
			//直接删除
			categoryMapper.deleteByPrimaryKey(id);
		}else{//递归删除
			for (TbContentCategory c:zList) {
				deleteCategory(c.getId());
			}
			categoryMapper.deleteByPrimaryKey(id);
		}
	}
	
}
