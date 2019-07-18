package com.melardev.android.crud.datasource.common.models

import com.melardev.android.crud.datasource.common.enums.OperationStatus

class DataSourceOperation<T> private constructor(
    private val operationStatus: OperationStatus,
    val data: T?,
    val fullMessages: Array<String>?
) {

    val isSuccess: Boolean
        get() = operationStatus == OperationStatus.SUCCESS && data != null

    val isLoading: Boolean
        get() = operationStatus == OperationStatus.LOADING

    val isLoaded: Boolean
        get() = operationStatus != OperationStatus.LOADING

    companion object {

        fun <T> success(data: T): DataSourceOperation<T> {
            return DataSourceOperation(
                OperationStatus.SUCCESS,
                data,
                null
            )
        }

        fun <T> success(fullMessages: Array<String>, data: T): DataSourceOperation<T> {
            return DataSourceOperation(OperationStatus.SUCCESS, data, fullMessages)
        }

        fun <T> error(fullMessages: Array<String>, data: T?): DataSourceOperation<T> {
            return DataSourceOperation(
                OperationStatus.ERROR,
                data,
                fullMessages
            )
        }

        fun <T> loading(data: T?): DataSourceOperation<T> {
            return DataSourceOperation(
                OperationStatus.LOADING,
                data,
                null
            )
        }
    }
}
