package com.github.dawn9117.rlock.core.creator;

import com.github.dawn9117.rlock.common.enums.LockModel;
import org.redisson.api.RLock;

/**
 * 可重入锁创建器
 *
 * @author HEBO
 */
public class ReentrantLockCreator extends AbstractLockCreator {

	@Override
	protected RLock create(String... keys) {
		return redissonClient.getLock(keys[0]);
	}


	@Override
	protected LockModel getLockModel() {
		return LockModel.REENTRANT;
	}
}
