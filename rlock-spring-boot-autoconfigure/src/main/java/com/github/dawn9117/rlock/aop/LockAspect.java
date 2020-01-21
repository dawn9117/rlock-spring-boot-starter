package com.github.dawn9117.rlock.aop;

import com.github.dawn9117.rlock.annotation.Rlock;
import com.github.dawn9117.rlock.common.enums.LockModel;
import com.github.dawn9117.rlock.common.util.AppContextHolder;
import com.github.dawn9117.rlock.common.util.JoinPointUtils;
import com.github.dawn9117.rlock.config.RedissonProperties;
import com.github.dawn9117.rlock.config.RlockProperties;
import com.github.dawn9117.rlock.core.creator.LockCreator;
import com.github.dawn9117.rlock.core.lock.LockContext;
import com.github.dawn9117.rlock.core.lock.name.LockNameBuilder;
import com.github.dawn9117.rlock.core.lock.name.LockNameContext;
import com.github.dawn9117.rlock.excepiton.LockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 分布式锁aop
 */
@Aspect
@Slf4j
@AllArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class LockAspect {

	private RedissonProperties redissonProperties;

	private RlockProperties rlockProperties;

	@Pointcut("@annotation(com.github.dawn9117.rlock.annotation.Rlock))")
	public void lockPointcut() {
	}

	@Around("lockPointcut()")
	public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
		// 直接从方法上获取注解对象, spring相关的注解失效, 比如@AlisFor
		Rlock rlock = AnnotationUtils.findAnnotation(JoinPointUtils.getMethod(point), Rlock.class);
		LockContext lockContext = loadLockContext(point, rlock);
		RLock rLock = getRLock(lockContext);

		log.info("[Rlock] prepare lock, lockName:[{}], lockContext:[{}]", rLock.getName(), lockContext);
		try {
			doLock(lockContext, rLock);
			if (!rLock.isLocked()) {
				throw new LockException("[Rlock] lock error, lock failed, lockContext:" + lockContext);
			}

			log.info("[Rlock] locked, lockName:[{}]", rLock.getName());
			return point.proceed();
		} finally {
			if (rLock.isLocked()) {
				rLock.unlock();
				log.info("[Rlock] lock released, lockName:[{}]", rLock.getName());
			}
		}
	}

	private boolean doLock(LockContext lockContext, RLock rLock) throws InterruptedException {
		if (lockContext.getWaitTime() <= 0) {
			//一直等待加锁
			rLock.lock(lockContext.getLeaseTime(), TimeUnit.MILLISECONDS);
			return true;
		} else {
			return rLock.tryLock(lockContext.getWaitTime(), lockContext.getLeaseTime(), TimeUnit.MILLISECONDS);
		}
	}

	private RLock getRLock(LockContext lockContext) {
		List<LockCreator> creators = AppContextHolder.getBeanList(LockCreator.class);
		for (LockCreator creator : creators) {
			if (creator.supported(lockContext.getLockModel())) {
				return creator.get(lockContext.getKeys());
			}
		}
		throw new LockException("[Rlock] un-support lock model:" + lockContext.getLockModel());
	}

	private LockContext loadLockContext(ProceedingJoinPoint point, Rlock rlock) throws Throwable {
		LockModel lockModel = getLockModel(rlock);
		if (!lockModel.equals(LockModel.MULTIPLE) && !lockModel.equals(LockModel.RED_LOCK) && rlock.keys().length > 1) {
			throw new RuntimeException("[RLock] 加锁失败, 参数有多个, 锁模式为->" + lockModel.name() + ", key只能有一个");
		}

		return LockContext.builder()
				.waitTime(rlock.waitTime())
				.leaseTime(rlock.leaseTime())
				.keys(getKeys(point, rlock))
				.lockModel(lockModel)
				.build();
	}

	private String[] getKeys(ProceedingJoinPoint point, Rlock rlock) throws Throwable {
		List<LockNameBuilder> builders = AppContextHolder.getBeanList(LockNameBuilder.class);
		for (LockNameBuilder builder : builders) {
			if (builder.getClass().equals(rlock.lockNameBuilder())) {
				return builder.build(LockNameContext.builder()
						.config(rlockProperties)
						.method(JoinPointUtils.getMethod(point))
						.params(point.getArgs())
						.keys(rlock.keys())
						.build());
			}
		}
		return null;
	}

	/**
	 * 优先使用方法上指定的lockModel, 如果是AUTO则使用全局的, 如果全局的也是AUTO, 则根据参数长度自动选择
	 *
	 * @param rlock 注解对象
	 * @return LockModel
	 */
	private LockModel getLockModel(Rlock rlock) {
		if (!LockModel.AUTO.equals(rlock.lockModel())) {
			return rlock.lockModel();
		}

		if (redissonProperties.getLockModel() != null && !LockModel.AUTO.equals(redissonProperties.getLockModel())) {
			return redissonProperties.getLockModel();
		}

		// 未配置则根据参数长度自动选择
		return rlock.keys().length > 1 ? LockModel.MULTIPLE : LockModel.REENTRANT;
	}
}
