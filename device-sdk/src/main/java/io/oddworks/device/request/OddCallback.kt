package io.oddworks.device.request


/**
 * @param  type of entity returned on success
 */
interface OddCallback<T> {
    fun onSuccess(entity: T)
    fun onFailure(exception: Exception)
}