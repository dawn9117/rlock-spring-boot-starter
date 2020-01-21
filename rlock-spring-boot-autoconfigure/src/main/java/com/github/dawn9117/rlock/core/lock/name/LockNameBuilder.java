package com.github.dawn9117.rlock.core.lock.name;

/**
 * 锁名称构建器
 *
 * @author HEBO
 */
public interface LockNameBuilder {

	String DELIMITER = ":";

	/**
	 * 构建锁名称
	 *
	 * @param context 锁上下文
	 * @return String
	 * @throws Throwable 异常
	 */
	String[] build(LockNameContext context) throws Throwable;

}
