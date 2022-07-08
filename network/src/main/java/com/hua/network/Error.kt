package com.hua.network

import androidx.annotation.StringRes
/**
 * @author Xiaoc
 * @since 2021/5/10
 *
 * 错误数据类，存储着错误码和具体错误信息
 */
data class Error(val errorMsg: String? = null,
                 @StringRes val errorMsgId: Int = R.string.base_network_description_unknown_error,
                 val errorCode: Int? = ApiError.unknownError.errorCode)