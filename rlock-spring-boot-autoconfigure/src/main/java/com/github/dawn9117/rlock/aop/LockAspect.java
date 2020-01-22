package com.github.dawn9117.rlock.aop;

import com.github.dawn9117.rlock.annotation.Rlock;
import com.github.dawn9117.rlock.common.enums.LockModel;
import com.github.dawn9117.rlock.common.util.AppContextHolder;
import com.github.dawn9117.rlock.common.util.JoinPointUtils;
import com.github.dawn9117.rlock.config.RlockProperties;
import com.github.dawn9117.rlock.core.lock.LockContext;
import com.github.dawn9117.rlock.core.lock.LockInvoker;
import com.github.dawn9117.rlock.core.lock.name.LockNameBuilder;
import com.github.dawn9117.rlock.core.lock.name.LockNameContext;
import com.github.dawn9117.rlock.excepiton.LockException;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;


/**
 * redis分布式锁aop
 */
@Aspect
@AllArgsConstructor
public class LockAspect {

	private RlockProperties rlockProperties;

	@Pointcut("@annotation(com.github.dawn9117.rlock.annotation.Rlock))")
	public void lockPointcut() {
	}

	@Around("lockPointcut()")
	public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
		// 直接从方法上获取注解对象, spring相关的注解失效, 比如@AlisFor
		Rlock rlock = AnnotationUtils.findAnnotation(JoinPointUtils.getMethod(point), Rlock.class);
		LockInvoker invoker = getLockInvoker(point, rlock);
		try {
			invoker.lock();
			return point.proceed();
		} finally {
			invoker.unlock();
		}
	}


	private LockInvoker getLockInvoker(ProceedingJoinPoint point, Rlock rlock) throws Throwable {
		LockModel lockModel = getLockModel(rlock);
		if (!lockModel.equals(LockModel.MULTIPLE) && !lockModel.equals(LockModel.RED_LOCK) && rlock.keys().length > 1) {
			throw new RuntimeException("[RLock] 加锁失败, 参数有多个, 锁模式为->" + lockModel.name() + ", key只能有一个");
		}

		return new LockInvoker(LockContext.builder()
				.waitTime(rlock.waitTime())
				.leaseTime(rlock.leaseTime())
				.keys(getKeys(point, rlock))
				.lockModel(lockModel)
				.build());
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
		throw new LockException("[Rlock] un-support LockNameBuilder:" + rlock.lockNameBuilder().getName());
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

		if (rlockProperties.getLockModel() != null && !LockModel.AUTO.equals(rlockProperties.getLockModel())) {
			return rlockProperties.getLockModel();
		}

		// 未配置则根据参数长度自动选择
		return rlock.keys().length > 1 ? LockModel.MULTIPLE : LockModel.REENTRANT;
	}
}
