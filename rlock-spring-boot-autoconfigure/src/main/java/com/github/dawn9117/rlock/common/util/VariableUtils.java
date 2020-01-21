package com.github.dawn9117.rlock.common.util;

import org.aspectj.lang.JoinPoint;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * 变量工具类
 *
 * @author HEBO
 */
public class VariableUtils {
	/**
	 * 本地变量名工具
	 */
	private static final LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 获取方法上的参数名称列表
	 *
	 * @param method 方法
	 * @return 参数名称列表
	 */
	public static String[] getParameterNames(Method method) {
		return discoverer.getParameterNames(method);
	}

	/**
	 * 获取AOP连接方法上的参数名称列表
	 *
	 * @param joinPoint 连接的方法
	 * @return 参数名称列表
	 */
	public static String[] getParameterNames(JoinPoint joinPoint) throws NoSuchMethodException {
		Method method = JoinPointUtils.getMethod(joinPoint);
		return discoverer.getParameterNames(method);
	}

}
