package com.github.dawn9117.rlock.core.lock.name;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author HEBO
 */
public class DefaultNameBuilder implements LockNameBuilder {

	@Override
	public String[] build(LockNameContext context) throws Throwable {
		List<String> keys = Lists.newArrayList();

		// 指定key
		if (CollectionUtils.isNotEmpty(context.getSpecialKeys())) {
			context.getSpecialKeys().forEach(key -> keys.add(getKeyJoiner(context, key).toString()));
		}

		// spel表达式解析
		if (CollectionUtils.isNotEmpty(context.getExpressionKeys())) {
			context.getExpressionKeys().forEach(key -> keys.add(getKeyJoiner(context, key).toString()));
		}

		// 如果没有配置 keys, 默认用 (全局前缀:项目名:类名:方法名)
		if (CollectionUtils.isEmpty(keys)) {
			keys.add(getKeyJoiner(context).toString());
		}

		return keys.toArray(new String[0]);
	}

	private StringJoiner getKeyJoiner(LockNameContext context) {
		StringJoiner joiner = new StringJoiner(DELIMITER);
		joiner.add(context.getPrefix());
		joiner.add(context.getProjectName());
		joiner.add(context.getFullClassName());
		joiner.add(context.getMethodName());
		return joiner;
	}

	private StringJoiner getKeyJoiner(LockNameContext context, String key) {
		StringJoiner joiner = getKeyJoiner(context);
		joiner.add(key);
		return joiner;
	}


}
