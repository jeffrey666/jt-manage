package com.jt.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manager.pojo.ItemCat;
import com.jt.manager.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/findall")
	@ResponseBody
	public List<ItemCat> findAll(){
		List<ItemCat> list = itemCatService.queryAll();
		return list;
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public List<ItemCat> queryItemCatList(@RequestParam(defaultValue="0")Integer id){
		List<ItemCat> itemCatList = itemCatService.queryItemCatList(id);
		return itemCatList;
	}
}
