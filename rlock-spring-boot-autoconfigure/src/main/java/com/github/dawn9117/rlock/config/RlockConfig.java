package com.github.dawn9117.rlock.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * rlock配置
 *
 * @author HEBO
 */
@Data
public class RlockConfig {

	@Bean
	@ConditionalOnMissingBean(RlockProperties.class)
	@ConfigurationProperties(prefix = "rlock")
	public RlockProperties rlockProperties() {
		return new RlockProperties();
	}

	@Bean("rlockRedissonProperties")
	@ConditionalOnMissingBean(RedissonProperties.class)
	@ConfigurationProperties(prefix = "spring.redis.redisson")
	public RedissonProperties redissonProperties() {
		return new RedissonProperties();
	}

	@Bean
	@Primary
	@ConditionalOnMissingBean(RedisProperties.class)
	public RedisProperties redisProperties() {
		return new RedisProperties();
	}

}
