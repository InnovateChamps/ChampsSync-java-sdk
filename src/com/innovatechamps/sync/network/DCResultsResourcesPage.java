package com.innovatechamps.sync.network;

import java.util.List;

import com.innovatechamps.sync.resources.ResultsPaging;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DCResultsResourcesPage class.
 *
 * @param <T> class type
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DCResultsResourcesPage<T> extends DCResource {
  private ResultsPaging paging;

  private List<T> results;
}
