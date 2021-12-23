package com.hua.simpleweather

import android.Manifest
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.gyf.immersionbar.ktx.immersionBar
import com.hua.simpleweather.db.dao.CityDao
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.databinding.ActivityMainBinding
import com.hua.simpleweather.other.Contacts.FIRST_ACTION
import com.hua.simpleweather.other.Contacts.FIRST_ACTION_ISFIRST
import com.hua.simpleweather.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _bind :ActivityMainBinding ?= null
    private val bind get() = _bind!!

    @Inject
    lateinit var dao: WeatherDao

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var navController: NavController
    private lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        initBar()
        sp = getSharedPreferences(FIRST_ACTION, MODE_PRIVATE)
        val isFirst = sp.getBoolean(FIRST_ACTION_ISFIRST,true)
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
            if(dao.getCityCount() <= 0){
                navController.navigate(R.id.cityFragment)
            }
        }
    }

    private fun initBar() {
        immersionBar {
            transparentBar()
            keyboardEnable(true)
            autoDarkModeEnable(true)
            statusBarDarkFont(true)
        }
    }

    override fun onBackPressed() {
        //拦截返回
        if(navController.currentDestination?.id == R.id.homeFragment){
            finish()
        }else{
            super.onBackPressed()
        }
    }
}