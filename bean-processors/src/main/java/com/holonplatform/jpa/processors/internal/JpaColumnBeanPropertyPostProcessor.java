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

import java.util.Optional;

import jakarta.annotation.Priority;
import jakarta.persistence.Column;

import com.holonplatform.core.DataMappable;
import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;

/**
 * A {@link BeanPropertyPostProcessor} to setup property configuration {@link DataMappable#PATH} property using JPA
 * <code>Column</code> annotation.
 *
 * @since 5.1.0
 */
@Priority(1500)
public class JpaColumnBeanPropertyPostProcessor extends AbstractJpaBeanPropertyPostProcessor {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jpa.internal.processors.AbstractJpaBeanPropertyPostProcessor#processJpaBeanProperty(com.
	 * holonplatform.core.beans.BeanProperty.Builder, java.lang.Class)
	 */
	@Override
	protected Builder<?> processJpaBeanProperty(Builder<?> property, Class<?> beanOrNestedClass) {
		if (!property.getConfiguration().getParameter(DataMappable.PATH).isPresent()) {
			final Optional<Column> methodColumn = property.getReadMethod().map(m -> m.getAnnotation(Column.class))
					.filter(a -> a.name().trim().length() > 0);
			if (methodColumn.isPresent()) {
				property.withConfiguration(DataMappable.PATH, methodColumn.get().name());
				LOGGER.debug(() -> "JpaColumnBeanPropertyPostProcessor: setted property [" + property
						+ "] configuration data path to [" + methodColumn.get().name() + "]");
			} else {
				property.getAnnotation(Column.class).map(a -> a.name()).filter(n -> n.trim().length() > 0)
						.ifPresent(n -> {
							property.withConfiguration(DataMappable.PATH, n);
							LOGGER.debug(() -> "JpaColumnBeanPropertyPostProcessor: setted property [" + property
									+ "] configuration data path [" + DataMappable.PATH.getKey() + "] to [" + n + "]");
						});
			}
		} else {
			LOGGER.debug(() -> "JpaColumnBeanPropertyPostProcessor: property [" + property
					+ "] data path is already configured ["
					+ property.getConfiguration().getParameter(DataMappable.PATH).get()
					+ "]: skip JPA Column annotation processing");
		}
		return property;
	}

}
