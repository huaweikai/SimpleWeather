package com.hua.simpleweather

import android.Manifest
import android.app.Application
import android.content.*
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.hua.network.Contacts.FIRST_ACTION
import com.hua.network.Contacts.FIRST_ACTION_IS_FIRST
import com.hua.simpleweather.databinding.ActivityMainBinding
import com.hua.network.api.WeatherService
import com.hua.network.onSuccess
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import com.hua.simpleweather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _bind: ActivityMainBinding? = null
    private val bind get() = _bind!!


    //获取当前activity是不是多窗口模式
    private val isInMultiWindow: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isInMultiWindowMode
            } else {
                false
            }
        }
    //activityForResult 申请位置等权限
    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.values.contains(false)) {
                //当有一个为false时，及调用权限为赋予
                permissionCancel()
            } else {
                lifecycleScope.launch(Dispatchers.Default){
                    //去获取当前定位
                    getLocation()
                }
            }
        }

    private val viewModel by viewModels<MainViewModel>()
    lateinit var navController: NavController
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.disableAutoFill()
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        //设置当前activity显示模式以及通知栏亮色暗色模式
        setupSystemBar()
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.findNavController()
        initApp()

    }

    private fun setupSystemBar() {
        if (!isInMultiWindow) {
            fullScreen()
        }
        if (resources.configuration.isNightModeActive) {
            setLightStatusBar(false)
        } else {
            setLightStatusBar(true)
        }
    }
    //初始化app操作
    private fun initApp() {
        sp = getSharedPreferences(FIRST_ACTION, MODE_PRIVATE)
        val isFirst = sp.getBoolean(FIRST_ACTION_IS_FIRST, true)
        //判断是否为第一次进入app，是的话，导入城市数据库
        if (isFirst) viewModel.firstAction()
        //检测权限
        locationPermission.launch(
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private fun getLocation() {
        //获取到当前使用的gps提供器
        val locationProvider = getLocationProvider()
        if (locationProvider.isNotBlank()) {
            (getSystemService(Context.LOCATION_SERVICE) as LocationManager).getLocation(
                this@MainActivity, locationProvider
            ) {
                //当位置为空时，及获取不到，走权限为空路线
                if (it == null) {
                    permissionCancel()
                } else {
                    lifecycleScope.launch {
                        //去为软件添加当前位置
                        if (viewModel.addLocation(it)) {
                            //如果获取天气信息失败，同样走 权限失败的路线
                            permissionCancel()
                        }
                    }
                }
            }
        }else{
            permissionCancel()
        }
    }

    private fun permissionCancel() {
        lifecycleScope.launch{
            //此时要判断当前城市数量，如果不为0，也就是无论是否定位失败，现在已经有天气数据，将不再跳转到城市管理界面
            if(viewModel.currentCityCount() <= 0){
                navController.navigate(R.id.action_toCity_popHome)
            }
        }
    }

    /**
     * 返回当前的位置提供器
     */
    private fun getLocationProvider(): String {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val providers = locationManager?.getProviders(true) ?: arrayListOf()
        if (providers.isEmpty()) {
            toastOnUi("没有可用的位置信息")
        }
        return when {
            providers.contains(LocationManager.NETWORK_PROVIDER) -> {
                //如果是Network
                LocationManager.NETWORK_PROVIDER
            }
            providers.contains(LocationManager.GPS_PROVIDER) -> {
                //如果是GPS
                LocationManager.GPS_PROVIDER
            }
            else -> {
                longToastOnUi("没有可用的位置提供器")
                ""
            }
        }
    }
}