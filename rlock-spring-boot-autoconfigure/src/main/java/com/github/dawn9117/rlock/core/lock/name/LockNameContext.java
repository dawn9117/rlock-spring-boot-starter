package com.github.dawn9117.rlock.core.lock.name;

import com.github.dawn9117.rlock.common.constant.RlockConstant;
import com.github.dawn9117.rlock.common.util.ExpressionUtils;
import com.github.dawn9117.rlock.common.util.VariableUtils;
import com.github.dawn9117.rlock.config.RlockProperties;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 锁名称相关上下文
 *
 * @author HEBO
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LockNameContext {

	/**
	 * 指定的key
	 * 例:  my:lock
	 */
	private List<String> specialKeys;

	/**
	 * redis lock前缀
	 *
	 * @see RlockProperties#getPrefix()
	 */
	private String prefix;

	/**
	 * 项目名
	 *
	 * @see RlockProperties#getProject()
	 */
	private String projectName;

	/**
	 * 类全路径
	 * 例: com.github.dawn9117.service.UserService
	 */
	private String fullClassName;

	/**
	 * 类名
	 * 例: UserService
	 */
	private String simpleClassName;

	/**
	 * 方法名
	 * 例: addUser
	 */
	private String methodName;

	/**
	 * expression解析的值
	 * 例: li san
	 */
	private List<String> expressionKeys;

	public static Builder builder() {
		return new Builder();
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Builder {
		private Method method;

		private Object[] params;

		private RlockProperties config;

		private String[] keys;

		public Builder config(RlockProperties config) {
			this.config = config;
			return this;
		}

		public Builder method(Method method) {
			this.method = method;
			return this;
		}

		public Builder params(Object[] params) {
			this.params = params;
			return this;
		}

		public Builder keys(String[] keys) {
			this.keys = keys;
			return this;
		}


		public LockNameContext build() {
			LockNameContext context = new LockNameContext();
			context.setPrefix(config.getPrefix());
			context.setFullClassName(method.getDeclaringClass().getName());
			context.setSimpleClassName(method.getDeclaringClass().getSimpleName());
			context.setMethodName(method.getName());
			context.setProjectName(config.getProject());
			for (String key : keys) {
				if (StringUtils.startsWith(key, RlockConstant.EL_PREFIX)) {
					if (context.getExpressionKeys() == null) {
						context.setExpressionKeys(Lists.newArrayList());
					}
					String expressionValue = ExpressionUtils.parseSpel(key, VariableUtils.getParameterNames(method), params, String.class);
					context.getExpressionKeys().add(expressionValue);
				} else {
					if (context.getSpecialKeys() == null) {
						context.setExpressionKeys(Lists.newArrayList());
					}
					context.getSpecialKeys().add(key);
				}
			}
			return context;
		}
	}
}
