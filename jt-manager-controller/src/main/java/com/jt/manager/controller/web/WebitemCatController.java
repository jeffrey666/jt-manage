package com.jt.manager.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.ItemCatResult;
import com.jt.manager.service.web.ItemCatWebService;

@Controller
@RequestMapping("web/itemcat")
public class WebitemCatController {
	@Autowired
	private ItemCatWebService itemCatWebService;
	
	@RequestMapping("all")
	@ResponseBody
	public ItemCatResult queryItemCatList(){
		ItemCatResult result = itemCatWebService.queryItemCatAll();
		return result;
	}
}
