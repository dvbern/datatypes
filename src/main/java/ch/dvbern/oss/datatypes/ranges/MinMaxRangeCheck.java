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
 * RangeCheck, der einen Wert auf Unter- und/oder Obergrenze prueft.
 */
@SuppressWarnings("ElementOnlyUsedFromTestCode")
public class MinMaxRangeCheck implements RangeCheck {

	private static final long serialVersionUID = 1L;

	@Nullable
	private final Long minValue;
	@Nullable
	private final Long maxValue;

	/**
	 * Constructs MinMaxRangeCheck
	 *
	 * Ohne Grenzen
	 */
	// Serializable Class requires a no-arg constructor
	public MinMaxRangeCheck() {

		this.minValue = null;
		this.maxValue = null;
	}

	/**
	 * Constructs MinMaxRangeCheck
	 *
	 * @param minValue Optional. Untergrenze
	 * @param maxValue Optional. Obergrenze
	 */
	public MinMaxRangeCheck(@Nullable final Long minValue, @Nullable final Long maxValue) {

		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public boolean checkRange(final long nummerToCheck) {

		Long value = nummerToCheck;

		if (minValue != null && value.compareTo(minValue) < 0) {
			return false;
		}

		return !(maxValue != null && value.compareTo(maxValue) > 0);
	}

	@Override
	@Nonnull
	public String toString() {

		return "MinMaxRangeCheck[minValue=" + minValue + ",maxValue=" + maxValue + ']';
	}

	/**
	 * @return Untergrenze oder null
	 */
	@Nullable
	public Long getMinValue() {

		return minValue;
	}

	/**
	 * @return Obergrenze oder null
	 */
	@Nullable
	public Long getMaxValue() {

		return maxValue;
	}
}
