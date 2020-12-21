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

package ch.dvbern.oss.datatypes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostkontoNummerTest {

	@Test
	public void doTest() {

		PostkontoNummer pcValid = new PostkontoNummer(301045968);
		assertTrue(pcValid.isValid());
		assertEquals("30-104596-8", pcValid.toString());
		assertEquals(pcValid, new PostkontoNummer("301045968"));
		assertEquals(pcValid, new PostkontoNummer(pcValid.toString()));

		// Auch 8-stellige PC-Kontonummern sind moeglich!
		assertTrue(new PostkontoNummer("10-15000-6").isValid());

		assertTrue(new PostkontoNummer("10150006").isValid());

		assertTrue(new PostkontoNummer(10150006).isValid());

		assertFalse(new PostkontoNummer(301045978).isValid());

	}

	@Test
	public void testOrder() {

		PostkontoNummer n1 = new PostkontoNummer(301045968);
		PostkontoNummer n2 = new PostkontoNummer(301035968);
		PostkontoNummer n3 = new PostkontoNummer(301045968);
		assertEquals(0, n1.compareTo(n3));
		assertEquals(-1, n1.compareTo(n2));
		assertEquals(1, n2.compareTo(n1));
	}
}
