package com.github.dawn9117.rlock.core.compensator;

import com.github.dawn9117.rlock.core.lock.LockContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 失败重试补偿器
 *
 * @author HEBO
 */
@Slf4j
public class RetryLockCompensator implements LockFailedCompensator {

	/**
	 * 默认重试次数
	 */
	public static final int DEFAULT_RETRY = Integer.MAX_VALUE;

	/**
	 * 默认重试等待时间
	 */
	public static final long DEFAULT_RETRY_WAIT_TIME = 1000L;

	@Override
	public Boolean compensate(LockContext context) throws Throwable {
//		for (int i = 0; i < context.getRetry(); i++) {
//			Thread.sleep(TimeUnit.MILLISECONDS.convert(context.getRetryWaitTime(), context.getTimeunit()));
//			if (invoker.lock(context.getKey(), context.getExpire())) {
//				return Boolean.TRUE;
//			}
//		}
		return Boolean.FALSE;
	}

}
