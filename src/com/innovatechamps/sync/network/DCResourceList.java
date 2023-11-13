package com.innovatechamps.sync.network;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * List of resources returned by an API.
 *
 * @param <T> Type of resource being returned
 */
@Getter
@Setter
public class DCResourceList<T> extends DCResource {
  /** List of results. */
  private List<T> results;
}
