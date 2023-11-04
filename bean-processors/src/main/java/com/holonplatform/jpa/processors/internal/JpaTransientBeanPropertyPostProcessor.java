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
import jakarta.persistence.Transient;

import com.holonplatform.core.beans.BeanProperty.Builder;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.IgnoreMode;

/**
 * A {@link BeanPropertyPostProcessor} to set a bean property as ignored using the JPA <code>Transient</code>
 * annotation.
 *
 * @since 5.1.0
 */
@Priority(2000)
public class JpaTransientBeanPropertyPostProcessor extends AbstractJpaBeanPropertyPostProcessor {

	/*
	 * (non-Javadoc)
	 * @see com.holonplatform.jpa.internal.processors.AbstractJpaBeanPropertyPostProcessor#processJpaBeanProperty(com.
	 * holonplatform.core.beans.BeanProperty.Builder, java.lang.Class)
	 */
	@Override
	protected Builder<?> processJpaBeanProperty(Builder<?> property, Class<?> beanOrNestedClass) {
		// check not already ignored
		if (property.getIgnoreMode().orElse(IgnoreMode.DO_NOT_IGNORE) == IgnoreMode.DO_NOT_IGNORE) {
			property.getAnnotation(Transient.class).ifPresent(a -> {
				property.ignoreMode(IgnoreMode.IGNORE_INCLUDE_NESTED);
				LOGGER.debug(
						() -> "JpaTransientBeanPropertyPostProcessor: property [" + property + "] setted as ignored");
			});
		}
		return property;
	}

}
