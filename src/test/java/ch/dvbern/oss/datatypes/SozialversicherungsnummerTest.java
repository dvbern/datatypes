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

public class SozialversicherungsnummerTest {

	@Test
	public void testPruefziffer0() {

		// Bugzilla 5067: Nummern mit Prüfziffer 0 wurden als ungültig beurteilt.
		SozialversicherungsNummer sn = new SozialversicherungsNummer(7562844768650L);
		assertTrue(sn.isValid());
	}

	@Test
	public void testValidFromString() {

		SozialversicherungsNummer sn = new SozialversicherungsNummer("7569217076985");
		assertTrue(sn.isValid());
		SozialversicherungsNummer sn2 = new SozialversicherungsNummer("756.9217.0769.85");
		assertEquals(sn, sn2);
		assertTrue(sn2.isValid());
	}

	@Test
	public void testValidFromLong() {

		SozialversicherungsNummer sn = new SozialversicherungsNummer(7569217076985L);
		assertTrue(sn.isValid());
	}

	@Test
	public void testInvalid() {

		SozialversicherungsNummer sn = new SozialversicherungsNummer(7569227076983L);
		assertFalse(sn.isValid());
	}

	@Test
	public void testGetNumberString() {

		String nr = "1234567890123";
		SozialversicherungsNummer nummer = new SozialversicherungsNummer(nr);
		assertEquals(nr, nummer.getNummerAsString());
	}

	@Test
	public void testTooShort() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new SozialversicherungsNummer("1")
		);

		assertThat(ex)
			.hasMessageContaining(" 1 ");
	}

	@Test
	public void testTooLong() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new SozialversicherungsNummer("12345678901234")
		);

		assertThat(ex)
			.hasMessageContaining(" 12345678901234 ");
	}

	@Test
	public void testCompareTo() {

		SozialversicherungsNummer sn1 = new SozialversicherungsNummer(7569227076983L);
		//noinspection EqualsWithItself
		assertEquals(0, sn1.compareTo(sn1));
		SozialversicherungsNummer sn2 = new SozialversicherungsNummer(7569217076985L);
		assertEquals(-1, sn1.compareTo(sn2));
	}

	@Test
	public void testToString() {

		assertEquals("756.9217.0769.85", new SozialversicherungsNummer("7569217076985").toString());
	}

	@Test
	public void testPruefziffer() {

		assertEquals(7, new SozialversicherungsNummer(7561234567897L).getPruefziffer());
		assertEquals(2, new SozialversicherungsNummer(7562229390322L).getPruefziffer());
		assertEquals(5, new SozialversicherungsNummer(7569217076985L).getPruefziffer());
		assertEquals(0, new SozialversicherungsNummer(7562844768650L).getPruefziffer());
		assertEquals(7, new SozialversicherungsNummer(7561277671407L).getPruefziffer());
		assertEquals(1, new SozialversicherungsNummer(7567779844851L).getPruefziffer());
	}

}
