package com.github.dawn9117.rlock.core.creator;

import com.github.dawn9117.rlock.common.enums.LockModel;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;

/**
 * 红锁创建器
 *
 * @author HEBO
 */
public class RedLockCreator extends AbstractLockCreator {

	@Override
	protected RLock create(String... keys) {
		return new RedissonRedLock(getLocks(keys));
	}

	@Override
	protected LockModel getLockModel() {
		return LockModel.RED_LOCK;
	}

}
