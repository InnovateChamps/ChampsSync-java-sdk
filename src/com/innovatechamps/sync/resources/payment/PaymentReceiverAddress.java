package com.innovatechamps.sync.resources.payment;

import com.innovatechamps.sync.resources.common.Address;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/** PaymentReceiverAddress class. */
@EqualsAndHashCode(callSuper = true)
@Getter
public class PaymentReceiverAddress extends Address {
	/** State. */
	private String stateName;

	/** City. */
	private String cityName;

	/** Floor. */
	private String floor;

	/** Apartment. */
	private String apartment;
}
