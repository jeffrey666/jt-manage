package com.jt.manager.mapper;

import java.util.List;

import com.jt.common.mapper.SysMapper;
import com.jt.manager.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{
	public List<Item> queryItemList();
}
