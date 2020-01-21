package com.github.dawn9117.rlock.core.creator;

import com.github.dawn9117.rlock.common.enums.LockModel;
import org.redisson.api.RLock;

/**
 * 公平锁创建器
 *
 * @author HEBO
 */
public class FairLockCreator extends AbstractLockCreator {

	@Override
	protected RLock create(String... keys) {
		return redissonClient.getFairLock(keys[0]);
	}

	@Override
	protected LockModel getLockModel() {
		return LockModel.FAIR;
	}

}
