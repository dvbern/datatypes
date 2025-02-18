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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for IBAN
 */
public class IBANTest {

	private static final String IBAN_1 = "CH63 0900 0000 2500 9779 8";
	private static final String IBAN_2 = "CH95 0900 0000 6076 1739 7";
	private static final String IBAN_WITHOUT_CLEARING_NUMBER = "FO92 6460 0123 4567 89";

	private static final String IBAN_1_UNFORMATTED = noWhitespace(IBAN_1);
	private static final String IBAN_2_UNFORMATTED = noWhitespace(IBAN_2);
	private static final String IBAN_WITHOUT_CLEARING_NUMBER_UNFORMATTED = noWhitespace(IBAN_WITHOUT_CLEARING_NUMBER);

	private static final String CLEARING = "09000";

	private static String noWhitespace(String iban) {
		return iban.replaceAll("\\s", "");
	}

	@Nested
	class ExtractClearingNumber {

		@Test
		public void extracts_clearingNumber() {
			assertThat(new IBAN(IBAN_1).extractClearingNr())
				.isEqualTo(CLEARING);
			assertThat(new IBAN(IBAN_2).extractClearingNr())
				.isEqualTo(CLEARING);
			assertThat(new IBAN(IBAN_1_UNFORMATTED).extractClearingNr())
				.isEqualTo(CLEARING);
			assertThat(new IBAN(IBAN_2_UNFORMATTED).extractClearingNr())
				.isEqualTo(CLEARING);
		}

		@Test
		public void extracts_clearingNumber_for_iban_without_clearingNumber() {
			assertThat(new IBAN("FO92 6460 0123 4567 89").extractClearingNr())
				.isNull();
		}

		@Test
		public void throws_for_invalid_input() {
			assertThatThrownBy(() -> new IBAN("InvalidNumber").extractClearingNr())
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("InvalidNumber");
		}

	}

	@Nested
	class HashcodeEquals {

		@Test
		void equals_is_equal_with_same_instance() {
			IBAN iban = new IBAN(IBAN_1);
			assertThat(iban)
				.isEqualTo(iban);
		}

		@Test
		void equals_is_symmetric() {
			assertThat(new IBAN(IBAN_1))
				.isEqualTo(new IBAN(IBAN_1));
		}

		@Test
		public void equals_is_equal_independent_of_formatting() {
			assertThat(new IBAN(IBAN_1))
				.isEqualTo(new IBAN(IBAN_1_UNFORMATTED));
			assertThat(new IBAN(IBAN_1_UNFORMATTED))
				.isEqualTo(new IBAN(IBAN_1));

			assertThat(new IBAN(IBAN_WITHOUT_CLEARING_NUMBER))
				.isEqualTo(new IBAN(IBAN_WITHOUT_CLEARING_NUMBER_UNFORMATTED));
			assertThat(new IBAN(IBAN_WITHOUT_CLEARING_NUMBER_UNFORMATTED))
				.isEqualTo(new IBAN(IBAN_WITHOUT_CLEARING_NUMBER));
		}

		@Test
		public void equals_differs_for_different_ibans() {
			assertThat(new IBAN(IBAN_1))
				.isNotEqualTo(new IBAN(IBAN_2));
			assertThat(new IBAN(IBAN_2))
				.isNotEqualTo(new IBAN(IBAN_1));
		}

		@Test
		public void equals_differs_for_different_classes() {
			assertThat(new IBAN(IBAN_1))
				.isNotEqualTo(new Object());
		}

		@Test
		void equals_differs_for_null() {
			assertThat(new IBAN(IBAN_1))
				.isNotEqualTo(null);
		}

		@Test
		public void hashcode_is_same_independent_of_formatting() {
			assertThat(new IBAN(IBAN_1))
				.hasSameHashCodeAs(new IBAN(IBAN_1));
			assertThat(new IBAN(IBAN_1))
				.hasSameHashCodeAs(new IBAN(IBAN_1_UNFORMATTED));
		}
	}

	@Nested
	class IsValid {

		@Test
		public void accepts_valid_values() {
			assertThat(new IBAN(IBAN_1).isValid())
				.isTrue();
			assertThat(new IBAN(IBAN_2).isValid())
				.isTrue();
			assertThat(new IBAN(IBAN_1_UNFORMATTED).isValid())
				.isTrue();
			assertThat(new IBAN(IBAN_2_UNFORMATTED).isValid())
				.isTrue();
			assertThat(new IBAN(IBAN_WITHOUT_CLEARING_NUMBER).isValid())
				.isTrue();

			assertThat(new IBAN("AnyString").isValid())
				.isFalse();
			assertThat(new IBAN("XY123456").isValid())
				.isFalse();
			assertThat(new IBAN().isValid())
				.isFalse();
		}

		@Test
		public void rejects_numbers_with_invalid_checksum() {
			// a valid IBAN would be: CH63 0900 0000 2500 9779 8
			assertThat(new IBAN("CH63 0900 0000 2500 9779 9").isValid())
				.isFalse();
		}

		@Test
		public void rejects_invalid_values() {
			assertThat(new IBAN("AnyString").isValid())
				.isFalse();
			assertThat(new IBAN("XY123456").isValid())
				.isFalse();
			assertThat(new IBAN().isValid())
				.isFalse();
		}
	}

	@Nested
	class ToString {

		@Test
		public void formats_with_blanks_after_4_chars() {
			assertThat(new IBAN(IBAN_1))
				.hasToString(IBAN_1);
			assertThat(new IBAN(IBAN_1_UNFORMATTED))
				.hasToString(IBAN_1);

			assertThat(new IBAN("CH"))
				.hasToString("CH");
			assertThat(new IBAN("CH63"))
				.hasToString("CH63");
			assertThat(new IBAN("123456789"))
				.hasToString("1234 5678 9");
			assertThat(new IBAN("123456789    0123456789                    "))
				.hasToString("1234 5678 9012 3456 789");
			assertThat(new IBAN("12345678901234567890"))
				.hasToString("1234 5678 9012 3456 7890");
			assertThat(new IBAN("123456789012345678901"))
				.hasToString("1234 5678 9012 3456 7890 1");
		}
	}

	@Nested
	class CompareTo {

		@Test
		public void returns_zero_for_equal_value() {
			assertThat(new IBAN("CH63 0900 0000 2500 9779 8").compareTo(new IBAN("CH63 0900 0000 2500 9779 8")))
				.isZero();
		}

		@Test
		public void orders_ascending() {
			assertThat(new IBAN("CH63 0900 0000 2500 9779 8").compareTo(new IBAN("CH63 0900 0000 2500 9779 9")))
				.isNegative();
			assertThat(new IBAN("CH63 0900 0000 2500 9779 8").compareTo(new IBAN("CH63 0900 0000 2500 9779 7")))
				.isPositive();
		}

	}

}
