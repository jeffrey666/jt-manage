package com.jt.manager.mapper;

import java.util.List;

import com.jt.common.mapper.SysMapper;
import com.jt.manager.pojo.ItemCat;

public interface ItemCatMapper extends SysMapper<ItemCat>{
	
	public List<ItemCat> queryItemCatList(Integer parentId);
}
