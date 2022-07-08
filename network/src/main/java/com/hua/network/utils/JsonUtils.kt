package com.hua.network.utils

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 * @author Xiaoc
 * @since 2022-01-08
 */
val globalJson = Json {
    // 取消严格模式，如果实体类没有包含Json字符串中的字段时不报错
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = true
}

/**
 * 使用 [json] 以及 [serializer] 将数据实体 [T] 转换为 [String]
 * > 转换失败返回 `""`
 */
inline fun <reified T> T?.toJsonString(
    json: Json = globalJson,
    serializer: SerializationStrategy<T>? = null
): String {
    return when {
        null == this -> ""
        null != serializer -> json.encodeToString(serializer, this)
        else -> json.encodeToString(this)
    }
}