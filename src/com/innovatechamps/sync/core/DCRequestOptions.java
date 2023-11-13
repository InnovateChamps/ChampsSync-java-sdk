package com.innovatechamps.sync.core;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

/** DCRequestOptions class. */
@Data
@Builder
public class DCRequestOptions {
  private String accessToken;

  private int connectionTimeout;

  private int connectionRequestTimeout;

  private int socketTimeout;

  private Map<String, String> customHeaders;

  /**
   * Create default DCRequestOptions.
   *
   * @return DCRequestOptions
   */
  public static DCRequestOptions createDefault() {
    return DCRequestOptions.builder().build();
  }
}
