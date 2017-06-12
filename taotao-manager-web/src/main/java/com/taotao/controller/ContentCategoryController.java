package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService categortService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(value="id", defaultValue="0")
	Long parentId) {
		 List<EasyUITreeNode> list = categortService.getContentCatList(parentId);
		 return list;
	}
	
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult createCategory(Long parentId,String name) {
		TaotaoResult result = categortService.insertCategory(parentId,name);
		return result;
	}
	

	@RequestMapping("/update")
	@ResponseBody
	public TaotaoResult updateName(Long id,String name) {
		TaotaoResult result = categortService.updateName(id,name);
		return result;
	}
	
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult delete(Long id) {
		TaotaoResult result = categortService.delete(id);
		return result;
	}
}
