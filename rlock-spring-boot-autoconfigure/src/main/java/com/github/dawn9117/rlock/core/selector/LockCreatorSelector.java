package com.github.dawn9117.rlock.core.selector;

import com.github.dawn9117.rlock.core.creator.*;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 锁创建器selector
 *
 * @author HEBO
 */
public class LockCreatorSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{
				FairLockCreator.class.getName(),
				MultipleLockCreator.class.getName(),
				ReadLockCreator.class.getName(),
				RedLockCreator.class.getName(),
				ReentrantLockCreator.class.getName(),
				WriteLockCreator.class.getName()
		};
	}

}
