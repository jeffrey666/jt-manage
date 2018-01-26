/*package com.jt.jedis;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisTest {
	@Test
	public void jedisTest(){
		Jedis jedis = new Jedis("192.168.217.40",6379,50000);
		String name= jedis.get("name");
		jedis.set("address", "shenzhen");
		System.out.println(name);
		jedis.close();
	}
	
	 * Jedis分片
	 * 如何将分开的独立的redis节点整合到一起来执行分布式的内存数据库功能
	 * 从redis的角度三个节点实例还是分开的，redis提供分片的功能，利用
	 * 底层代码来维护一个分布式的使用
	 
	@Test
	public void jedisShard(){
		//将key对应的存储到唯一的redis节点中，分开存储，分开计算
		//构造一个集合存储redis所有节点的信息
		List<JedisShardInfo> infoList = new ArrayList<JedisShardInfo>();
		//存储节点信息的内容需要对象redisShardInfo对象
		JedisShardInfo  info1 = new JedisShardInfo("192.168.217.40",6379);
		JedisShardInfo  info2 = new JedisShardInfo("192.168.217.40",6380);
		JedisShardInfo  info3 = new JedisShardInfo("192.168.217.40",6381);
		//将对象添加到List中
		infoList.add(info1);
		infoList.add(info2);
		infoList.add(info3);
		ShardedJedis jedis = new ShardedJedis(infoList);
		//什么样的key会对应哪个redis name 是放到6379还是6380
		String s = jedis.get("book");
		System.out.println(s);
		jedis.set("player", "James");
		//key值的取值和redis节点的映射--hash一致性
		jedis.close();
	}
	
	@Test
	public void jedisPool(){
		List<JedisShardInfo> infoList = new ArrayList<JedisShardInfo>();
		JedisShardInfo  info1 = new JedisShardInfo("192.168.217.40",6379);
		JedisShardInfo  info2 = new JedisShardInfo("192.168.217.40",6380);
		JedisShardInfo  info3 = new JedisShardInfo("192.168.217.40",6381);
		infoList.add(info1);
		infoList.add(info2);
		infoList.add(info3);
		//jedis不是new出来的，需要调用jedis池来获取jedis对象
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(200);
		config.setMaxWaitMillis(50000);
		//将池对象创建出来
		ShardedJedisPool pool = new ShardedJedisPool(config,infoList);
		ShardedJedis jedis = pool.getResource();
		String str = jedis.get("book");
		System.out.println(str);
		pool.returnResource(jedis);
		pool.close();
	}
}
*/