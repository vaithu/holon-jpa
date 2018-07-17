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
package com.holonplatform.jpa.processors;

import java.util.ServiceLoader;

import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertyPostProcessor;
import com.holonplatform.core.beans.BeanPropertySetPostProcessor;
import com.holonplatform.core.internal.utils.ObjectUtils;
import com.holonplatform.jpa.processors.internal.JpaColumnBeanPropertyPostProcessor;
import com.holonplatform.jpa.processors.internal.JpaEnumeratedBeanPropertyPostProcessor;
import com.holonplatform.jpa.processors.internal.JpaIdentifierBeanPropertyPostProcessor;
import com.holonplatform.jpa.processors.internal.JpaTableBeanPropertySetPostProcessor;
import com.holonplatform.jpa.processors.internal.JpaTemporalBeanPropertyPostProcessor;
import com.holonplatform.jpa.processors.internal.JpaTransientBeanPropertyPostProcessor;

/**
 * Marker interface for JPA {@link BeanPropertyPostProcessor} and {@link BeanPropertySetPostProcessor}.
 *
 * @since 5.1.0
 */
public interface JpaBeanPostProcessor {

	/**
	 * Add all available {@link BeanPropertyPostProcessor}s and {@link BeanPropertySetPostProcessor}s to given
	 * {@link BeanIntrospector}.
	 * <p>
	 * Normally, it is not necessary to use this method, since bean post processors are automatically detected and
	 * registered in default {@link BeanIntrospector} implementations using default Java extensions, relying on the
	 * {@link ServiceLoader} strategy.
	 * </p>
	 * @param beanIntrospector The bean introspector (not null)
	 */
	static void registerPostProcessors(BeanIntrospector beanIntrospector) {
		ObjectUtils.argumentNotNull(beanIntrospector, "BeanIntrospector must not be null");

		beanIntrospector.addBeanPropertySetPostProcessor(new JpaTableBeanPropertySetPostProcessor());

		beanIntrospector.addBeanPropertyPostProcessor(new JpaIdentifierBeanPropertyPostProcessor());
		beanIntrospector.addBeanPropertyPostProcessor(new JpaEnumeratedBeanPropertyPostProcessor());
		beanIntrospector.addBeanPropertyPostProcessor(new JpaTemporalBeanPropertyPostProcessor());
		beanIntrospector.addBeanPropertyPostProcessor(new JpaColumnBeanPropertyPostProcessor());
		beanIntrospector.addBeanPropertyPostProcessor(new JpaTransientBeanPropertyPostProcessor());
	}

}
