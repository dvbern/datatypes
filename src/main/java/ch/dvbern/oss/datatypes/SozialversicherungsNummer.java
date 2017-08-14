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
 * Sozialversicherungsnummer (neue AHV-Nummer)
 *
 * @author beph
 */
@SuppressWarnings("ElementOnlyUsedFromTestCode")
public class SozialversicherungsNummer extends AbstractPruefzifferNummer
	implements Comparable<SozialversicherungsNummer> {

	private static final long serialVersionUID = 923267806499518804L;

	private static final long MAX_VALUE = 9999999999999L;
	private static final long MIN_VALUE = 1000000000000L;

	public SozialversicherungsNummer() {
		// Serializable Class requires a no-arg constructor
	}

	/**
	 * Konstruktor einer neuen Sozialversicherungsnummer anhand eines long.
	 */
	public SozialversicherungsNummer(final long nummer) {

		super(nummer, MIN_VALUE, MAX_VALUE);
	}

	/**
	 * Konstruktor einer Sozialversicherungsnummer anhand eines Strings.
	 */
	public SozialversicherungsNummer(@Nonnull final String nummer) {

		super(nummer, MIN_VALUE, MAX_VALUE);
	}

	/**
	 * Berechnet die Pruefziffer.
	 */
	@Override
	protected int berechnePruefziffer(final long nummerToCalculate) {
		// Entferne die Pruefziffer (ignoriere sie für die Pruefziffer Berechnung)
		long n = nummerToCalculate / 10;
		int pruefziffer = 0;
		int z = 1;
		while (n > 0) {
			long ziffer = n % 10;
			//noinspection NumericCastThatLosesPrecision
			pruefziffer = incrementPruefziffer(pruefziffer, (int) ziffer, z);
			z++;
			n /= 10;
		}
		int returnValue = 0;
		if (pruefziffer % 10 != 0) {
			returnValue = 10 - pruefziffer % 10;
		}
		return returnValue;
	}

	private int incrementPruefziffer(final int pruefziffer, final int ziffer, final int z) {

		int increment = ziffer;
		if (z % 2 != 0) {
			increment *= 3;
		}
		return pruefziffer + increment;
	}

	@Nonnull
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(Long.toString(getNummerAsLong()));
		sb.insert(3, '.');
		sb.insert(8, '.');
		sb.insert(13, '.');
		return sb.toString();
	}

	public int compareTo(@Nonnull final SozialversicherungsNummer o) {
		// invers!
		return Long.valueOf(o.getNummerAsLong()).compareTo(getNummerAsLong());
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
