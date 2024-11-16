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
package com.holonplatform.jpa.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.jpa.test.data.TestJpaEntity;

class TestBeanPropertySetPostProcessors {

	@Test
	void testTablePostProcessor() {

		final BeanPropertySet<TestJpaEntity> propertySet = BeanIntrospector.get().getPropertySet(TestJpaEntity.class);

		assertTrue(propertySet.getDataPath().isPresent());
		assertEquals("test_table", propertySet.getDataPath().orElse(null));

	}

}
