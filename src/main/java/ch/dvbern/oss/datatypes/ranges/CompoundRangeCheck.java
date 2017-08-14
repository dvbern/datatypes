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
package ch.dvbern.oss.datatypes.ranges;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Range-Check, der mehrere Checks zusammenfasst. Der Check ist valide, wenn
 * <b>mindestens einer</b> der uebergebenen Checks valide ist. Wenn kein Check
 * uebergeben wurde, ist also der CompoundRangeCheck auch immer ungueltig!
 */
public class CompoundRangeCheck implements RangeCheck {

	private static final long serialVersionUID = 1L;

	private static final RangeCheck[] RANGE_CHECKS = new RangeCheck[0];

	@Nonnull
	private final RangeCheck checks[];

	/**
	 * Constructs CompoundRangeCheck
	 *
	 * @param checks 0..n Checks
	 */
	@SuppressWarnings("OverloadedVarargsMethod")
	public CompoundRangeCheck(@Nullable final RangeCheck... checks) {

		if (checks == null) {
			this.checks = RANGE_CHECKS;
		} else {
			this.checks = checks.clone();
		}
	}

	public CompoundRangeCheck() {
		this.checks = RANGE_CHECKS;
	}

	@Override
	public boolean checkRange(final long nummerToCheck) {

		for (RangeCheck check : checks) {
			if (check.checkRange(nummerToCheck)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Nonnull
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("CompoundRangeCheck[L(");
		for (int i = 0; i < checks.length; i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append(checks[i]);
		}
		sb.append(")]");
		return sb.toString();
	}
}
