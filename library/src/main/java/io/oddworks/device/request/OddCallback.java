package io.oddworks.device.request;

import io.oddworks.device.model.Config;

/**
 * Created by brkattk on 9/15/15.
 */

/**
 * @param <T> type of entity returned on success
 */
public interface OddCallback<T> {
    public void onSuccess(T entity);
    public void onFailure(Exception exception);
}