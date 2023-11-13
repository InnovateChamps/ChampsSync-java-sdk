package com.innovatechamps.sync.network;

import java.util.List;
import lombok.Getter;

/**
 * Search page that contains elements property.
 *
 * @param <T> type
 */
@Getter
public class DCElementsResourcesPage<T> extends DCResource {
  /** The total number of items that match search criteria. */
  private int total;

  /** Offset of the next page. */
  private int nextOffset;

  /** Items in this page. */
  private List<T> elements;
}
