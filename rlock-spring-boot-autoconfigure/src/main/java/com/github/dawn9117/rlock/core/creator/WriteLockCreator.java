package com.github.dawn9117.rlock.core.creator;

import com.github.dawn9117.rlock.common.enums.LockModel;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;

/**
 * 写锁创建器
 *
 * @author HEBO
 */
public class WriteLockCreator extends AbstractLockCreator {

	@Override
	protected RLock create(String... keys) {
		RReadWriteLock rwLock = redissonClient.getReadWriteLock(keys[0]);
		return rwLock.writeLock();
	}

	@Override
	protected LockModel getLockModel() {
		return LockModel.WRITE;
	}

}
