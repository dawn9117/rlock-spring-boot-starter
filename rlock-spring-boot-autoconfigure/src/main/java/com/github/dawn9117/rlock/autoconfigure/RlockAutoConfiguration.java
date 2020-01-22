package com.github.dawn9117.rlock.autoconfigure;

import com.github.dawn9117.rlock.aop.LockAspect;
import com.github.dawn9117.rlock.common.util.AppContextHolder;
import com.github.dawn9117.rlock.config.RedissonProperties;
import com.github.dawn9117.rlock.config.RlockConfig;
import com.github.dawn9117.rlock.config.RlockProperties;
import com.github.dawn9117.rlock.core.selector.LockCreatorSelector;
import com.github.dawn9117.rlock.core.selector.LockNameBuilderSelector;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Import({AppContextHolder.class, RlockConfig.class, LockNameBuilderSelector.class, LockCreatorSelector.class})
@ConditionalOnProperty(name = "enabled", prefix = "rlock", matchIfMissing = true, havingValue = "true")
public class RlockAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(LockAspect.class)
	public LockAspect lockAspect(RlockProperties rlockProperties) {
		return new LockAspect(rlockProperties);
	}


	@Bean
	@ConditionalOnMissingBean(Config.class)
	@SuppressWarnings("all")
	public Config config(RedissonProperties redissonProperties, RedisProperties redisProperties) {
		Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
		Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
		Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
		int timeout;
		if (null == timeoutValue) {
			timeout = 10000;
		} else if (!(timeoutValue instanceof Integer)) {
			Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
			timeout = ((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
		} else {
			timeout = (Integer) timeoutValue;
		}

		Config config;
		if (redissonProperties.getConfig() != null) {
			try {
				InputStream is = getConfigStream(redissonProperties.getConfig());
				config = Config.fromJSON(is);
			} catch (IOException e) {
				// trying next format
				try {
					InputStream is = getConfigStream(redissonProperties.getConfig());
					config = Config.fromYAML(is);
				} catch (IOException e1) {
					throw new IllegalArgumentException("Can't parse config", e1);
				}
			}
		} else if (redisProperties.getSentinel() != null) {
			Method nodesMethod = ReflectionUtils.findMethod(Sentinel.class, "getNodes");
			Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

			String[] nodes;
			if (nodesValue instanceof String) {
				nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
			} else {
				nodes = convert((List<String>) nodesValue);
			}

			config = new Config();
			config.useSentinelServers()
					.setMasterName(redisProperties.getSentinel().getMaster())
					.addSentinelAddress(nodes)
					.setDatabase(redisProperties.getDatabase())
					.setConnectTimeout(timeout)
					.setPassword(redisProperties.getPassword());
		} else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
			Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
			Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
			List<String> nodesObject = (List<String>) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);

			String[] nodes = convert(nodesObject);

			config = new Config();
			config.useClusterServers()
					.addNodeAddress(nodes)
					.setConnectTimeout(timeout)
					.setPassword(redisProperties.getPassword());
		} else {
			config = new Config();
			String prefix = "redis://";
			Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
			if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
				prefix = "rediss://";
			}

			config.useSingleServer()
					.setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
					.setConnectTimeout(timeout)
					.setDatabase(redisProperties.getDatabase())
					.setPassword(redisProperties.getPassword());
		}
		return config;
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnBean(Config.class)
	@ConditionalOnMissingBean(RedissonClient.class)
	public RedissonClient redisson(Config config) {
		return Redisson.create(config);
	}

	private String[] convert(List<String> nodesObject) {
		List<String> nodes = new ArrayList<>(nodesObject.size());
		for (String node : nodesObject) {
			if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
				nodes.add("redis://" + node);
			} else {
				nodes.add(node);
			}
		}
		return nodes.toArray(new String[0]);
	}

	private InputStream getConfigStream(String path) {
		return this.getClass().getClassLoader().getResourceAsStream(path);
	}

}
