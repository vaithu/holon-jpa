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
package com.holonplatform.jpa.processors.internal;

import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.beans.BeanPropertySetPostProcessor;
import com.holonplatform.core.internal.Logger;
import com.holonplatform.core.internal.utils.ClassUtils;
import com.holonplatform.jpa.processors.JpaBeanPostProcessor;
import com.holonplatform.jpa.processors.internal.utils.JpaAPIUtils;

/**
 * Base JPA {@link BeanPropertySetPostProcessor} which checks for JPA API availability before processing.
 *
 * @since 5.1.0
 */
public abstract class AbstractJpaBeanPropertySetPostProcessor implements BeanPropertySetPostProcessor, JpaBeanPostProcessor {

	/**
	 * Logger
	 */
	protected static final Logger LOGGER = JpaProcessorsLogger.create();

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.core.beans.BeanPropertySetPostProcessor#processBeanPropertySet(com.holonplatform.core.beans.
	 * BeanPropertySet.Builder, java.lang.Class)
	 */
	@Override
	public void processBeanPropertySet(BeanPropertySet.Builder<?, ?> propertySet, Class<?> beanClass) {
		if (JpaAPIUtils.isJpaApiPresent(
				(beanClass != null) ? beanClass.getClassLoader() : ClassUtils.getDefaultClassLoader())) {
			processJpaBeanPropertySet(propertySet, beanClass);
		} else {
			LOGGER.debug(() -> "JPA API not detected in classpath: skip bean property set processing by class ["
					+ getClass().getName() + "]");
		}
	}

	/**
	 * Process given bean <code>propertySet</code>.
	 * @param propertySet The bean property set to process
	 * @param beanClass Bean class
	 */
	protected abstract void processJpaBeanPropertySet(BeanPropertySet.Builder<?, ?> propertySet, Class<?> beanClass);

}
