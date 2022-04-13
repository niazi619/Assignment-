package com.wanologicalsolutions.apps.bankadmin.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import com.wanologicalsolutions.apps.bankadmin.databinding.ActivityLoginBinding
import com.wanologicalsolutions.apps.bankadmin.helpers.alert.messageAlert
import com.wanologicalsolutions.apps.bankadmin.network.enums.Status
import com.wanologicalsolutions.apps.bankadmin.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        binding.edittextEmail.addTextChangedListener {
            viewModel.loginRequest.value?.email = it?.toString()
        }
        binding.edittextPassword.addTextChangedListener {
            viewModel.loginRequest.value?.password = it?.toString()
        }
    }

    private fun validate(): Boolean {
        var validated = true
        if (binding.edittextEmail.text.isEmpty()) {
            validated = false
            messageAlert(
                "Please enter ${
                    binding.edittextEmail.hint.toString().lowercase()
                }"
            )
        } else if (binding.edittextPassword.text.isEmpty()) {
            validated = false
            messageAlert(
                "Please enter ${binding.edittextPassword.hint.toString().lowercase()}"
            )
        }
        return validated
    }


    private fun setupListeners() {
        binding.buttonSignIn.setOnClickListener {
            if (validate()) {
                binding.buttonSignIn.isEnabled = false
                performLogin()
            }
        }
    }

    private fun performLogin() {
        viewModel.requestLogin().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.buttonSignIn.isEnabled = true
                    binding.progressModule.layoutProgress.visibility = View.GONE
                    it.data?.let { response ->
                        when (response.success) {
                            true -> {
                                moveToDashboard()
                            }
                            else -> {
                                messageAlert(
                                    response.message
                                        ?: CONSTANTS.MESSAGES.SOMETHING_WENT_WRONG_PUBLIC,
                                )
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    binding.buttonSignIn.isEnabled = true
                    binding.progressModule.layoutProgress.visibility = View.GONE
                    messageAlert(
                        it.message ?: CONSTANTS.MESSAGES.SOMETHING_WENT_WRONG_PUBLIC,
                    )
                }
                Status.LOADING -> {
                    binding.progressModule.layoutProgress.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun moveToDashboard() = DashboardActivity.createInstance(this).let {
        startActivity(it).apply {
            finishAffinity()
        }
    }
}