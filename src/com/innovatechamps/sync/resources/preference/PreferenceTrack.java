package com.innovatechamps.sync.resources.preference;

import com.innovatechamps.sync.client.preference.PreferenceTrackValuesRequest;

import lombok.Getter;

/** Track to be executed during the users interaction in the Checkout flow. */
@Getter
public class PreferenceTrack {
  /** Track type (google_ad or facebook_ad). */
  private String type;

  /** Values according the track type. */
  private PreferenceTrackValuesRequest values;
}
