package com.github.dawn9117.rlock.core.compensator;

import com.github.dawn9117.rlock.core.lock.LockContext;

/**
 * 加锁失败补偿器: 直接返回失败
 *
 * @author HEBO
 */
public class DirectFailedCompensator implements LockFailedCompensator {
	@Override
	public Boolean compensate(LockContext lockContext) throws Throwable {
		return false;
	}
}
