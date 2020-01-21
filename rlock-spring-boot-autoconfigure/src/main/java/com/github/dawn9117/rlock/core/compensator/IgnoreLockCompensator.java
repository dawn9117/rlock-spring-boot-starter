package com.github.dawn9117.rlock.core.compensator;

import com.github.dawn9117.rlock.core.lock.LockContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 加锁失败补偿器: 直接返回true, 不加锁, 直接执行方法
 *
 * @author HEBO
 */
@Slf4j
public class IgnoreLockCompensator implements LockFailedCompensator {
	@Override
	public Boolean compensate(LockContext lockContext) throws Throwable {
		return true;
	}
}
