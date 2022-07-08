package com.hua.resource

import com.hua.model.weather.WeatherVO


fun getDetailIcon(detailViewType: DetailViewType):Int{
    return when(detailViewType){
        DetailViewType.ViewTypeWind -> R.drawable.ic_detail_windy_speed
        DetailViewType.ViewTypeWet -> R.drawable.ic_detail_wet
        DetailViewType.ViewTypeUltraviolet->R.drawable.ic_detail_ultraviolet
        DetailViewType.ViewTypePressure ->R.drawable.ic_detail_pressure
        DetailViewType.ViewTypeVisibility -> R.drawable.ic_detail_eye
        DetailViewType.ViewTypeCloudRate -> R.drawable.ic_detail_cloud
    }
}

fun getDetailTile(detailViewType: DetailViewType):String{
    return when(detailViewType){
        DetailViewType.ViewTypeWind -> "实时风速"
        DetailViewType.ViewTypeWet -> "相对湿度"
        DetailViewType.ViewTypeUltraviolet->"紫外线"
        DetailViewType.ViewTypePressure ->"气压"
        DetailViewType.ViewTypeVisibility -> "能见度"
        DetailViewType.ViewTypeCloudRate -> "云量"
    }
}

fun getDetailSubTile(detailViewType: DetailViewType,data:WeatherVO.Result):String{
    return when(detailViewType){
        DetailViewType.ViewTypeWind -> "${getWindDirection(data.hourly.wind[0].speed)} ${data.hourly.wind[0].direction}米/秒"
        DetailViewType.ViewTypeWet -> "${data.realtime.humidity * 100}%"
        DetailViewType.ViewTypeUltraviolet-> "${data.realtime.dswrf}"
        DetailViewType.ViewTypePressure -> "${data.realtime.pressure}"
        DetailViewType.ViewTypeVisibility -> "${data.realtime.visibility}km"
        DetailViewType.ViewTypeCloudRate -> "${data.realtime.cloudRate}%"
    }
}


sealed class DetailViewType(val type: Int) {
    object ViewTypeWind : DetailViewType(1)
    object ViewTypeWet : DetailViewType(2)
    object ViewTypeUltraviolet : DetailViewType(3)
    object ViewTypePressure : DetailViewType(4)
    object ViewTypeVisibility : DetailViewType(5)
    object ViewTypeCloudRate : DetailViewType(6)
}