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

import jakarta.annotation.Priority;
import jakarta.persistence.Table;

import com.holonplatform.core.beans.BeanPropertySet.Builder;
import com.holonplatform.core.DataMappable;
import com.holonplatform.core.beans.BeanPropertySetPostProcessor;
import com.holonplatform.core.internal.utils.AnnotationUtils;

/**
 * A {@link BeanPropertySetPostProcessor} to setup property set configuration {@link DataMappable#PATH} property using
 * JPA <code>Table</code> annotation.
 *
 * @since 5.1.0
 */
@Priority(2000)
public class JpaTableBeanPropertySetPostProcessor extends AbstractJpaBeanPropertySetPostProcessor {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.holonplatform.jpa.internal.processors.AbstractJpaBeanPropertySetPostProcessor#processJpaBeanPropertySet(com.
	 * holonplatform.core.beans.BeanPropertySet.Builder, java.lang.Class)
	 */
	@Override
	protected void processJpaBeanPropertySet(Builder<?, ?> propertySet, Class<?> beanClass) {
		if (!propertySet.getConfiguration().getParameter(DataMappable.PATH).isPresent()) {
			if (beanClass.isAnnotationPresent(Table.class)) {
				final String name = AnnotationUtils.getStringValue(beanClass.getAnnotation(Table.class).name());
				if (name != null && name.trim().length() > 0) {
					propertySet.configuration(DataMappable.PATH, name);
					LOGGER.debug(() -> "JpaTableBeanPropertySetPostProcessor: setted bean [" + beanClass
							+ "] property set data path to [" + name + "]");
				}
			}
		} else {
			LOGGER.debug(() -> "JpaTableBeanPropertySetPostProcessor: Bean class [" + beanClass
					+ "] property set data path is already configured ["
					+ propertySet.getConfiguration().getParameter(DataMappable.PATH).get()
					+ "]: skip JPA Table annotation processing");
		}
	}

}
