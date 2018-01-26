package com.jt.manager.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manager.pojo.Item;
import com.jt.manager.service.ItemService;

@Controller
public class WebItemController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("items/{itemId}")
	@ResponseBody
	public Item queryItemById(@PathVariable Long itemId){
		Item item = itemService.queryById(itemId);
		return item;
	}
}
