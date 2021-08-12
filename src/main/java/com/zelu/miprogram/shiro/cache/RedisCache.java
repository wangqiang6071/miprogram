package com.zelu.miprogram.shiro.cache;

import com.zelu.miprogram.config.ApplicationTextUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Set;


//redis的缓存
@Service
public class RedisCache<k,v> implements Cache<k,v> {
    //缓存的get put delete clear size  首先shiro调用get换取幻缓存的数据  如果没有 查询数据 在调用set 把数据缓存到redis
    //默认构造器
    public RedisCache(){}

    //这个构造器的作用是把缓存名字传过来
    private String cacheName;

    public RedisCache(String cacheName){
       this.cacheName=cacheName;
    }

    @Override
    public v get(k k) throws CacheException {
        //(v)getRedisTemplate().opsForValue().get(k);
        return (v)getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public v put(k k, v v) throws CacheException {
        //getRedisTemplate().opsForValue().set(k.toString(),v);
        getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public v remove(k k) throws CacheException {
        //(v)getRedisTemplate().delete(k);
        return (v)getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().opsForHash().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return null;
    }

    @Override
    public Collection<v> values() {
        return null;
    }
    //封装获取redistemplate的方法
    public RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationTextUtils.getBeanObj("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);//开启事物
        return redisTemplate;
    }
}
