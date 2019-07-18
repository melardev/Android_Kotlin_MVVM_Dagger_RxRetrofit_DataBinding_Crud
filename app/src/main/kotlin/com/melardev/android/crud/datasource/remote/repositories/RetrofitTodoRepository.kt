package com.melardev.android.crud.datasource.remote.repositories


import com.google.gson.Gson
import com.melardev.android.crud.datasource.common.entities.Todo
import com.melardev.android.crud.datasource.common.models.DataSourceOperation
import com.melardev.android.crud.datasource.common.repositories.TodoRepository
import com.melardev.android.crud.datasource.remote.api.RxTodoApi
import com.melardev.android.crud.datasource.remote.dtos.responses.ErrorDataResponse
import com.melardev.android.crud.datasource.remote.dtos.responses.SuccessResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Singleton

@Singleton
class RetrofitTodoRepository(private val todoApi: RxTodoApi, private val gson: Gson) : TodoRepository {


    override fun getAll(): Observable<DataSourceOperation<List<Todo>>> {
        return todoApi.fetchAll()
            .flatMap { todoList ->
                Observable.just(
                    DataSourceOperation.success(
                        todoList
                    )
                )
            }
            .subscribeOn(Schedulers.io())
            .doOnNext { apiResponse -> println("Received " + apiResponse.data!!.size) }
            .onErrorReturn { throwable ->
                // We can not do it with lambda expressions
                // return buildDataSourceError(List.class, throwable);
                if (throwable.javaClass == HttpException::class.java) {
                    val response = (throwable as HttpException).response()
                    if (response != null) {
                        return@onErrorReturn DataSourceOperation.error(
                            buildErrorDataResponse(response)!!.fullMessages,
                            null
                        )
                    }
                }
                DataSourceOperation.error(
                    arrayOf<String>(
                        throwable.message!!
                    ), null
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getById(todoId: Long): Observable<DataSourceOperation<Todo>> {
        return todoApi.fetchById(todoId)
            .flatMap { todo ->
                Observable.just(
                    DataSourceOperation.success(
                        todo
                    )
                )
            }
            .subscribeOn(Schedulers.io())
            .doOnNext { apiResponse -> println("Received " + apiResponse.data) }
            .onErrorReturn { throwable -> buildDataSourceError(Todo::class.java, throwable) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun create(todo: Todo): Observable<DataSourceOperation<Todo>> {
        return todoApi.create(todo)
            .flatMap { response ->
                Observable.just(
                    DataSourceOperation.success(
                        response
                    )
                )
            }
            .subscribeOn(Schedulers.io())
            .doOnNext { apiResponse -> println("Received " + apiResponse.data) }
            .onErrorReturn { throwable -> buildDataSourceError(Todo::class.java, throwable) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(todo: Todo): Observable<DataSourceOperation<Todo>> {
        return todoApi.update(todo.id, todo)
            .flatMap { response -> Observable.just(DataSourceOperation.success(response)) }
            .subscribeOn(Schedulers.io())
            .doOnNext { apiResponse -> println("Received " + apiResponse.data) }
            .onErrorReturn { throwable -> buildDataSourceError(Todo::class.java, throwable) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(id: Long?): Observable<DataSourceOperation<SuccessResponse>> {
        return todoApi.delete(id)
            .flatMap { response ->
                return@flatMap if (response.isSuccess) {
                    Observable.just(DataSourceOperation.success(response.fullMessages, response))
                } else {
                    return@flatMap Observable
                        .just<DataSourceOperation<SuccessResponse>>(
                            DataSourceOperation.error(
                                response.fullMessages,
                                null
                            )
                        )
                }
            }
            .subscribeOn(Schedulers.io())
            .doOnNext { apiResponse -> println("Received " + apiResponse.data) }
            .onErrorReturn { throwable -> buildDataSourceError(SuccessResponse::class.java, throwable) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    @Suppress("unused")
    @Throws(IOException::class)
    private fun <T> buildDataSourceError(clazz: Class<T>, throwable: Throwable): DataSourceOperation<T> {

        if (throwable.javaClass == HttpException::class.java) {
            val response = (throwable as HttpException).response()
            if (response != null) {
                return DataSourceOperation.error(
                    buildErrorDataResponse(response)!!.fullMessages,
                    null
                )
            }
        }
        return DataSourceOperation.error(
            arrayOf<String>(throwable.message!!),
            null
        )
    }

    @Throws(IOException::class)
    private fun buildErrorDataResponse(responseBody: Response<*>): ErrorDataResponse? {
        var body = responseBody.errorBody()
        if (body == null)
            body = responseBody.body() as ResponseBody?

        return if (body == null) null else gson.fromJson(body.string(), ErrorDataResponse::class.java)

    }
}
