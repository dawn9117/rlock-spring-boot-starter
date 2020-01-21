package com.github.dawn9117.rlock.core.lock;

import com.github.dawn9117.rlock.common.enums.LockModel;
import lombok.Builder;
import lombok.Data;

/**
 * 锁上下文
 *
 * @author HEBO
 */
@Data
@Builder
public class LockContext {

	/**
	 * 获取锁时等待的最大时间, -1一直等待
	 */
	private Long waitTime;

	/**
	 * 持有锁的最大时间, 到时间锁自动释放, 也可以unlock手动释放
	 */
	private Long leaseTime;

	/**
	 * 构建锁的key
	 */
	private String[] keys;

	/**
	 * 锁类型
	 *
	 * @see LockModel
	 */
	private LockModel lockModel;

}
