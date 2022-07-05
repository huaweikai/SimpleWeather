package com.hua.simpleweather

import android.Manifest
import android.content.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.hua.simpleweather.databinding.ActivityMainBinding
import com.hua.simpleweather.network.interfaces.WeatherService
import com.hua.simpleweather.other.Contacts.FIRST_ACTION
import com.hua.simpleweather.other.Contacts.FIRST_ACTION_IS_FIRST
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import com.hua.simpleweather.utils.disableAutoFill
import com.hua.simpleweather.utils.fullScreen
import com.hua.simpleweather.utils.setLightStatusBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _bind :ActivityMainBinding ?= null
    private val bind get() = _bind!!


    private val isInMultiWindow: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isInMultiWindowMode
            } else {
                false
            }
        }

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController
    private lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.disableAutoFill()
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupSystemBar()
        sp = getSharedPreferences(FIRST_ACTION, MODE_PRIVATE)
        val isFirst = sp.getBoolean(FIRST_ACTION_IS_FIRST,true)
        if(isFirst){
            viewModel.firstAction()
        }

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.findNavController()

        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            it.forEach { map->
                if(!map.value){
                    Toast.makeText(this, "${map.key}未获取权限", Toast.LENGTH_SHORT).show()
                }
            }
        }.launch(
            //okhttp缓存
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        )

        lifecycleScope.launch {
            //如果没有一个城市就直接到城市管理界面
            if(viewModel.getCityCount() <= 0){
                navController.navigate(R.id.cityFragment)
            }
        }
        lifecycleScope.launch{
            Log.d("TAG", "onCreate: ${service.getWeather("113.3869","34.53704")}")
        }
    }
    @Inject
    lateinit var service: WeatherService

    fun setupSystemBar() {
        if (!isInMultiWindow) {
            fullScreen()
        }
        if (resources.configuration.isNightModeActive) {
            setLightStatusBar(false)
        } else{
            setLightStatusBar(true)
        }
    }
}