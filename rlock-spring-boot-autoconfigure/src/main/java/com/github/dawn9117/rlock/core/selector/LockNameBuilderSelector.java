package com.github.dawn9117.rlock.core.selector;

import com.github.dawn9117.rlock.core.lock.name.DefaultNameBuilder;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 失败补偿器注册
 *
 * @author HEBO
 */
public class LockNameBuilderSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{DefaultNameBuilder.class.getName()};
	}
}
