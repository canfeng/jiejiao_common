package com.jiejiao.common.utils;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 * 
 * @author SZG
 * 
 */
public class JedisPoolUtil {
	
	private static ResourceBundle bundle = null;
	private static JedisPoolConfig config=null;
	private static JedisPool jedisPool = null;
	
	static { 
		bundle=ResourceBundle.getBundle("redis");
	    config = new JedisPoolConfig();
		config.setMaxTotal(Integer.parseInt(bundle.getString("redis.maxTotal")));
		config.setMaxIdle(Integer.parseInt(bundle.getString("redis.maxIdle")));
		config.setMaxWaitMillis(Long.parseLong(bundle.getString("redis.maxWaitMillis")));
		config.setTestOnBorrow(Boolean.parseBoolean(bundle.getString("redis.testOnBorrow")));
		config.setTestOnReturn(Boolean.parseBoolean(bundle.getString("redis.testOnReturn")));
		config.setTestWhileIdle(Boolean.parseBoolean(bundle.getString("redis.testWhileIdle")));
		config.setMinEvictableIdleTimeMillis(Long.parseLong(bundle.getString("redis.minEvictableIdleTimeMillis")));
		config.setTimeBetweenEvictionRunsMillis(Long.parseLong(bundle.getString("redis.timeBetweenEvictionRunsMillis")));
		config.setNumTestsPerEvictionRun(Integer.parseInt(bundle.getString("redis.numTestsPerEvictionRun")));
		jedisPool = new JedisPool(config, bundle.getString("redis.hostName"), Integer.parseInt(bundle.getString("redis.port")), 60000);
	}

	public static Jedis getJedis() {
		return jedisPool.getResource();
	}

	public static void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public static boolean exist(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
		return false;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] get(byte[] key) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	public static void set(byte[] key, byte[] value) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	public static void set(byte[] key, byte[] value, int time) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}
	
	public static void set(String key, String value, int time) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.expire(key, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	public static void hset(byte[] key, byte[] field, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	public static void hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String hget(String key, String field) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			// 释放redis对象

			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] hget(byte[] key, byte[] field) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			// 释放redis对象

			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}

		return value;
	}

	public static void hdel(byte[] key, byte[] field) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hdel(key, field);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}

	/**
	 * 存储REDIS队列 顺序存储
	 * 
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void lpush(byte[] key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.lpush(key, value);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis);
		}
	}
	
	 /**
     * 存储REDIS队列 反向存储
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpush(byte[] key, byte[] value) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.rpush(key, value);
 
        } catch (Exception e) {
 
             
             
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
 
    /**
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public static void rpoplpush(byte[] key, byte[] destination) {
 
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.rpoplpush(key, destination);
 
        } catch (Exception e) {
 
             
             
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
    }
 
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public static List<byte[]> lpopList(byte[] key) {
 
        List<byte[]> list = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            list = jedis.lrange(key, 0, -1);
 
        } catch (Exception e) {
 
             
             
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
        return list;
    }
 
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public static byte[] rpop(byte[] key) {
 
        byte[] bytes = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            bytes = jedis.rpop(key);
 
        } catch (Exception e) {
 
             
             
            e.printStackTrace();
 
        } finally {
 
            //返还到连接池
            close(jedis);
 
        }
        return bytes;
    }
 
    public static void hmset(Object key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
        } catch (Exception e) {
             
             
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
    }
 
    public static void hmset(Object key, Map<String, String> hash, int time) {
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            jedis.hmset(key.toString(), hash);
            jedis.expire(key.toString(), time);
        } catch (Exception e) {
             
             
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
    }
 
    public static List<String> hmget(Object key, String... fields) {
        List<String> result = null;
        Jedis jedis = null;
        try {
 
            jedis = jedisPool.getResource();
            result = jedis.hmget(key.toString(), fields);
 
        } catch (Exception e) {
             
             
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
        return result;
    }
 
    public static Set<String> hkeys(String key) {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hkeys(key);
 
        } catch (Exception e) {
             
             
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
        return result;
    }
 
    public static List<byte[]> lrange(byte[] key, int from, int to) {
        List<byte[]> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, from, to);
 
        } catch (Exception e) {
             
             
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
 
        }
        return result;
    }
 
    public static Map<byte[],?> hgetAll(byte[] key) {
        Map<byte[],?> result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
        } catch (Exception e) {
             
             
            e.printStackTrace();
 
        } finally {
            //返还到连接池
            close(jedis);
        }
        return result;
    }
 
    public static void del(byte[] key) {
 
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
             
             
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
    }
 
    public static long llen(byte[] key) {
 
        long len = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.llen(key);
        } catch (Exception e) {
             
             
            e.printStackTrace();
        } finally {
            //返还到连接池
            close(jedis);
        }
        return len;
    }

}
