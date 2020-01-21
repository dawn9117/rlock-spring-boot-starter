package com.github.dawn9117.rlock.core.creator;

import com.github.dawn9117.rlock.common.enums.LockModel;
import org.apache.commons.lang3.ArrayUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象锁构造器
 *
 * @author HEBO
 */
public abstract class AbstractLockCreator implements LockCreator {

	@Autowired(required = false)
	protected RedissonClient redissonClient;

	@Override
	public RLock get(String... keys) {
		if (ArrayUtils.isEmpty(keys)) {
			return null;
		}
		return create(keys);
	}

	@Override
	public Boolean supported(LockModel model) {
		return model.equals(getLockModel());
	}

	protected RLock[] getLocks(String... keys) {
		List<RLock> locks = Arrays.stream(keys).map(key -> redissonClient.getLock(key)).collect(Collectors.toList());
		return locks.toArray(new RLock[0]);
	}

	protected abstract RLock create(String... keys);

	protected abstract LockModel getLockModel();


}
