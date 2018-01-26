package com.jt.manager.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.manager.mapper.ItemCatMapper;
import com.jt.manager.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat>{
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	//使用普通的redis集群模式
	@Autowired
	private RedisService redisService;
	//使用redis cluster模式
//	@Autowired
//	private JedisCluster cluster;
	//使用主从哨兵模式
//	@Autowired
//	private RedisSentinelService sentinelService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static final Logger log = Logger.getLogger(ItemCatService.class);
	public List<ItemCat> queryItemCatList(Integer parentId){
		//设置key的名称
		String ITEM_CAT_KEY ="ITEM_CAT"+parentId;
		//从redis缓存中获取value
//		String  value= redisService.get(ITEM_CAT_KEY);
		String  value= redisService.get(ITEM_CAT_KEY);
		List<ItemCat> itemCatList = null;
		if(StringUtils.isNotEmpty(value)){//value值非空
			try {
				//将缓存中的json字符串转换为对象
				JsonNode jsonNode = MAPPER.readTree(value);
				itemCatList =MAPPER.readValue(jsonNode.traverse(),MAPPER.getTypeFactory().constructCollectionType(List.class, ItemCat.class));
			}catch(Exception e){
				//写日志
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}else{
			itemCatList = itemCatMapper.queryItemCatList(parentId);
			//将对象变成json字符串
			String json = null;
			try {
				//存入redis缓存中
				json = MAPPER.writeValueAsString(itemCatList);
//				redisService.set(ITEM_CAT_KEY,json);
				redisService.set(ITEM_CAT_KEY,json);
			} catch (Exception e) {
				//写日志
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return itemCatList;
	}
}
