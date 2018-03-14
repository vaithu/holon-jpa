/*
 * Copyright 2016-2017 Axioma srl.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.holonplatform.jpa.internal.processors;

import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.jpa.internal.JpaAPIUtils;
import com.holonplatform.jpa.processors.JpaBeanPostProcessor;

/**
 * Base JPA {@link BeanPropertyPostProcessor} which checks for JPA API availability before processing.
 *
 * @since 5.1.0
 */
public abstract class AbstractJpaBeanPropertyPostProcessor implements BeanPropertyPostProcessor, JpaBeanPostProcessor {

	/**
	 * Logger
	 */
	protected static final Logger LOGGER = JpaProcessorsLogger.create();

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.core.beans.BeanPropertyPostProcessor#processBeanProperty(com.holonplatform.core.beans.
	 * BeanProperty.Builder, java.lang.Class)
	 */
	@Override
	public Builder<?> processBeanProperty(Builder<?> property, Class<?> beanOrNestedClass) {
		if (JpaAPIUtils.isJpaApiPresent((beanOrNestedClass != null) ? beanOrNestedClass.getClassLoader()
				: ClassUtils.getDefaultClassLoader())) {
			return processJpaBeanProperty(property, beanOrNestedClass);
		}
		LOGGER.debug(() -> "JPA API not detected in classpath: skip bean property processing by class ["
				+ getClass().getName() + "]");
		return property;
	}

	/**
	 * Process given bean <code>property</code>.
	 * @param property The property to process
	 * @param beanOrNestedClass Bean class
	 * @return Processed bean property
	 */
	protected abstract Builder<?> processJpaBeanProperty(Builder<?> property, Class<?> beanOrNestedClass);

}
