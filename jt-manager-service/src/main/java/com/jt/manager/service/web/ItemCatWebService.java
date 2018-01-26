package com.jt.manager.service.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manager.pojo.ItemCat;
import com.jt.manager.service.BaseService;

@Service
public class ItemCatWebService extends BaseService<ItemCat>{
	
	public ItemCatResult queryItemCatAll(){
		
		ItemCatResult result = new ItemCatResult();
		//查询出所有的分类,并在内存中生成树形结构
		List<ItemCat> itemCats = super.queryAll();
		//存储在map中，key为parentId，value为数据集合
		Map<Long,List<ItemCat>>	itemCatMap = new HashMap<Long,List<ItemCat>>();
		
		for (ItemCat itemCat : itemCats) {
			if(!itemCatMap.containsKey(itemCat.getParentId())){
				itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
			}
			//将不同级别的itemCat放进对应key下的List集合中
			itemCatMap.get(itemCat.getParentId()).add(itemCat);
		}
		//封装一级对象
		List<ItemCatData> itemCatDataList1 = new ArrayList<ItemCatData>();
		//获取所有一级分类对象
		List<ItemCat> itemCatList1 = itemCatMap.get(0L);
		for (ItemCat itemCat : itemCatList1) {
			ItemCatData itemCatData1 = new ItemCatData();
			itemCatData1.setUrl("/products/"+itemCat.getId()+".html");
			itemCatData1.setName("<a href='"+itemCatData1.getUrl()+"'>"+itemCat.getName()+"</a>");
			
			result.getItemCats().add(itemCatData1);
			//若一级分类没有二级分类则跳过
			if(!itemCat.getIsParent()){
				continue;
			}
			
			//封装二级对象
			List<ItemCatData> itemCatDataList2 = new ArrayList<ItemCatData>();
			List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
			for (ItemCat itemCat2 : itemCatList2) {
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/"+itemCat2.getId()+".html");
				itemCatData2.setName("<a href='"+itemCatData2.getUrl()+"'>"+itemCat2.getName()+"</a>");
				
				if(itemCat2.getIsParent()){//存在下一级分类
					//封装三级对象
					List<String> itemCatDataList3 = new ArrayList<String>();
					List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
					for (ItemCat itemCat3 : itemCatList3) {
						itemCatDataList3.add("/products/"+itemCat3.getId()+".html|"+itemCat3.getName());
					}
					itemCatData2.setItems(itemCatDataList3);
				}
				itemCatDataList2.add(itemCatData2);
			}
			itemCatData1.setItems(itemCatDataList2);
			if(result.getItemCats().size()>=14){
				break;
			}
		}
		return result;
	}
}














