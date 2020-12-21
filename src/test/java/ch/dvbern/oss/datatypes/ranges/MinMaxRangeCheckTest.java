/*
 * Copyright 2017 DV Bern AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
package ch.dvbern.oss.datatypes.ranges;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinMaxRangeCheckTest {

	private static final MinMaxRangeCheck CHECK = new MinMaxRangeCheck(0L, 100L);

	@Test
	public void testValid() {
		assertTrue(CHECK.checkRange(0L));
		assertTrue(CHECK.checkRange(1L));
		assertTrue(CHECK.checkRange(99L));
		assertTrue(CHECK.checkRange(100L));
	}

	@Test
	public void testFail() {
		assertFalse(CHECK.checkRange(-1L));
		assertFalse(CHECK.checkRange(101L));
	}

	@Test
	public void testNoRangeNo() {
		MinMaxRangeCheck noArgCheck = new MinMaxRangeCheck();

		assertTrue(noArgCheck.checkRange(0L));
		assertTrue(noArgCheck.checkRange(-1L));
		assertTrue(noArgCheck.checkRange(100L));
		assertTrue(noArgCheck.checkRange(101L));
	}
}
