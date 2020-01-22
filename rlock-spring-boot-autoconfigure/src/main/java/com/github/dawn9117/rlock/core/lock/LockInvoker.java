package com.github.dawn9117.rlock.core.lock;

import com.github.dawn9117.rlock.common.util.AppContextHolder;
import com.github.dawn9117.rlock.core.creator.LockCreator;
import com.github.dawn9117.rlock.excepiton.LockException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 锁执行器
 *
 * @author HEBO
 */
@Data
@Slf4j
public class LockInvoker {

	private LockContext lockContext;

	private RLock rLock;

	public LockInvoker(LockContext lockContext) {
		this.lockContext = lockContext;
		rLock = loadRLock(lockContext);
		if (Objects.isNull(rLock)) {
			throw new LockException("[Rlock] create lock failed");
		}
	}

	/**
	 * 加锁
	 *
	 * @return 是否成功
	 * @throws InterruptedException InterruptedException
	 * @throws LockException        LockException
	 */
	public boolean lock() throws InterruptedException, LockException {
		log.info("[Rlock] prepare lock, lockName:[{}], lockContext:[{}]", rLock.getName(), lockContext);
		if (lockContext.getWaitTime() <= 0) {
			//一直等待加锁
			rLock.lock(lockContext.getLeaseTime(), TimeUnit.MILLISECONDS);
		} else {
			rLock.tryLock(lockContext.getWaitTime(), lockContext.getLeaseTime(), TimeUnit.MILLISECONDS);
		}
		if (!isLocked()) {
			throw new LockException("[Rlock] lock error, lock failed, lockContext:" + lockContext);
		}
		log.info("[Rlock] locked, lockName:[{}]", rLock.getName());
		return Boolean.TRUE;
	}

	/**
	 * 释放锁
	 */
	public void unlock() {
		if (this.isLocked()) {
			rLock.unlock();
		}
		log.info("[Rlock] lock released, lockName:[{}]", rLock.getName());
	}


	/**
	 * 锁状态
	 *
	 * @return 锁状态
	 */
	public boolean isLocked() {
		return rLock.isLocked();
	}


	private RLock loadRLock(LockContext lockContext) {
		List<LockCreator> creators = AppContextHolder.getBeanList(LockCreator.class);
		for (LockCreator creator : creators) {
			if (creator.supported(lockContext.getLockModel())) {
				return creator.get(lockContext.getKeys());
			}
		}
		throw new LockException("[Rlock] un-support lock model:" + lockContext.getLockModel());
	}


}
