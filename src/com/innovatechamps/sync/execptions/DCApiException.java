package com.innovatechamps.sync.execptions;

import com.innovatechamps.sync.network.DCResponse;

import lombok.Getter;

/** DCApiException class. */
@SuppressWarnings("serial")
@Getter
public class DCApiException extends Exception {
  private final int statusCode;

  private final DCResponse apiResponse;

  /**
   * DCApiException constructor.
   *
   * @param message message
   * @param response response
   */
  public DCApiException(String message, DCResponse response) {
    this(message, null, response);
  }

  /**
   * DCApiException constructor.
   *
   * @param message message
   * @param cause cause
   * @param response response
   */
  public DCApiException(String message, Throwable cause, DCResponse response) {
    super(message, cause);
    this.apiResponse = response;
    this.statusCode = response.getStatusCode();
  }
}
