package com.hua.simpleweather

/**
 * @author : huaweikai
 * @Date   : 2021/12/23
 * @Desc   : event
 */
sealed class ActionEvent{
    object Success:ActionEvent()
    data class Error(val message:String):ActionEvent()
    object Empty:ActionEvent()
    object Loading:ActionEvent()
}
