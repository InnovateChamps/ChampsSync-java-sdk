package com.innovatechamps.sync.resources.preference;

import com.innovatechamps.sync.resources.common.Address;

import lombok.Getter;

/** Shipping address. */
@Getter
public class PreferenceReceiverAddress extends Address {
  /** Country. */
  private String countryName;

  /** State. */
  private String stateName;

  /** Floor. */
  private String floor;

  /** Apartment. */
  private String apartment;

  /** City name. */
  private String cityName;
}
