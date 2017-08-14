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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Postkontonummer, Modulo 10 rekursiv
 *
 * @author hefr
 */
@SuppressWarnings("ElementOnlyUsedFromTestCode")
public class PostkontoNummer extends AbstractPruefzifferNummer implements Comparable<PostkontoNummer> {

	private static final long serialVersionUID = 1L;
	private static final long MAX_VALUE = 999999999L;
	private static final long MIN_VALUE = 100000000L;
	private static final int[] TABELLE = { 0, 9, 4, 6, 8, 2, 7, 1, 3, 5 };

	/**
	 * Postkontonummern können 8 oder 9-stellig sein: XX-XXXXX-X oder
	 * XX-XXXXXX-X Im Mittelteil muss also gegebenenfalls eine 0 ergänzt werden.
	 */
	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	private static String checkDigits(final String nummer) {

		// group1: erste 2 Zeichen
		// group3: letztes Zeichen
		// group2: Rest weniger eventuelle "-" (5 oder 6-stellig)
		String group1 = nummer.substring(0, 2);
		String group3 = nummer.substring(nummer.length() - 1);
		String group2 = nummer.substring(2, nummer.length() - 1);
		group2 = NON_DIGIT.matcher(group2).replaceAll("");

		return group1 + String.format("%06d", Integer.parseInt(group2)) + group3;
	}

	public PostkontoNummer() {
		// Serializable Class requires a no-arg constructor
	}

	/**
	 * Konstruktor einer neuen Postkontonummer anhand eines long.
	 */
	public PostkontoNummer(final long nummer) {

		super(checkDigits(String.valueOf(nummer)), MIN_VALUE, MAX_VALUE);
	}

	/**
	 * Konstruktor einer Postkontonummer anhand eines Strings.
	 */
	public PostkontoNummer(@Nonnull final String nummer) {

		super(checkDigits(nummer), MIN_VALUE, MAX_VALUE);
	}

	/**
	 * Berechnet die Pruefziffer.
	 */
	@Override
	protected int berechnePruefziffer(final long nummerToCalculate) {
		// Entferne die Pruefziffer (ignoriere sie für die Pruefziffer Berechnung)
		String numberAsString = String.valueOf(nummerToCalculate / 10);
		int uebertrag = 0;

		for (int i = 0; i < numberAsString.length(); i++) {
			Integer currentDigit = Integer.valueOf(numberAsString.substring(i, i + 1));
			uebertrag = TABELLE[(uebertrag + currentDigit) % 10];
		}

		return (10 - uebertrag) % 10;
	}

	@Nonnull
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(Long.toString(getNummerAsLong()));
		sb.insert(2, '-');
		sb.insert(9, '-');
		return sb.toString();
	}

	public int compareTo(@Nonnull final PostkontoNummer o) {
		// invers!
		return Long.valueOf(o.getNummerAsLong()).compareTo(getNummerAsLong());
	}

	protected PostkontoNummer(long nummer, long minValue, long maxValue) {
		super(nummer, minValue, maxValue);
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
