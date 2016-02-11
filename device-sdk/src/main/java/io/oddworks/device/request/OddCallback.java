package io.oddworks.device.request;


/**
 * @param <T> type of entity returned on success
 */
public interface OddCallback<T> {
    public void onSuccess(T entity);
    public void onFailure(Exception exception);
}