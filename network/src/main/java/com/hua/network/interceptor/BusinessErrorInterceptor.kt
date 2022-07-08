package com.hua.network.interceptor

import com.hua.network.ApiError
import com.hua.network.ApiException
import com.hua.network.Error
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset

class BusinessErrorInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if(!response.isSuccessful){
            return response
        }

        val responseBody = response.body!!
        val source = responseBody.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        val contentType = responseBody.contentType()
        val charset: Charset = contentType?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
        val resultString = buffer.clone().readString(charset)

        val jsonObject = try {
            JSONObject(resultString)
        } catch (e: JSONException){
            throw ApiException(ApiError.dataTypeError)
        }

        if(!jsonObject.has("status")){
            throw ApiException(ApiError.dataTypeError)
        }

        val code = jsonObject.optString("status")
        if(code == "ok"){
            return response
        }

        // 如果没有走成功的路径，则代表这次请求有问题
        // 获取服务端返回的具体错误信息
        throw ApiException(
            Error(
                errorMsg = jsonObject.optString("error")
            )
        )
    }
}