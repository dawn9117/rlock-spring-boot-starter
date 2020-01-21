package com.github.dawn9117.rlock.common.util;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author HEBO
 */
@UtilityClass
public class JoinPointUtils {

	public Method getMethod(JoinPoint point) throws NoSuchMethodException {
		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		return point.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
	}

}
