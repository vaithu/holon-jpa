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
import jakarta.persistence.Temporal;

import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.temporal.TemporalType;

/**
 * A {@link BeanPropertyPostProcessor} to setup a the property {@link TemporalType} using the JPA <code>Temporal</code>
 * annotation.
 * <p>
 * Property configuration is processed only if the bean property has not a {@link TemporalType} configured yet.
 * </p>
 *
 * @since 5.0.0
 */
@Priority(1200)
public class JpaTemporalBeanPropertyPostProcessor extends AbstractJpaBeanPropertyPostProcessor {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jpa.internal.processors.AbstractJpaBeanPropertyPostProcessor#processJpaBeanProperty(com.
	 * holonplatform.core.beans.BeanProperty.Builder, java.lang.Class)
	 */
	@Override
	protected Builder<?> processJpaBeanProperty(Builder<?> property, Class<?> beanOrNestedClass) {
		if (!property.getConfiguration().getTemporalType().isPresent()) {
			property.getAnnotation(Temporal.class).ifPresent(a -> {
				property.temporalType(convert(a.value()));
				LOGGER.debug(() -> "JpaTemporalBeanPropertyPostProcessor: setted property [" + property
						+ "] temporalType to: [" + a.value() + "]");
			});
		} else {
			LOGGER.debug(() -> "JpaTemporalBeanPropertyPostProcessor: property [" + property
					+ "] temporal type is already configured [" + property.getTemporalType().get()
					+ "]: skip JPA Temporal annotation processing");
		}
		return property;
	}

	/**
	 * Convert a JPA {@link jakarta.persistence.TemporalType} enumeration value into a {@link TemporalType} value.
	 * @param temporalType JPA enumeration value
	 * @return {@link TemporalType} value
	 */
	private static TemporalType convert(jakarta.persistence.TemporalType temporalType) {
		if (temporalType != null) {
			switch (temporalType) {
			case DATE:
				return TemporalType.DATE;
			case TIME:
				return TemporalType.TIME;
			case TIMESTAMP:
				return TemporalType.DATE_TIME;
			default:
				break;
			}
		}
		return null;
	}

}
