package com.github.dawn9117.rlock.config;


import lombok.Data;

/**
 * 单节点配置
 */
@Data
public class SingleServerConfig {

	private String address;

	private Integer subscriptionConnectionMinimumIdleSize = 1;

	private Integer subscriptionConnectionPoolSize = 50;

	private Integer connectionMinimumIdleSize = 32;

	private Integer connectionPoolSize = 64;

	private Integer database = 0;

	private Long dnsMonitoringInterval = 5000L;

}
