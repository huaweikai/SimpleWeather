package com.hua.network.calladapter

import com.hua.network.ApiError
import com.hua.network.ApiException
import com.hua.network.ApiResult
import kotlinx.serialization.json.JsonObject
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author Xiaoc
 * @since 2021/5/10
 */
class ApiResultCallAdapterFactory: CallAdapter.Factory(){

    /**
     * 检查是否是 Call<ApiResult<T>> 类型的返回类型
     */
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        /**
         * 凡是检测不通过的，直接抛异常，并提示使用者返回值的类型不正确
         */
        // 检查返回类型里是否是Call<T>类型
        check(getRawType(returnType) == Call::class.java){
            "返回值必须是 retrofit2.Call 类型"
        }
        check(returnType is ParameterizedType){
            "返回至类型必须是ParameterizedType对应类型"
        }

        // 取出Call<T>里的T，检查是否是ApiResult<T>类型
        val apiResultType = getParameterUpperBound(0,returnType)
        check(getRawType(apiResultType) == ApiResult::class.java){
            "返回包装类型必须是 ApiResult 类型"
        }
        check(apiResultType is ParameterizedType){
            "返回包装类型必须是ParameterizedType对应类型"
        }

        // 取出 ApiResult<T> 中的T，这才是网络请求返回的真正类型
        val dataType = getParameterUpperBound(0,apiResultType)

        return ApiResultCallAdapter<Any>(dataType)
    }

}

/**
 * CallAdapter适配器，也就是将 T 转换为 ApiResult<T> 的适配器
 */
class ApiResultCallAdapter<T> (private val type: Type): CallAdapter<T, Call<ApiResult<T>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<ApiResult<T>> {
        return ApiResultCall(call)
    }

}

/**
 * 继承Call接口
 * 它相当于在其中加上了请求的封装
 * 它将从服务端传回的数据类型封装为 ApiResult<T> 再回调
 * 并进行了一些错误处理
 */
class ApiResultCall<T>(private val delegate: Call<T>): Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        delegate.enqueue(object : Callback<T> {

            /**
             * 网络请求成功后回调该方法（无论statusCode是否为200）
             */
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) { // HTTP响应为[200..300)
                    val result = if (response.body() == null) {
                        ApiResult.Failure(ApiError.dataIsNull)
                    } else {
                        ApiResult.Success(response.body()!!)
                    }
                    callback.onResponse(this@ApiResultCall, Response.success(result))
                } else {
                    // HTTP响应为错误码
                    val result = ApiResult.Failure(ApiError.httpStatusError)
                    callback.onResponse(this@ApiResultCall, Response.success(result))
                }
            }

            /**
             * 网络请求失败后回调该方法
             */
            override fun onFailure(call: Call<T>, t: Throwable) {
                val failureResult = when (t) {
                    is ApiException -> {
                        ApiResult.Failure(t.error)
                    }
                    is SocketTimeoutException -> {
                        ApiResult.Failure(ApiError.timeoutError)
                    }
                    is ConnectException, is UnknownHostException -> {
                        ApiResult.Failure(ApiError.connectionError)
                    }
                    else -> {
                        ApiResult.Failure(ApiError.unknownError)
                    }
                }
                callback.onResponse(this@ApiResultCall, Response.success(failureResult))
            }

        })

    }

    override fun clone(): Call<ApiResult<T>> = ApiResultCall(delegate.clone())

    override fun execute(): Response<ApiResult<T>> {
        throw UnsupportedOperationException("不支持同步请求")
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        return delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

}