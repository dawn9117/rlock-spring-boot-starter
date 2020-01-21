package com.github.dawn9117.rlock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * rlock配置
 *
 * @author HEBO
 */
@Data
public class RlockConfig {

	@Bean
	@ConfigurationProperties(prefix = "rlock")
	public RlockProperties rlockProperties() {
		return new RlockProperties();
	}

	@Bean
	@ConfigurationProperties(prefix = "redisson")
	public RedissonProperties redissonProperties() {
		return new RedissonProperties();
	}

}
