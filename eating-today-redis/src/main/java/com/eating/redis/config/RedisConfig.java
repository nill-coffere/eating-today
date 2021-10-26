package com.eating.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author han bin
 **/
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private  String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String passWord;

    @Value("${spring.redis.database}")
    private int dataBase;

    @Value("${spring.redis.timeout}")
    private long timeOut;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    /**
     * connection pool
     */
    private final Map<Integer, JedisConnectionFactory> REDIS_DB_LIST = new ConcurrentHashMap<>();

    /**
     * object lock
     */
    private final Object LOCK = new Object();

    /**
     * get redis connection by database
     * @param index
     * @return
     */
    public StringRedisTemplate getRedisTemplate(Integer index) {
        StringRedisTemplate result;
        if(Objects.isNull(index) || index > 9 || index < 0){
            index = dataBase;
        }
        if (REDIS_DB_LIST.containsKey(index)) {
            result = new StringRedisTemplate(REDIS_DB_LIST.get(index));
        } else{
            synchronized (LOCK) {
                result = REDIS_DB_LIST.containsKey(index) ? new StringRedisTemplate(REDIS_DB_LIST.get(index))
                        : createRedisTemplate(index);
            }
        }
        return result;
    }

    /**
     * create redis connection by database
     * @param index
     * @return
     */
    private StringRedisTemplate createRedisTemplate(int index) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(RedisPassword.of(passWord));
        config.setDatabase(index);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder().usePooling().
                poolConfig(jedisPoolConfig).and().readTimeout(Duration.ofMillis(timeOut)).build();

        JedisConnectionFactory jedisFactory = new JedisConnectionFactory(config, jedisConfig);
        jedisFactory.afterPropertiesSet();
        jedisFactory.getPoolConfig().setMaxIdle(maxIdle);
        jedisFactory.getPoolConfig().setMinIdle(minIdle);
        REDIS_DB_LIST.put(index, jedisFactory);
        return new StringRedisTemplate(jedisFactory);
    }

}
