package com.hua.network

import okio.IOException

/**
 * @author : huaweikai
 * @Date   : 2022/04/09
 * @Desc   :
 */
class ApiException(val error: Error) :IOException()