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

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ch.dvbern.oss.datatypes.ranges.CompoundRangeCheck;
import ch.dvbern.oss.datatypes.ranges.MinMaxRangeCheck;
import ch.dvbern.oss.datatypes.ranges.RangeCheck;

/**
 * Stellt eine ZpvNummer dar.
 *
 * @author beph
 */
@SuppressWarnings("ElementOnlyUsedFromTestCode")
public class ZpvNummer extends AbstractPruefzifferNummer implements Comparable<ZpvNummer> {

	private static final long serialVersionUID = -7862409348622892480L;

	/**
	 * Untergrenze fuer eine gueltige ZPV-Nummer
	 */
	public static final long MIN_VALUE = 10000000L;
	/**
	 * Obergrenze fuer eine gueltige ZPV-Nummer
	 */
	public static final long MAX_VALUE = 999999999L;

	/**
	 * Untergrenze fuer eine Lasttest-ZPV-Nummer
	 */
	public static final long LASTTEST_MIN_VALUE = 1L;
	/**
	 * Obergrenze fuer eine Lasttest-ZPV-Nummer
	 */
	public static final long LASTTEST_MAX_VALUE = 9999L;

	private static final RangeCheck CHECK_STRICT = new MinMaxRangeCheck(MIN_VALUE, MAX_VALUE);
	private static final RangeCheck CHECK_LASTTESTONLY = new MinMaxRangeCheck(LASTTEST_MIN_VALUE, LASTTEST_MAX_VALUE);
	private static final RangeCheck CHECK_LASTTEST_ALLOWED = new CompoundRangeCheck(CHECK_STRICT, CHECK_LASTTESTONLY);

	private static final Locale PRETTYPRINT_LOCALE = new Locale("de", "CH");

	public ZpvNummer() {
		// Serializable Class requires a no-arg constructor
	}

	/**
	 * @see #ZpvNummer(long, boolean) mit strict = true
	 */
	public ZpvNummer(final long nummer) {

		this(nummer, true);
	}

	/**
	 * @param nummer Text-Repraesentation der ZPV-Nummer. Vor dem Parsen werden
	 * vorher intern alle Sonderzeichen entfernt.
	 * @see #ZpvNummer(String, boolean) mit strict = true
	 */
	public ZpvNummer(@Nonnull final String nummer) {

		this(nummer, true);
	}

	/**
	 * @param strict true: Pruefung anhand der Spec. false: Lasttest-ZPV-Nummern
	 * (1-9999)erlauben.
	 */
	public ZpvNummer(final long nummer, final boolean strict) {

		super(nummer, strict ? CHECK_STRICT : CHECK_LASTTEST_ALLOWED);
	}

	/**
	 * @param strict true: Pruefung anhand der Spec. false: Lasttest-ZPV-Nummern
	 * (1-9999)erlauben.
	 */
	public ZpvNummer(@Nonnull final String nummer, final boolean strict) {

		super(nummer, strict ? CHECK_STRICT : CHECK_LASTTEST_ALLOWED);
	}

	@Override
	protected int berechnePruefziffer(final long nummerToCalculate) {

		//noinspection NumericCastThatLosesPrecision
		int restlicheNummer = (int) nummerToCalculate / 10; // bestehende Pruefziffer
		// abschneiden
		int pruefziffer = 0;
		boolean ungeradePosition = true;
		while (restlicheNummer > 0) {
			// immer nur die letzte Stelle betrachten
			// => 0 <= ziffer <= 9
			int ziffer = restlicheNummer % 10;

			int zifferTmp = ziffer;
			if (ungeradePosition) {
				// Ziffer an jeder ungeraden Position verdoppeln
				zifferTmp = ziffer * 2;
			}
			ungeradePosition = !ungeradePosition;
			// => 0 <= zifferTmp <= 18

			// Mehr Zufaelligkeit
			if (zifferTmp >= 10) {
				zifferTmp = zifferTmp % 10 + 1;
			}

			// Checksumme aktualisieren
			pruefziffer += zifferTmp;

			// naechste Stelle
			restlicheNummer /= 10;
		}
		// Sicherstellen, dass die Pruefziffer einstellig ist
		return (10 - pruefziffer % 10) % 10;
	}

	public int compareTo(@Nonnull final ZpvNummer o) {
		// invers
		return Long.valueOf(o.getNummerAsLong()).compareTo(getNummerAsLong());
	}

	@Override
	public String toString() {

		return NumberFormat.getInstance(PRETTYPRINT_LOCALE).format(getNummerAsLong());
	}

	@SuppressWarnings("EmptyMethod")
	@Override
	public boolean equals(@Nullable Object o) {
		return super.equals(o);
	}

	@SuppressWarnings("EmptyMethod")
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
