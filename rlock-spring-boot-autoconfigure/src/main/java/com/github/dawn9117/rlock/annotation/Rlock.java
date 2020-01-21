package com.github.dawn9117.rlock.annotation;

import com.github.dawn9117.rlock.common.enums.LockModel;
import com.github.dawn9117.rlock.core.compensator.LockFailedCompensator;
import com.github.dawn9117.rlock.core.compensator.RetryLockCompensator;
import com.github.dawn9117.rlock.core.lock.name.DefaultNameBuilder;
import com.github.dawn9117.rlock.core.lock.name.LockNameBuilder;
import org.redisson.RedissonLock;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Rlock {

	/**
	 * 如果keys有多个,如果不设置,则使用 联锁
	 *
	 * @return 锁
	 */
	@AliasFor("keys")
	String[] value() default {};

	/**
	 * 锁的模式:如果不设置,默认自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE
	 */
	LockModel lockModel() default LockModel.AUTO;

	/**
	 * 配合LockModel使用
	 *
	 * @return 锁keys
	 * @see Rlock#lockModel
	 */
	@AliasFor("value")
	String[] keys() default {};

	/**
	 * 锁持有时间, 默认-1: 看门狗自动续期, 否则锁到期自动失效
	 * <p>
	 * leaseTime == -1, 设置锁有效期为{@RedissonProperties#lockWatchdogTimeout}
	 * 每隔{@RedissonProperties#lockWatchdogTimeout}的三分之一时间重置锁的有效时间
	 * <p>
	 * 具体实现在 RedissonLock#lock(long, TimeUnit)
	 * private <T> RFuture<Long> tryAcquireAsync(long leaseTime, TimeUnit unit, long threadId)
	 *
	 * @return 锁持有时间
	 * @see RedissonLock#lock(long, TimeUnit)
	 */
	long leaseTime() default -1;

	/**
	 * 等待加锁超时时间, 默认-1, 一直等待
	 *
	 * @return 等待加锁超时时间
	 */
	long waitTime() default -1;

	/**
	 * 锁名称构建器
	 *
	 * @return LockNameBuilder
	 */
	Class<? extends LockNameBuilder> lockNameBuilder() default DefaultNameBuilder.class;

	/**
	 * // TODO 尚未实现, 失败补偿策略
	 *
	 * @return 补偿策略
	 */
	Class<? extends LockFailedCompensator> compensator() default RetryLockCompensator.class;


}
