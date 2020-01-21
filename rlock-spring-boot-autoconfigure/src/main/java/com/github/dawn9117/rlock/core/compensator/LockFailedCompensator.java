package com.github.dawn9117.rlock.core.compensator;

import com.github.dawn9117.rlock.core.lock.LockContext;

/**
 * 加锁失败补偿器
 *
 * @author HEBO
 */
public interface LockFailedCompensator {

	/**
	 * 加锁失败会调用该方法:
	 * 该方法如果返回true, 则表示补偿成功继续执行方法, 否则就抛出异常
	 *
	 * @param lockContext 锁信息
	 * @return Boolean
	 * @throws Throwable 补偿异常
	 */
	Boolean compensate(LockContext lockContext) throws Throwable;

}
