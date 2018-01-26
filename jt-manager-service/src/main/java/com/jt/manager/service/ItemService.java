package com.jt.manager.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.service.RedisService;
import com.jt.common.vo.EasyUIResult;
import com.jt.manager.mapper.ItemDescMapper;
import com.jt.manager.mapper.ItemMapper;
import com.jt.manager.pojo.Item;
import com.jt.manager.pojo.ItemDesc;
@Service
public class ItemService extends BaseService<Item>{
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private RedisService redisService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public EasyUIResult queryItemList(Integer page,Integer rows){
		//设定缓存中的key
		String ITEM_PAGE_ROWS="ITEM_"+page+"_"+rows;
		
		String jsonData = redisService.get(ITEM_PAGE_ROWS);
		List<Item> itemList = null;
		if(StringUtils.isNotEmpty(jsonData)){//缓存中存在
			try {//将字符串转换为对象
				JsonNode jsonNode=MAPPER.readTree(jsonData);
				itemList = MAPPER.readValue(jsonNode.traverse(),
						MAPPER.getTypeFactory().constructCollectionType(List.class, Item.class));
			} catch (Exception e) {
				//写日志
				e.printStackTrace();
			}
		}else{
			PageHelper.startPage(page, rows);
			itemList=itemMapper.queryItemList();
			
			try {//存入缓存中
				String json=MAPPER.writeValueAsString(itemList);
				redisService.set(ITEM_PAGE_ROWS, json, 180);
				
			} catch (Exception e) {
				//写日志
				e.printStackTrace();
			}
			
		}
		
		PageInfo<Item> pageInfo = new PageInfo<Item>(itemList);
		
		return new EasyUIResult(pageInfo.getTotal(),itemList);
	}
	
	public void saveItem(Item item,String desc){
		//删除内存中存在的K-V
		
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insertSelective(item);
		//商品详情
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
		//将新增的商品及详情存入redis缓存中
		String ITEM_KEY= "ITEM_"+item.getId();
		String ITEM_DESC_KEY = "ITEM_DESC_"+item.getId();
		try {
			String itemData = MAPPER.writeValueAsString(item);
			String itemDescData=MAPPER.writeValueAsString(itemDesc);
			redisService.set(ITEM_KEY, itemData, 60*60*24);
			redisService.set(ITEM_DESC_KEY, itemDescData, 60*60*24);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateItem(Item item, String desc){
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		//商品详情
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateByPrimaryKey(itemDesc);
		//将新增的商品及详情存入redis缓存中
		String ITEM_KEY= "ITEM_"+item.getId();
		String ITEM_DESC_KEY = "ITEM_DESC_"+item.getId();
		try {
			//删除旧的缓存
			redisService.del(ITEM_KEY);
			redisService.del(ITEM_DESC_KEY);
			//存入新的商品信息
			String itemData = MAPPER.writeValueAsString(item);
			String itemDescData=MAPPER.writeValueAsString(itemDesc);
			redisService.set(ITEM_KEY, itemData, 60*60*24);
			redisService.set(ITEM_DESC_KEY, itemDescData, 60*60*24);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteItems(Long[] ids){
		itemDescMapper.deleteByIDS(ids);
		itemMapper.deleteByIDS(ids);
		//删除旧的缓存
		for(Long id:ids){
			String ITEM_KEY= "ITEM_"+id;
			String ITEM_DESC_KEY = "ITEM_DESC_"+id;
			redisService.del(ITEM_KEY);
			redisService.del(ITEM_DESC_KEY);
		}
	}

	public ItemDesc queryItemDesc(Long itemId) {
		String ITEM_DESC_KEY = "ITEM_DESC_"+itemId;
		String itemDescData = redisService.get(ITEM_DESC_KEY);
		ItemDesc itemDesc = null;
		try {
			if(StringUtils.isNotEmpty(itemDescData)){//缓存中存在
					itemDesc = MAPPER.readValue(itemDescData, ItemDesc.class);
					return itemDesc;
			}else{//缓存中不存在
				itemDesc =itemDescMapper.selectByPrimaryKey(itemId);
				itemDescData =MAPPER.writeValueAsString(itemDesc);
				redisService.set(ITEM_DESC_KEY, itemDescData,60*60*24);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
}

