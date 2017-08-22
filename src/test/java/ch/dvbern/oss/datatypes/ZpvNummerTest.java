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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.mindprod.csv.CSVReader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author beph
 */
@SuppressWarnings("ResultOfObjectAllocationIgnored")
public class ZpvNummerTest {

	/**
	 *
	 */
	@Test
	public void doTest() {

		ZpvNummer zpvNr = new ZpvNummer(17742883L);
		assertTrue(zpvNr.isValid());
		zpvNr = new ZpvNummer("17742883");
		assertTrue(zpvNr.isValid());

		assertTrue(new ZpvNummer(17742883L).equals(new ZpvNummer("17742883")));

		assertTrue(new ZpvNummer(243911690L).isValid());

	}

	/**
	 * Liest eine Datei mit gueltigen ZPV-Nummern ein und prueft alle Eintraege daraus.
	 */
	@Test
	public void doTestBulk() throws EOFException, IOException {

		CSVReader r = new CSVReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
			.getResourceAsStream("zpv_nummern.csv")));
		r.skipToNextLine();

		int lines = 0;

		while (true) {
			lines++;

			try {
				String zpvText = r.get();
				r.skipToNextLine();

				try {
					ZpvNummer zpvNummer = new ZpvNummer(zpvText);
					assertEquals("ZPV-Nummer: " + zpvText, Boolean.TRUE, zpvNummer.isValid());
				} catch (NumberFormatException ignored) {
					fail("ZPV darf keine Sonderzeichen enthalten: " + zpvText);
				}

			} catch (EOFException ignored) {
				break;
			}
		}

		// sicherstellen, das wir auch alle Zeilen gelesen haben :)
		assertEquals(237307, lines);

	}

	/**
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testOutOfRangeTooLow() {

		new ZpvNummer(10000000L - 1);
		new ZpvNummer(ZpvNummer.MIN_VALUE - 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutOfRangeTooLowWithConstant() {

		new ZpvNummer(ZpvNummer.MIN_VALUE - 1);
	}

	/**
	 *
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testOutOfRangeTooHigh() {

		new ZpvNummer(999999999L + 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutOfRangeTooHighWithConstant() {

		new ZpvNummer(ZpvNummer.MAX_VALUE + 1);
	}

	public void testLasttest() {
		new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE, true);
		new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE + 1, true);
		new ZpvNummer(ZpvNummer.LASTTEST_MAX_VALUE - 1, true);
		new ZpvNummer(ZpvNummer.LASTTEST_MAX_VALUE, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStictButLasttest() {

		new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLasttestTooLow() {
		new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE - 1, true);
		new ZpvNummer(ZpvNummer.LASTTEST_MAX_VALUE, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLasttestTooHigh() {
		new ZpvNummer(ZpvNummer.LASTTEST_MAX_VALUE + 1, true);
	}

	/**
	 *
	 */
	@Test
	public void testPruefziffer() {

		ZpvNummer zpvNr = new ZpvNummer(17742883L);
		assertEquals("Prüfziffer ist falsch", 3, zpvNr.getPruefziffer());
		assertTrue(zpvNr.isValid());
	}

	/**
	 *
	 */
	@Test
	public void testEqualsHashCode() {

		ZpvNummer zpvNr = new ZpvNummer(17742883L);
		assertEquals(550029376, zpvNr.hashCode());
		assertFalse(new ZpvNummer(17742884L).hashCode() == zpvNr.hashCode());

		assertTrue(zpvNr.equals(zpvNr));
		assertFalse(zpvNr.equals(null));
		assertFalse(zpvNr.equals(new ZpvNummer("10000000")));
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, new ZpvNummer(17742883L).compareTo(new ZpvNummer(17742883L)));
		assertEquals(1, new ZpvNummer(17742883L).compareTo(new ZpvNummer(17742884L)));
		assertEquals(-1, new ZpvNummer(17742884L).compareTo(new ZpvNummer(17742883L)));
	}
}
