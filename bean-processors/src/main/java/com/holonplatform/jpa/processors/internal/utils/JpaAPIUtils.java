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
package com.holonplatform.jpa.processors.internal.utils;

import java.io.Serializable;
import java.util.Map;
import java.util.WeakHashMap;

import com.holonplatform.core.internal.utils.ClassUtils;

/**
 * Utility class to check JPA API availability.
 * 
 * @since 5.1.0
 */
public final class JpaAPIUtils implements Serializable {

	private static final long serialVersionUID = -4394050943345038571L;

	/**
	 * JPA API presence in classpath for classloader
	 */
	private static final Map<ClassLoader, Boolean> JPA_API_PRESENT = new WeakHashMap<>();

	private JpaAPIUtils() {
	}

	/**
	 * Checks whether the JPA API is available from classpath using current {@link ClassLoader}.
	 * @return <code>true</code> if present
	 */
	public static boolean isJpaApiPresent() {
		return isJpaApiPresent(ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Checks whether the JPA API is available from classpath.
	 * @param classLoader ClassLoader to use
	 * @return <code>true</code> if present
	 */
	public static boolean isJpaApiPresent(ClassLoader classLoader) {
		if (JPA_API_PRESENT.containsKey(classLoader)) {
			Boolean present = JPA_API_PRESENT.get(classLoader);
			return (present != null && present.booleanValue());
		}
		boolean present = ClassUtils.isPresent("jakarta.persistence.Persistence", classLoader);
		JPA_API_PRESENT.put(classLoader, present);
		return present;
	}

}
