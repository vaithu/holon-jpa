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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.holonplatform.core.beans.BeanIntrospector;
import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.temporal.TemporalType;
import com.holonplatform.jpa.test.data.TestEntityEnum;
import com.holonplatform.jpa.test.data.TestJpaEntity;
import com.holonplatform.jpa.test.data.TestJpaEntityEmbeddedId;

public class TestBeanPropertyPostProcessors {

	private final static BeanPropertySet<TestJpaEntity> PROPERTY_SET = BeanIntrospector.get()
			.getPropertySet(TestJpaEntity.class);

	@Test
	public void testColumPostProcessor() {

		PathProperty<?> prp = PROPERTY_SET.getProperty("id").orElse(null);
		assertNotNull(prp);
		assertTrue(prp.getDataPath().isPresent());
		assertEquals("code", prp.getDataPath().orElse(null));

		prp = PROPERTY_SET.getProperty("date").orElse(null);
		assertNotNull(prp);
		assertTrue(prp.getDataPath().isPresent());
		assertEquals("the_date", prp.getDataPath().orElse(null));

	}

	@Test
	public void testIdPostProcessor() {

		Set<PathProperty<?>> ids = PROPERTY_SET.getIdentifiers();
		assertFalse(ids.isEmpty());
		assertEquals(1, ids.size());
		assertEquals("id", ids.iterator().next().getName());

	}

	@Test
	public void testEmbeddedIdPostProcessor() {

		final BeanPropertySet<TestJpaEntityEmbeddedId> PS = BeanIntrospector.get()
				.getPropertySet(TestJpaEntityEmbeddedId.class);

		Set<PathProperty<?>> ids = PS.getIdentifiers();
		assertFalse(ids.isEmpty());
		assertEquals(1, ids.size());

		final PathProperty<?> code = ids.iterator().next();
		assertNotNull(code);

		assertEquals("code", code.getName());
		assertEquals("key", code.getDataPath().orElse(null));

	}

	@Test
	public void testEnumeratedPostProcessor() {

		PathProperty<TestEntityEnum> prp = PROPERTY_SET.getProperty("enumeration", TestEntityEnum.class).orElse(null);
		assertNotNull(prp);
		assertTrue(prp.getConverter().isPresent());
		assertEquals(1, prp.getConvertedValue(TestEntityEnum.SECOND));

		prp = PROPERTY_SET.getProperty("enumeration2", TestEntityEnum.class).orElse(null);
		assertNotNull(prp);
		assertTrue(prp.getConverter().isPresent());
		assertEquals("FIRST", prp.getConvertedValue(TestEntityEnum.FIRST));

	}

	@Test
	public void testTemporalPostProcessor() {

		PathProperty<?> prp = PROPERTY_SET.getProperty("date").orElse(null);
		assertNotNull(prp);
		assertTrue(prp.getConfiguration().getTemporalType().isPresent());
		assertEquals(TemporalType.DATE, prp.getConfiguration().getTemporalType().get());

		prp = PROPERTY_SET.getProperty("date2").orElse(null);
		assertNotNull(prp);
		assertTrue(prp.getConfiguration().getTemporalType().isPresent());
		assertEquals(TemporalType.DATE_TIME, prp.getConfiguration().getTemporalType().get());

	}

}
