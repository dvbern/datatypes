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

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ch.dvbern.oss.datatypes.ranges.MinMaxRangeCheck;
import ch.dvbern.oss.datatypes.ranges.RangeCheck;

/**
 * Basisklasse einer Nummer, welche eine Prüfziffer beinhaltet.
 *
 * @author beph
 */
@SuppressWarnings("ElementOnlyUsedFromTestCode")
public abstract class AbstractPruefzifferNummer implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2480682944154690382L;
	protected static final Pattern NON_DIGIT = Pattern.compile("\\D");

	private final long nummer;

	private final int pruefziffer;

	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	private static void checkRange(final long nummerToCheck, @Nullable final RangeCheck rangeCheck) {

		if (rangeCheck == null) {
			return;
		}

		if (!rangeCheck.checkRange(nummerToCheck)) {
			throw new IllegalArgumentException("Argument " + nummerToCheck + " not in range: " + rangeCheck);
		}

	}

	// Serializable Class requires a no-arg constructor
	protected AbstractPruefzifferNummer() {
		nummer = 0;
		pruefziffer = 0;
	}

	/**
	 * Konstruktor einer neuen Pruefziffernummer anhand eines long.
	 */
	protected AbstractPruefzifferNummer(final long nummer, final long minValue, final long maxValue) {

		checkRange(nummer, new MinMaxRangeCheck(minValue, maxValue));
		this.nummer = nummer;
		this.pruefziffer = berechnePruefziffer(nummer);
	}

	/**
	 * Konstruktor einer neuen Pruefziffernummer anhand eines long.
	 *
	 * @param rangeCheck optional
	 */
	protected AbstractPruefzifferNummer(final long nummer, @Nullable final RangeCheck rangeCheck) {

		checkRange(nummer, rangeCheck);
		this.nummer = nummer;
		this.pruefziffer = berechnePruefziffer(nummer);
	}

	/**
	 * Konstruktor einer Pruefziffernummer anhand eines Strings.
	 *
	 * @throws NumberFormatException wenn beim Parsen des Strings ein Fehler
	 *                               auftritt, @see {@link Long#parseLong(String)}
	 */
	protected AbstractPruefzifferNummer(@Nonnull final String nummer, final long minValue, final long maxValue) {

		this(Long.parseLong(NON_DIGIT.matcher(nummer).replaceAll("")), minValue, maxValue);
	}

	/**
	 * Constructs AbstractPruefzifferNummer
	 *
	 * @param rangeCheck optional
	 */
	protected AbstractPruefzifferNummer(@Nonnull final String nummer, @Nullable final RangeCheck rangeCheck) {

		this(Long.parseLong(NON_DIGIT.matcher(nummer).replaceAll("")), rangeCheck);
	}

	/**
	 * Berechnen der Prüfziffer
	 *
	 * @param nummerToCalculate die Nummer für welche die Prüfziffer erstellt werden muss
	 * @return die berechente Prüfziffer
	 */
	protected abstract int berechnePruefziffer(long nummerToCalculate);

	/**
	 * @return Returns the nummer.
	 * @deprecated since 0.0.6, use {@link #getNummerAsLong()} instead.
	 */
	@Deprecated
	public long getNummer() {

		return nummer;
	}

	/**
	 * @return Returns the nummer as long.
	 */
	public long getNummerAsLong() {

		return nummer;
	}

	/**
	 * @return Returns the pruefziffer.
	 */
	public int getPruefziffer() {

		return pruefziffer;
	}

	/**
	 * Gibt {@code true} zurück, wenn die letzte Ziffer der berechneten
	 * Prüfziffer entspricht, sonst false.
	 *
	 * @return true wenn
	 * @see #getPruefziffer()
	 */
	public boolean isValid() {

		return nummer % 10 == pruefziffer;
	}

	/**
	 * @return die Nummer als String
	 * @deprecated since 0.0.6, replaced by {@link #getNummerAsString()}. Gibt
	 * die Nummer als String zurück. Convenience- Methode für
	 *
	 * <pre>
	 * 	{@code Long.toString(getNumber())}
	 * </pre>
	 */
	@Deprecated
	@Nonnull
	public String getNumberString() {

		return Long.toString(nummer);
	}

	/**
	 * Gibt die Nummer als String zurück. Convenience- Methode für
	 *
	 * <pre>
	 *    {@code Long.toString(getNumber())}
	 * </pre>
	 *
	 * @return die Nummer als String
	 */
	@Nonnull
	public String getNummerAsString() {

		return Long.toString(nummer);
	}

	@Override
	public boolean equals(@Nullable final Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AbstractPruefzifferNummer that = (AbstractPruefzifferNummer) o;

		if (nummer != that.nummer) {
			return false;
		}
		return pruefziffer == that.pruefziffer;

	}

	@Override
	public int hashCode() {

		int result = (int) (nummer ^ nummer >>> 32);
		result = 31 * result + pruefziffer;
		return result;
	}
}
