package com.innovatechamps.sync.network;

import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;

/** DCHttpClient interface. */
public interface DCHttpClient {

  /**
   * Method responsible to send a request.
   *
   * @param request request
   * @return response
   * @throws DCException exception
   */
  DCResponse send(DCRequest request) throws DCException, DCApiException;
}
