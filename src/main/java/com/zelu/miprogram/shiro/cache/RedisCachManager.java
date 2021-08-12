package com.zelu.miprogram.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

//自定义的类=》redis缓存管理器
public class RedisCachManager implements CacheManager {
    //这里的name值为 授权的值(authorizationCacheName=自定义的值)
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        System.out.println("getCache===>"+name);
        return new RedisCache<K,V>(name);
    }
}
