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


    private val isInMultiWindow: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isInMultiWindowMode
            } else {
                false
            }
        }

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.values.contains(false)) {
                permissionCancel()
            } else {
                lifecycleScope.launch(Dispatchers.Default){
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

    private fun initApp() {
        sp = getSharedPreferences(FIRST_ACTION, MODE_PRIVATE)
        val isFirst = sp.getBoolean(FIRST_ACTION_IS_FIRST, true)
        if (isFirst) viewModel.firstAction()
        locationPermission.launch(
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private fun getLocation() {
        val locationProvider = getLocationProvider()
        if (locationProvider.isNotBlank()) {
            (getSystemService(Context.LOCATION_SERVICE) as LocationManager).getLocation(
                this@MainActivity, locationProvider
            ) {
                if (it == null) {
                    permissionCancel()
                } else {
                    lifecycleScope.launch {
                        if (viewModel.addLocation(it)) {
                            permissionCancel()
                        }
                    }
                }
            }
        }
    }

    private fun permissionCancel() {
        lifecycleScope.launch{
            if(viewModel.currentCityCount() <= 0){
                navController.navigate(R.id.action_toCity_popHome)
            }
        }
    }

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