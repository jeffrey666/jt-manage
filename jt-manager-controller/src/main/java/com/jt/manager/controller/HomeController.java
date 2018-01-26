package com.jt.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class HomeController {
	@RequestMapping("/{pageName}")
	public String index(@PathVariable()String pageName){
		
		return pageName;
	}
	
}
