package com.melardev.android.crud.datasource.common.repositories


import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.remote.dtos.responses.SuccessResponse
import io.reactivex.Observable

interface BaseRepository<T> {

    fun getAll(): Observable<DataSourceOperation<List<T>>>

    fun getById(id: Long): Observable<DataSourceOperation<T>>

    fun create(todo: T): Observable<DataSourceOperation<T>>

    fun update(todo: T): Observable<DataSourceOperation<T>>

    fun delete(id: Long?): Observable<DataSourceOperation<SuccessResponse>>
}
