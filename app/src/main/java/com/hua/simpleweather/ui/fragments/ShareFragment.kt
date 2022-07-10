package com.hua.simpleweather.ui.fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.ColorUtils
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.hua.model.AppInfoVo
import com.hua.model.weather.WeatherVO
import com.hua.network.utils.globalJson
import com.hua.resource.*
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseFragment
import com.hua.simpleweather.databinding.FragmentShareBinding
import com.hua.simpleweather.ui.adapter.ShareAppAdapter
import com.hua.simpleweather.utils.*
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.roundToInt

class ShareFragment : BaseFragment<FragmentShareBinding>() {

    private val readPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.values.contains(false)) {
                toastOnUi("权限不足")
            } else {
                saveImage()
            }
        }

    private val shareAppAdapter by lazy {
        ShareAppAdapter()
    }

    private var saveImageUri: Uri?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViewBind(FragmentShareBinding.inflate(inflater))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getString("weather")
        if (weather == null) findNavController().navigateUp()
        val result = globalJson.decodeFromString<WeatherVO>(weather!!)
        initView(result)
        shareAppAdapter.setData(getShareApps(requireContext()))

        shareAppAdapter.setOnClickListener {
            saveImageUri = saveImage()
            if(saveImageUri == null) {
                toastOnUi("保存为图片失败")
                return@setOnClickListener
            }
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, saveImageUri)
                type = "image/*"
                `package` = it.packageName
                startActivity(this)
            }
        }
    }

    private fun initView(weather: WeatherVO) {
        val layoutInflater = LayoutInflater.from(requireContext())
        val location = LocationUtils.getAddress(
            requireContext(),
            weather.lng.toDouble(),
            weather.lat.toDouble()
        )
        bind.apply {
            shareBg.load(getSkyBg(requireContext().isDarkMode(), weather.result.realtime.skycon)) {
                this.transformations(RoundedCornersTransformation(8.valueToPx(requireContext())))
            }
            shareClose.setOnClickListener {
                findNavController().navigateUp()
            }

            shareTemp.text =
                getString(R.string.sky_temp_now, weather.result.realtime.temperature.roundToInt())
            shareTemp.setTextColor(Color.WHITE)

            val aqi = weather.result.realtime.airQuality.aqi.chn
            shareSkyAir.text = getString(
                R.string.share_air_desc,
                getAQIDesc(aqi.toString()),
                aqi
            )
            shareSkyAir.setTextColor(Color.WHITE)

            shareSkyDesc.text = getSkyName(weather.result.realtime.skycon)
            shareSkyDesc.setTextColor(Color.WHITE)

            repeat(4) {
                shareDailyWeather.addView(
                    layoutInflater.inflate(R.layout.item_share_weather, shareDailyWeather, false)
                        .apply {
                            calendar.time =
                                resultDate.parse(weather.result.daily.astro[it].date) as Date
                            findViewById<TextView>(R.id.share_item_day).text = getString(
                                R.string.share_item_temp_date,
                                (calendar.get(Calendar.MONTH) + 1).toTime(),
                                calendar.get(Calendar.DAY_OF_MONTH).toTime()
                            )
                            findViewById<TextView>(R.id.share_item_day).setTextColor(Color.WHITE)
                            findViewById<ImageView>(R.id.share_item_skyIcon).setImageResource(
                                getSkyIcon(weather.result.daily.skycon[it].value)
                            )

                            findViewById<TextView>(R.id.share_item_temp).text = getString(
                                R.string.share_item_temp,
                                weather.result.daily.temperature[it].max.roundToInt(),
                                weather.result.daily.temperature[it].min.roundToInt()
                            )
                            findViewById<TextView>(R.id.share_item_temp).setTextColor(Color.WHITE)
                            findViewById<TextView>(R.id.share_item_air).text =
                                getAQIDesc(weather.result.daily.airQuality.aqi[it].avg.chn.toString())
                            findViewById<TextView>(R.id.share_item_air).setTextColor(Color.WHITE)

                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                1f
                            )
                        }
                )
            }





            shareCityThough.text = getString(
                R.string.share_city_detail,
                location?.adminArea,
                location?.subAdminArea,
                location?.thoroughfare
            )

            shareSkyCity.text = weather.cityName

            val outColor = shareSkyCity.currentTextColor
            val outContainerColor =
                ColorUtils.setAlphaComponent(outColor, (255 * 0.7f).roundToInt())
            val calendar = Calendar.getInstance()
            shareYearMoth.text = getString(
                R.string.share_year_month,
                calendar.get(Calendar.YEAR).toString(),
                calendar.get(Calendar.MONTH).toTime()
            )
            shareDay.text = calendar.get(Calendar.DAY_OF_MONTH).toTime()
            shareCityThough.setTextColor(outContainerColor)
            shareYearMoth.setTextColor(outContainerColor)
            shareDay.setTextColor(outContainerColor)

            shareAppRv.adapter = shareAppAdapter
        }
    }

    @SuppressLint("WrongConstant")
    fun getShareApps(context: Context): List<AppInfoVo> {
        val packageManager = context.packageManager
        val appInfo = arrayListOf<AppInfoVo>()
        val intent = Intent(Intent.ACTION_SEND).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            setType("image/*")
        }
        val resloveInfos = packageManager.queryIntentActivities(
            intent,
            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
        )
        resloveInfos.forEach {
            appInfo.add(
                AppInfoVo(
                    icon = it.loadIcon(packageManager),
                    appName = it.loadLabel(packageManager).toString(),
                    packageName = it.activityInfo.packageName,
                    launcherName = it.activityInfo.name,
                )
            )
        }
        return appInfo
    }

    private fun saveImage(): Uri? {
        val bitmap = bind.shareBitmap.drawToBitmap()
        val contentValues = ContentValues().apply {

            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "简易天气分享${simpleDateFormat.format(Date(System.currentTimeMillis()))}"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        }
        val saveUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        saveUri?.let {
            requireContext().contentResolver.openOutputStream(it)?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG,100,it)
            }
        }
        return saveUri

//        val bitmap = bind.shareBitmap.drawToBitmap()
//        val dir = File("${requireContext().externalCacheDir}/share_weather/")
//        if(!dir.exists()){
//            dir.mkdir()
//        }
//        val fileName = File(dir,"${System.currentTimeMillis()}.png")
//        Log.d("TAG", "saveImage: 开始保存图片")
//        val fos = FileOutputStream(fileName)
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
//        fos.flush()
//        fos.close()
//        Log.d("TAG", "saveImage: 保存图片成功")
//        return fileName.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(saveImageUri != null){
            context?.contentResolver?.delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "${MediaStore.Images.Media.DATA}=?",
                arrayOf(saveImageUri.toString())
            )

        }
    }
}