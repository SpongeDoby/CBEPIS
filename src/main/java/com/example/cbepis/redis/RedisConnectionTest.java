package com.example.cbepis.redis;

import redis.clients.jedis.Jedis;

public class RedisConnectionTest {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost");
        //String
        System.out.println(jedis.ping());
    }
}
