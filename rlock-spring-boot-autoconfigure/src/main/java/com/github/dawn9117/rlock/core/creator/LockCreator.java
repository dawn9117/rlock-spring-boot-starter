package com.github.dawn9117.rlock.core.creator;

import com.github.dawn9117.rlock.common.enums.LockModel;
import org.redisson.api.RLock;

/**
 * 锁构建器
 *
 * @author HEBO
 */
public interface LockCreator {

	/**
	 * 是否支持
	 *
	 * @param model 锁模式
	 * @return 是否支持
	 */
	default Boolean supported(LockModel model) {
		return false;
	}

	/**
	 * 获取锁
	 *
	 * @param key key
	 * @return 锁
	 */
	RLock get(String... key);

}
