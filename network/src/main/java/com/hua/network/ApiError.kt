package com.hua.network

/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 客户端本地定义的网络请求相关错误
 */
object ApiError {

    private const val dataIsNullCode = 0x000F
    private const val unknownCode = 0x0010
    private const val dataTypeCode = 0x0011
    private const val httpStatusCode = 0x0012
    private const val timeoutCode = 0x0016
    private const val connectionErrorCode = 0x0017
    private const val requestErrorCode = 0x0018
    private const val userIsNullCode = 0x0020

    val dataIsNull = Error("返回的数据为空",errorMsgId = R.string.base_network_description_data_is_null_error,errorCode = dataIsNullCode)
    val unknownError = Error("未知错误",errorMsgId = R.string.base_network_description_unknown_error,errorCode = unknownCode)
    val connectionError = Error("网络连接失败，请检查网络",errorMsgId = R.string.base_network_description_connection_error,errorCode = connectionErrorCode)
    val timeoutError = Error("请求超时，请重试",errorMsgId = R.string.base_network_description_timeout_error,errorCode = timeoutCode)
    val dataTypeError = Error("数据返回类型不正确，解析发生错误",errorMsgId = R.string.base_network_description_timeout_error,errorCode = dataTypeCode)
    val httpStatusError = Error("与服务器连接异常",errorMsgId = R.string.base_network_description_http_status_error,errorCode = httpStatusCode)
    val requestError = Error("请求参数有误", errorMsgId = R.string.base_network_description_request_error, errorCode = requestErrorCode)

    val userIsNulError = Error("用户不存在", errorMsgId = R.string.base_network_description_user_is_null, errorCode = userIsNullCode)
}
