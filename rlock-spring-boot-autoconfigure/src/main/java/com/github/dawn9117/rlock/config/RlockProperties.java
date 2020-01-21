package com.github.dawn9117.rlock.config;

import com.github.dawn9117.rlock.common.enums.LockModel;
import lombok.Data;

/**
 * 锁配置
 *
 * @author HEBO
 */
@Data
public class RlockProperties {

	/**
	 * 注解开关(默认开启)
	 */
	private Boolean enabled = Boolean.TRUE;

	/**
	 * 锁名称前缀(所有的锁名称都会以prefix开头)
	 */
	private String prefix = "rlock";

	/**
	 * 项目名称
	 */
	private String project = "default";

	/**
	 * 锁类型
	 */
	private LockModel lockModel;
}
