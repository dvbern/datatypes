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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ZpvNummerTest {

	@Test
	public void doTest() {

		ZpvNummer zpvNr = new ZpvNummer(17742883L);
		assertTrue(zpvNr.isValid());
		zpvNr = new ZpvNummer("17742883");
		assertTrue(zpvNr.isValid());

		assertEquals(new ZpvNummer(17742883L), new ZpvNummer("17742883"));

		assertTrue(new ZpvNummer(243911690L).isValid());

	}

	/**
	 * Liest eine Datei mit gueltigen ZPV-Nummern ein und prueft alle Eintraege daraus.
	 * <p>
	 * Koennte durch einen @CsvFileSource test ersetzt werden. <strong>ABER:</strong> >200k Tests crashen jede IDE.
	 */
	@Test
	public void doTestBulk() throws IOException {
		InputStream testDataStream = readTestResource("zpv_nummern.csv");
		int lines = 0;

		try (BufferedReader r = new BufferedReader(new InputStreamReader(testDataStream))) {
			r.readLine(); // skip first line: heading
			lines++;

			for (String zpvText = r.readLine(); zpvText != null; zpvText = r.readLine()) {
				lines++;
				try {
					ZpvNummer zpvNummer = new ZpvNummer(zpvText);
					assertEquals(Boolean.TRUE, zpvNummer.isValid(), "ZPV-Nummer: " + zpvText);
				} catch (NumberFormatException ignored) {
					fail("ZPV darf keine Sonderzeichen enthalten: " + zpvText);
				}

			}
		}

		// sicherstellen, das wir auch alle Zeilen gelesen haben :)
		assertEquals(237307, lines);
	}

	private InputStream readTestResource(@SuppressWarnings("SameParameterValue") String name) {
		return requireNonNull(currentThread().getContextClassLoader().getResourceAsStream(name));
	}

	@Test
	public void testOutOfRangeTooLow_using_literal() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(10000000L - 1)
		);

		assertThat(ex)
			.hasMessageContaining(" 9999999 ");
	}


	@Test
	public void testOutOfRangeTooLow_using_constant() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.MIN_VALUE - 1)
		);

		assertThat(ex)
			.hasMessageContaining(" 9999999 ");
	}

	@Test
	public void testOutOfRangeTooLowWithConstant() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.MIN_VALUE - 1)
		);

		assertThat(ex)
			.hasMessageContaining(" 9999999 ");
	}

	@Test
	public void testOutOfRangeTooHigh() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(999999999L + 1)
		);

		assertThat(ex)
			.hasMessageContaining("1000000000")
			.hasMessageContaining("10000000")
			.hasMessageContaining("999999999");
	}

	@Test
	public void testOutOfRangeTooHighWithConstant() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.MAX_VALUE + 1)
		);

		assertThat(ex)
			.hasMessageContaining("1000000000")
			.hasMessageContaining("10000000")
			.hasMessageContaining("999999999");
	}

	@ParameterizedTest
	@ValueSource(longs = {
		0, 1, 2, 9998, 9999
	})
	public void testLasttest(long zpv) {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(zpv, true)
		);

		assertThat(ex)
			.hasMessageContaining(" " + zpv + " ");
	}

	@Test
	public void testStrictButLasttest() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE)
		);

		assertThat(ex)
			.hasMessageContaining(" 1 ");

	}

	@Test
	public void testLasttestTooLow_when_strict() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE, true)
		);

		assertThat(ex)
			.hasMessageContaining(" 1 ");
	}

	@Test
	public void testLasttestTooLow_when_strict_2() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE - 1, true)
		);

		assertThat(ex)
			.hasMessageContaining(" 0 ");
	}

	@Test
	public void testLasttestTooLow_when_strict2() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.LASTTEST_MIN_VALUE - 1, true)
		);

		assertThat(ex)
			.hasMessageContaining(" 0 ");
	}

	@Test
	public void testLasttestTooHigh() {
		IllegalArgumentException ex = assertThrows(
			IllegalArgumentException.class,
			() -> new ZpvNummer(ZpvNummer.LASTTEST_MAX_VALUE + 1, true)
		);

		assertThat(ex)
			.hasMessageContaining("10000")
			.hasMessageContaining("10000000")
			.hasMessageContaining("999999999");
	}


	@Test
	public void testPruefziffer() {

		ZpvNummer zpvNr = new ZpvNummer(17742883L);
		assertEquals(3, zpvNr.getPruefziffer(), "Prüfziffer ist falsch");
		assertTrue(zpvNr.isValid());
	}

	@Test
	public void testEqualsHashCode() {

		ZpvNummer zpvNr = new ZpvNummer(17742883L);
		assertEquals(550029376, zpvNr.hashCode());
		assertNotEquals(new ZpvNummer(17742884L).hashCode(), zpvNr.hashCode());

		assertEquals(zpvNr, zpvNr);
		assertNotEquals(null, zpvNr);
		assertNotEquals(zpvNr, new ZpvNummer("10000000"));
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, new ZpvNummer(17742883L).compareTo(new ZpvNummer(17742883L)));
		assertEquals(1, new ZpvNummer(17742883L).compareTo(new ZpvNummer(17742884L)));
		assertEquals(-1, new ZpvNummer(17742884L).compareTo(new ZpvNummer(17742883L)));
	}
}
