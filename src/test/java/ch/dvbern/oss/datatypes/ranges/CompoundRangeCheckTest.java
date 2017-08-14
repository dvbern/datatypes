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

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompoundRangeCheckTest {

	private static final MinMaxRangeCheck FIRST = new MinMaxRangeCheck(0L, 100L);
	private static final MinMaxRangeCheck SECOND = new MinMaxRangeCheck(200L, 300L);
	private static final CompoundRangeCheck CHECK = new CompoundRangeCheck(FIRST, SECOND);

	@Test
	public void testValid() {
		// first range
		assertTrue(CHECK.checkRange(0L));
		assertTrue(CHECK.checkRange(1L));
		assertTrue(CHECK.checkRange(99L));
		assertTrue(CHECK.checkRange(100L));
		// second range
		assertTrue(CHECK.checkRange(200L));
		assertTrue(CHECK.checkRange(250L));
		assertTrue(CHECK.checkRange(300L));
	}

	@Test
	public void testFail() {
		assertFalse(CHECK.checkRange(-1L)); // too low
		assertFalse(CHECK.checkRange(150L)); // in between
		assertFalse(CHECK.checkRange(301L)); // too high
	}
}
