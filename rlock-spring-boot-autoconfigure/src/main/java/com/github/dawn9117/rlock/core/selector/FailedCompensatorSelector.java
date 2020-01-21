package com.github.dawn9117.rlock.core.selector;

import com.github.dawn9117.rlock.core.compensator.DirectFailedCompensator;
import com.github.dawn9117.rlock.core.compensator.IgnoreLockCompensator;
import com.github.dawn9117.rlock.core.compensator.RetryLockCompensator;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 失败补偿器注册
 *
 * @author HEBO
 */
public class FailedCompensatorSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{DirectFailedCompensator.class.getName(), IgnoreLockCompensator.class.getName(), RetryLockCompensator.class.getName()};
	}
}
