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
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Datentyp IBAN Nummer
 */
@SuppressWarnings("ElementOnlyUsedFromTestCode")
public class IBAN implements Serializable, Comparable<IBAN> {

	private static final long serialVersionUID = 1L;
	private static final Pattern NON_WORD = Pattern.compile("\\W");

	@Nonnull
	private final String ibannummer;

	private static class IbanInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private final int allowedLength;
		private final int clearingNrLength;

		IbanInfo(final int allowedLength, final int clearingNrLength) {

			this.allowedLength = allowedLength;
			this.clearingNrLength = clearingNrLength;
		}

		IbanInfo(final int allowedLength) {

			this.allowedLength = allowedLength;
			this.clearingNrLength = 0;
		}

		// Serializable Class requires a no-arg constructor
		protected IbanInfo() {
			allowedLength = 0;
			clearingNrLength = 0;
		}

		public int getClearingNrLength() {

			return clearingNrLength;
		}

		public int getAllowedLength() {

			return allowedLength;
		}

	}

	private static final Map<String, IbanInfo> IBAN_INFOS = new HashMap<String, IbanInfo>();

	static {
		IBAN_INFOS.put("AD", new IbanInfo(24, 4));
		IBAN_INFOS.put("AT", new IbanInfo(20, 5));
		IBAN_INFOS.put("BE", new IbanInfo(16, 3));
		IBAN_INFOS.put("BA", new IbanInfo(20, 3));
		IBAN_INFOS.put("BG", new IbanInfo(22, 4));
		IBAN_INFOS.put("CH", new IbanInfo(21, 5));
		IBAN_INFOS.put("CY", new IbanInfo(28, 3));
		IBAN_INFOS.put("CZ", new IbanInfo(24, 4));
		IBAN_INFOS.put("DE", new IbanInfo(22, 8));
		IBAN_INFOS.put("DK", new IbanInfo(18, 4));
		IBAN_INFOS.put("EE", new IbanInfo(20, 2));
		IBAN_INFOS.put("ES", new IbanInfo(24, 4));
		IBAN_INFOS.put("FO", new IbanInfo(18));
		IBAN_INFOS.put("FI", new IbanInfo(18, 6));
		IBAN_INFOS.put("FR", new IbanInfo(27, 5));
		IBAN_INFOS.put("GB", new IbanInfo(22, 4));
		IBAN_INFOS.put("GI", new IbanInfo(23, 4));
		IBAN_INFOS.put("GL", new IbanInfo(18));
		IBAN_INFOS.put("GR", new IbanInfo(27, 8));
		IBAN_INFOS.put("HU", new IbanInfo(28, 7));
		IBAN_INFOS.put("HR", new IbanInfo(21, 7));
		IBAN_INFOS.put("IE", new IbanInfo(22, 4));
		IBAN_INFOS.put("IS", new IbanInfo(26, 4));
		IBAN_INFOS.put("IT", new IbanInfo(27));
		IBAN_INFOS.put("LI", new IbanInfo(21, 5));
		IBAN_INFOS.put("LT", new IbanInfo(20, 5));
		IBAN_INFOS.put("LU", new IbanInfo(20, 3));
		IBAN_INFOS.put("LV", new IbanInfo(21, 4));
		IBAN_INFOS.put("MA", new IbanInfo(24, 3));
		IBAN_INFOS.put("MC", new IbanInfo(27, 5));
		IBAN_INFOS.put("MK", new IbanInfo(19, 3));
		IBAN_INFOS.put("MT", new IbanInfo(31, 4));
		IBAN_INFOS.put("NL", new IbanInfo(18, 4));
		IBAN_INFOS.put("NO", new IbanInfo(15, 4));
		IBAN_INFOS.put("PL", new IbanInfo(28, 7));
		IBAN_INFOS.put("PT", new IbanInfo(25, 8));
		IBAN_INFOS.put("RO", new IbanInfo(24, 4));
		IBAN_INFOS.put("RS", new IbanInfo(22, 3));
		IBAN_INFOS.put("SE", new IbanInfo(24, 3));
		IBAN_INFOS.put("SI", new IbanInfo(19, 5));
		IBAN_INFOS.put("SK", new IbanInfo(24, 4));
		IBAN_INFOS.put("SM", new IbanInfo(27));
		IBAN_INFOS.put("TN", new IbanInfo(24, 5));
		IBAN_INFOS.put("TR", new IbanInfo(26, 5));
	}

	// Serializable Class requires a no-arg constructor
	public IBAN() {
		ibannummer = "";
	}

	/**
	 * Constructs IBAN
	 */
	public IBAN(@Nonnull final String iban) {
		this.ibannummer = NON_WORD.matcher(iban).replaceAll("");
	}

	/**
	 * @return ibannummer
	 */
	@Nonnull
	public String getIbannummer() {
		return ibannummer;
	}

	/**
	 * Validate IBAN format and check digit.
	 *
	 * @return true, wenn sowohl Format wie auch Prüfziffer korrekt sind
	 */
	public boolean isValid() {
		return isCheckDigitValid();
	}

	/**
	 * Determines if the given IBAN is valid based on the check digit. To
	 * validate the checksum: 1. Check that the total IBAN length is correct as
	 * per the country. If not, the IBAN is invalid. 2. Move the four initial
	 * characters to the end of the string. 3. Replace the letters in the string
	 * with digits, expanding the string as necessary, such that A=10, B=11 and
	 * Z=35. 4. Convert the string to an integer and mod-97 the entire number.
	 * If the remainder is 1 you have a valid IBAN number.
	 *
	 * @return boolean indicating if specific IBAN has a valid check digit
	 */
	private boolean isCheckDigitValid() {
		if (ibannummer.length() < 2) {
			return false;
		}
		if (ibannummer.length() != getValidIBANLength()) {
			return false;
		}

		final BigInteger numericIBAN = getNumericIBAN(false);

		final int checkDigit = numericIBAN.mod(new BigInteger("97")).intValue();
		return checkDigit == 1;
	}

	/**
	 * Get the country IBAN length.
	 */
	private int getValidIBANLength() {

		final String code = ibannummer.substring(0, 2).toUpperCase(Locale.ENGLISH);

		IbanInfo info = IBAN_INFOS.get(code);
		return info == null ? 0 : info.getAllowedLength();
	}

	/**
	 * Get the country specific length of the clearing number.
	 */
	private int getClearingNrLength() {

		final String code = ibannummer.substring(0, 2).toUpperCase(Locale.ENGLISH);
		IbanInfo info = IBAN_INFOS.get(code);
		return info == null ? 0 : info.getClearingNrLength();
	}

	@Nonnull
	private BigInteger getNumericIBAN(final boolean isCheckDigitAtEnd) {
		String endCheckDigitIBAN = ibannummer;
		if (!isCheckDigitAtEnd) {
			// Move first four characters to end of string to put check digit at end
			endCheckDigitIBAN = ibannummer.substring(4) + ibannummer.substring(0, 4);
		}
		final StringBuilder numericIBAN = new StringBuilder();
		for (int i = 0; i < endCheckDigitIBAN.length(); i++) {
			if (Character.isDigit(endCheckDigitIBAN.charAt(i))) {
				numericIBAN.append(endCheckDigitIBAN.charAt(i));
			} else {
				numericIBAN.append(10 + getAlphabetPosition(endCheckDigitIBAN.charAt(i)));
			}
		}
		return new BigInteger(numericIBAN.toString());
	}

	private static int getAlphabetPosition(final char letter) {

		return Character.valueOf(Character.toUpperCase(letter)).compareTo('A');
	}

	/**
	 * Gibt die Clearing-Nummer zurück
	 *
	 * @return ClearingNr.
	 */
	@Nullable
	public String extractClearingNr() {

		if (!isValid()) {
			throw new IllegalArgumentException(
				"Methode darf nur mit gültiger IBAN Nr aufgerufen werden "
					+ getIbannummer());
		}
		int clearingNrLength = getClearingNrLength();
		if (clearingNrLength == 0) {
			// keine Angaben der Laenge
			return null;
		}
		return ibannummer.substring(4, clearingNrLength + 4);
	}

	public int compareTo(@Nonnull final IBAN other) {
		return getIbannummer().compareTo(other.getIbannummer());
	}

	/**
	 * Gibt den formatierten IBAN String zurueck falls genug Zeichen vorhanden
	 * sind, ansonsten wird direkt der eingegebene String zurueckgegeben
	 *
	 * @return User-Kompatible String-Repraesentation
	 */
	@Override
	@Nonnull
	public String toString() {

		StringBuilder sb = new StringBuilder(getIbannummer());
		int numberOfSpaces = sb.length() / 4;
		// ibannummer in 4er gruppen darstellen
		for (int i = 0; i < numberOfSpaces; i++) {
			saveInsert((i + 1) * 4 + i, sb);
		}
		return sb.toString().trim();
	}

	private void saveInsert(final int offset, @Nonnull final StringBuilder sb) {

		if (sb.length() >= offset) {
			sb.insert(offset, ' ');
		}
	}

	@Override
	public boolean equals(@Nullable final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		IBAN that = (IBAN) o;

		return this.getIbannummer().equals(that.getIbannummer());

	}

	@Override
	public int hashCode() {
		return 31 * getIbannummer().hashCode();
	}
}
