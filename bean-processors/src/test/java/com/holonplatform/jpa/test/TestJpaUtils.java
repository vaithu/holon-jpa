package com.holonplatform.jpa.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.holonplatform.jpa.processors.internal.utils.JpaAPIUtils;

public class TestJpaUtils {

	@Test
	public void testJpaApiPresent() {
		assertTrue(JpaAPIUtils.isJpaApiPresent());
	}

}
