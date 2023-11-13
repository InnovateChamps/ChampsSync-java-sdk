package com.innovatechamps.sync.network;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** DCResponse class. */
@Getter
@AllArgsConstructor
public class DCResponse {

  private final Integer statusCode;

  private final Map<String, List<String>> headers;

  private final String content;
}
