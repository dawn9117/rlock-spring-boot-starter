package com.github.dawn9117.rlock.core.creator;

import com.github.dawn9117.rlock.common.enums.LockModel;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;

/**
 * 多锁创建器
 *
 * @author HEBO
 */
public class MultipleLockCreator extends AbstractLockCreator {

	@Override
	protected RLock create(String... keys) {
		return new RedissonMultiLock(getLocks(keys));
	}


	@Override
	protected LockModel getLockModel() {
		return LockModel.MULTIPLE;
	}
}
