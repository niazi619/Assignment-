package com.wanologicalsolutions.apps.bankadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wanologicalsolutions.apps.bankadmin.databinding.ActivitySplashBinding
import com.wanologicalsolutions.apps.bankadmin.helpers.session.SessionManager
import com.wanologicalsolutions.apps.bankadmin.ui.auth.LoginActivity
import com.wanologicalsolutions.apps.bankadmin.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val activityScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initListeners()
    }

    private fun initListeners() {
        activityScope.launch {
            delay(1500)
            SessionManager.isLoginFlow.firstOrNull()?.let {
                when (it) {
                    true -> moveToDashboard()
                    false -> moveToLogin()
                }
            }
        }
    }

    private fun moveToLogin() = LoginActivity.createInstance(this).let {
        startActivity(it).apply {
            finishAffinity()
        }
    }

    private fun moveToDashboard() = DashboardActivity.createInstance(this).let {
        startActivity(it).apply {
            finishAffinity()
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}