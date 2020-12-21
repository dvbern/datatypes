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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for IBAN
 */
public class IBANTest {

	private static final String IBAN_1 = "CH63 0900 0000 2500 9779 8";
	private static final String IBAN_2 = "CH95 0900 0000 6076 1739 7";

	private static final String IBAN_1_UNFORMATTED = IBAN_1.replaceAll(" ", "");
	private static final String IBAN_2_UNFORMATTED = IBAN_2.replaceAll(" ", "");

	private static final String CLEARING = "09000";

	@Test
	public void testEquals() {
		assertEquals(new IBAN(IBAN_1), new IBAN(IBAN_1));
		assertEquals(new IBAN(IBAN_1), new IBAN(IBAN_1_UNFORMATTED));

		assertNotEquals(new IBAN(IBAN_2), new IBAN(IBAN_1));
		//noinspection ObjectEqualsNull
		assertNotEquals(new IBAN(IBAN_1), null);
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, new IBAN("CH63 0900 0000 2500 9779 8").compareTo(new IBAN("CH63 0900 0000 2500 9779 8")));
		assertEquals(-1, new IBAN("CH63 0900 0000 2500 9779 8").compareTo(new IBAN("CH63 0900 0000 2500 9779 9")));
		assertEquals(1, new IBAN("CH63 0900 0000 2500 9779 8").compareTo(new IBAN("CH63 0900 0000 2500 9779 7")));
	}

	@Test
	public void testToString() {
		assertEquals(IBAN_1, new IBAN(IBAN_1).toString());
		assertEquals(IBAN_1, new IBAN(IBAN_1_UNFORMATTED).toString());

		assertEquals("CH", new IBAN("CH").toString());
		assertEquals("CH63", new IBAN("CH63").toString());
		assertEquals("1234 5678 9", new IBAN("123456789").toString());
		assertEquals("1234 5678 9012 3456 789", new IBAN("123456789    0123456789                    ").toString());
		assertEquals("1234 5678 9012 3456 7890", new IBAN("12345678901234567890").toString());
		assertEquals("1234 5678 9012 3456 7890 1", new IBAN("123456789012345678901").toString());
	}

	@Test
	public void testIsValid() {
		assertTrue(new IBAN(IBAN_1).isValid());
		assertTrue(new IBAN(IBAN_2).isValid());
		assertTrue(new IBAN(IBAN_1_UNFORMATTED).isValid());
		assertTrue(new IBAN(IBAN_2_UNFORMATTED).isValid());

		assertFalse(new IBAN("AnyString").isValid());
		assertFalse(new IBAN("XY123456").isValid());

		assertFalse(new IBAN().isValid());
	}

	@Test
	public void testExtractClearingNumber() {
		assertEquals(CLEARING, new IBAN(IBAN_1).extractClearingNr());
		assertEquals(CLEARING, new IBAN(IBAN_2).extractClearingNr());
		assertEquals(CLEARING, new IBAN(IBAN_1_UNFORMATTED).extractClearingNr());
		assertEquals(CLEARING, new IBAN(IBAN_2_UNFORMATTED).extractClearingNr());
	}

	@Test
	public void testExtractClearingNumberValid() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new IBAN("InvalidNumber").extractClearingNr()
		);

		assertThat(ex)
			.hasMessageContaining("InvalidNumber");
	}
}
