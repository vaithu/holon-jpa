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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.property.PropertyValueConverter;

/**
 * A {@link BeanPropertyPostProcessor} to setup a suitable enum {@link PropertyValueConverter} when the JPA
 * <code>Enumerated</code> annotation is detected on a bean property.
 * <p>
 * Property enum converter is setted up only if the bean property has not a {@link PropertyValueConverter} configured
 * yet.
 * </p>
 *
 * @since 5.0.0
 */
@Priority(1100)
public class JpaEnumeratedBeanPropertyPostProcessor extends AbstractJpaBeanPropertyPostProcessor {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jpa.internal.processors.AbstractJpaBeanPropertyPostProcessor#processJpaBeanProperty(com.
	 * holonplatform.core.beans.BeanProperty.Builder, java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Builder<?> processJpaBeanProperty(Builder<?> property, Class<?> beanOrNestedClass) {
		if (!property.getConverter().isPresent()) {
			property.getAnnotation(Enumerated.class).ifPresent(a -> {
				final EnumType enumType = a.value();
				if (enumType == EnumType.STRING) {
					((Builder) property).converter(PropertyValueConverter.enumByName());
				} else {
					((Builder) property).converter(PropertyValueConverter.enumByOrdinal());
				}
				LOGGER.debug(() -> "JpaEnumeratedBeanPropertyPostProcessor: setted property [" + property
						+ "] value converter to default enumeration converter using [" + enumType.name() + "] mode");
			});
		} else {
			LOGGER.debug(() -> "JpaEnumeratedBeanPropertyPostProcessor: property [" + property
					+ "] has a PropertyValueConverter [" + property.getConverter().get()
					+ "]: skip JPA Enumerated annotation processing");
		}
		return property;
	}

}
