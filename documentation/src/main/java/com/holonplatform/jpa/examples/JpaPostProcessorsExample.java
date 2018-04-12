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
package com.holonplatform.jpa.examples;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import com.holonplatform.core.beans.BeanPropertySet;
import com.holonplatform.core.property.PathProperty;
import com.holonplatform.core.temporal.TemporalType;

@SuppressWarnings("unused")
public class JpaPostProcessorsExample {

	enum MyEnum {
	}

	// tag::postprocessors[]
	@Entity
	@Table(name = "mytable")
	public class MyEntity { // <1>

		@Id
		private Long id;

		@Column(name = "txt")
		private String textValue;

		@Enumerated(EnumType.ORDINAL)
		private MyEnum enumeration;

		@Temporal(javax.persistence.TemporalType.DATE)
		private Date date;

		@Transient
		private String toIgnore;

		// getters and setters omitted
	}

	void introspect() {

		BeanPropertySet<MyEntity> propertySet = BeanPropertySet.create(MyEntity.class); // <2>

		Set<PathProperty<?>> identifiers = propertySet.getIdentifiers(); // <3>

		Optional<String> dataPath = propertySet.property("textValue").getDataPath(); // <4>

		Optional<TemporalType> temporalType = propertySet.property("date").getTemporalType(); // <5>

		boolean ignored = propertySet.contains("toIgnore"); // <6>

	}
	// end::postprocessors[]

}
