package com.holonplatform.jpa.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.holonplatform.jpa.processors.internal.utils.JpaAPIUtils;

class TestJpaUtils {

	@Test
	void testJpaApiPresent() {
		assertTrue(JpaAPIUtils.isJpaApiPresent());
	}

}
