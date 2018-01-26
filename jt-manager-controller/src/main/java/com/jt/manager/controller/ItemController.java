package com.jt.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manager.pojo.Item;
import com.jt.manager.pojo.ItemDesc;
import com.jt.manager.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	private static final Logger log= Logger.getLogger(ItemController.class);
	
	@Autowired
	private ItemService itemService;
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult queryItemList(Integer page,Integer rows){
			 
		EasyUIResult easyUIResult=itemService.queryItemList(page,rows);
		
		return easyUIResult;
	}
	
	@RequestMapping("save")
	@ResponseBody
	public SysResult save(Item item,String desc){
		try{
			itemService.saveItem(item,desc);
			log.info("{新增商品成功}");
			return SysResult.oK();
		}catch(Exception e){
			log.error("{新增商品失败："+e.getMessage()+"}");
			return SysResult.build(201, "新增错误："+e.getMessage());
		}
	}
	
	@RequestMapping("update")
	@ResponseBody
	public SysResult update(Item item,String desc){
		try{
			itemService.updateItem(item,desc);
			log.info("{修改商品成功}");
			return SysResult.oK();
		}catch(Exception e){
			log.error("{修改商品失败："+e.getMessage()+"}");
			return SysResult.build(201,"新增错误："+ e.getMessage());
		}
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public SysResult delete(Long[] ids){
		try{
			itemService.deleteItems(ids);
			log.info("{删除商品成功}");
			return SysResult.oK();
		}catch(Exception e){
			log.error("{删除商品失败："+e.getMessage()+"}");
			return SysResult.build(201,"新增错误："+e.getMessage());
		}
	}
	
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult queryItemDesc(@PathVariable Long itemId){
		
		ItemDesc itemDesc = itemService.queryItemDesc(itemId);
		
		return new SysResult(itemDesc);
	}
}
