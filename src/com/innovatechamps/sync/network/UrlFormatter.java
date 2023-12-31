package com.innovatechamps.sync.network;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.execptions.DCException;

/** UrlFormatter class. */
public class UrlFormatter {

  /**
   * Method responsible for format a url and add query params.
   *
   * @param path path
   * @param queryParams queryParams
   * @return url formatted
   * @throws DCException exception
   */
  public static String format(String path, Map<String, Object> queryParams) throws DCException {
    StringBuilder builder = new StringBuilder();
    builder.append(generateFullPath(path));

    try {
      URL url = new URL(builder.toString());
      if (Objects.isNull(url.getQuery()) && Objects.nonNull(queryParams)) {
        builder.append("?");

        ArrayList<Map.Entry<String, Object>> entries = new ArrayList<>(queryParams.entrySet());
        for (int i = 0; i < entries.size(); i++) {
          String queryFormat = i == 0 ? "%s=%s" : "&%s=%s";
          builder.append(
              String.format(
                  queryFormat,
                  URLEncoder.encode(entries.get(i).getKey(), "UTF-8"),
                  URLEncoder.encode(entries.get(i).getValue().toString(), "UTF-8")));
        }
      }
    } catch (UnsupportedEncodingException | MalformedURLException e) {
      throw new DCException(
          String.format("Error while trying to add query string to path: %s", e.getMessage()));
    }

    return builder.toString();
  }

  private static String generateFullPath(String path) {
    String formatPattern = path.startsWith("/") ? "%s%s" : "%s/%s";

    return !path.startsWith("https")
        ? String.format(formatPattern, DevChampsConfig.BASE_URL, path)
        : path;
  }
}
