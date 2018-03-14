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
package com.holonplatform.jpa.test.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "test_table")
public class TestJpaEntity implements Serializable {

	private static final long serialVersionUID = 5633907095460335419L;

	@Id
	@Column(name = "code")
	private Long id;

	@Enumerated(EnumType.ORDINAL)
	private TestEntityEnum enumeration;

	@Enumerated(EnumType.STRING)
	private TestEntityEnum enumeration2;

	@Column(name = "the_date")
	@Temporal(TemporalType.DATE)
	private Date date;

	@com.holonplatform.core.beans.Temporal(com.holonplatform.core.temporal.TemporalType.DATE_TIME)
	@Temporal(TemporalType.DATE)
	private Date date2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TestEntityEnum getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(TestEntityEnum enumeration) {
		this.enumeration = enumeration;
	}

	public TestEntityEnum getEnumeration2() {
		return enumeration2;
	}

	public void setEnumeration2(TestEntityEnum enumeration2) {
		this.enumeration2 = enumeration2;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

}
