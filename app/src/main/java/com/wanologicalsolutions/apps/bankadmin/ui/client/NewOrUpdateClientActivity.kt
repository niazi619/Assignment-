package com.wanologicalsolutions.apps.bankadmin.ui.client

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.hbb20.countrypicker.dialog.launchCountryPickerDialog
import com.wanologicalsolutions.apps.bankadmin.R
import com.wanologicalsolutions.apps.bankadmin.common.adapters.AccountsAdapter
import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import com.wanologicalsolutions.apps.bankadmin.databinding.ActivityDashboardBinding
import com.wanologicalsolutions.apps.bankadmin.databinding.ActivityNewOrUpdateClientBinding
import com.wanologicalsolutions.apps.bankadmin.helpers.alert.confirmAlert
import com.wanologicalsolutions.apps.bankadmin.helpers.alert.messageAlert
import com.wanologicalsolutions.apps.bankadmin.helpers.extensions.isEmailValid
import com.wanologicalsolutions.apps.bankadmin.models.Client
import com.wanologicalsolutions.apps.bankadmin.network.enums.Response
import com.wanologicalsolutions.apps.bankadmin.network.enums.Status
import com.wanologicalsolutions.apps.bankadmin.network.responsebodies.ApiResponse
import com.wanologicalsolutions.apps.bankadmin.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewOrUpdateClientActivity : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context, client: Client? = null) =
            Intent(context, NewOrUpdateClientActivity::class.java).apply {
                client?.let {
                    this.putExtra("client", it)
                }
            }
    }

    private val viewModel by viewModels<ClientViewModel>()

    @Inject
    lateinit var adapterAccounts: AccountsAdapter

    private lateinit var binding: ActivityNewOrUpdateClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewOrUpdateClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setupEditObservers()
        setupObservers()
        setupListeners()
        fillUpData()
    }

    private fun fillUpData() {
        intent.getParcelableExtra<Client>("client")?.let {
            viewModel.clientId.value = it.id
            supportActionBar?.title = "Update Profile"

            binding.edittextEmail.setText(it.email)
            binding.edittextFirstName.setText(it.firstName)
            binding.edittextLastName.setText(it.lastName)
            binding.edittextPersonalId.setText(it.personalId)
            binding.numberCountryPicker.fullNumber = it.mobileNumber
            it.gender?.let {
                if (it.equals("Male", true)) {
                    binding.radioButtonMale.isChecked = true
                } else if (it.equals("Female", true)) {
                    binding.radioButtonFemale.isChecked = true
                }
            }
            binding.textViewCountry.text = it.country
            binding.edittextCity.setText(it.city)
            binding.edittextStreet.setText(it.street)
            binding.edittextZip.setText(it.zip)
            viewModel.accounts.value = it.processAccounts()
        }
    }

    private fun initViews() {
        binding.layoutToolbar.title = "New Client"
        binding.layoutToolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.layoutToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.numberCountryPicker.registerCarrierNumberEditText(binding.edittextNumber)
        setupAccountsAdapter()
    }

    private fun setupEditObservers() {
        binding.edittextEmail.addTextChangedListener {
            it?.trim()?.toString()?.let {
                viewModel.email.value = it
            }
        }
        binding.edittextFirstName.addTextChangedListener {
            it?.trim()?.toString()?.let {
                viewModel.firstName.value = it
            }
        }
        binding.edittextLastName.addTextChangedListener {
            it?.trim()?.toString()?.let {
                viewModel.lastName.value = it
            }
        }
        binding.edittextPersonalId.addTextChangedListener {
            it?.trim()?.toString()?.let {
                viewModel.personalId.value = it
            }
        }
        binding.edittextNumber.addTextChangedListener {
            binding.numberCountryPicker.fullNumberWithPlus?.let {
                viewModel.mobileNumber.value = it
            }
        }
        binding.edittextCity.addTextChangedListener {
            it?.trim()?.toString()?.let {
                viewModel.city.value = it
            }
        }
        binding.edittextStreet.addTextChangedListener {
            it?.trim()?.toString()?.let {
                viewModel.street.value = it
            }
        }
        binding.edittextZip.addTextChangedListener {
            it?.trim()?.toString()?.let {
                viewModel.zipCode.value = it
            }
        }
        binding.radioGroupGender.setOnCheckedChangeListener { _, _ ->
            viewModel.gender.value = if (binding.radioButtonMale.isChecked) "Male" else "Female"
        }
    }

    private fun setupObservers() {
        viewModel.accounts.observe(this) {
            it?.let {
                adapterAccounts.clearAndAddList(it)
            }
        }
    }

    private fun setupListeners() {
        binding.textViewCountry.setOnClickListener {
            launchCountryPickerDialog {
                it?.let {
                    binding.textViewCountry.text = it.name
                    viewModel.country.value = it.name
                }
            }
        }
        binding.buttonNewAccount.setOnClickListener {
            if (binding.cardViewNewAccount.visibility == View.GONE) {
                binding.cardViewNewAccount.visibility = View.VISIBLE
            }
        }
        binding.buttonDismissAccount.setOnClickListener {
            if (binding.cardViewNewAccount.visibility == View.VISIBLE) {
                binding.edittextAccountNumber.setText("")
                binding.cardViewNewAccount.visibility = View.GONE
            }
        }
        binding.buttonCreateAccount.setOnClickListener {
            if (binding.edittextAccountNumber.text.isNotEmpty()) {
                binding.edittextAccountNumber.text.trim().toString().let {
                    viewModel.accounts.value = viewModel.accounts.value?.apply {
                        this.add(it)
                    }
                    binding.edittextAccountNumber.setText("")
                    binding.cardViewNewAccount.visibility = View.GONE
                }
            } else {
                messageAlert(
                    "Please enter account number"
                )
            }
        }
        binding.buttonCreateClient.setOnClickListener {
            if (validate()) {
                requestCreateOrUpdateClient()
            }
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
        } else if (!binding.edittextEmail.text.toString().isEmailValid()) {
            validated = false
            messageAlert(
                "Please enter valid email"
            )
        } else if (binding.edittextFirstName.text.isEmpty()) {
            validated = false
            messageAlert(
                "Please enter ${binding.edittextFirstName.hint.toString().lowercase()}"
            )
        } else if (binding.edittextLastName.text.isEmpty()) {
            validated = false
            messageAlert(
                "Please enter ${binding.edittextLastName.hint.toString().lowercase()}"
            )
        } else if (binding.edittextNumber.text.isEmpty()) {
            validated = false
            messageAlert(
                "Please enter mobile number"
            )
        } else if (!binding.numberCountryPicker.isValidFullNumber) {
            validated = false
            messageAlert("Please enter valid mobile number")
        } else if (binding.edittextPersonalId.text.isEmpty()) {
            validated = false
            messageAlert(
                "Please enter ${binding.edittextPersonalId.hint.toString().lowercase()}"
            )
        } else if (binding.edittextPersonalId.text.toString().length != 11) {
            validated = false
            messageAlert(
                "Please enter valid personal id contains 11 characters"
            )
        } else if (!binding.radioButtonMale.isChecked && !binding.radioButtonFemale.isChecked) {
            validated = false
            messageAlert(
                "Please choose your gender"
            )
        } else if (viewModel.accounts.value?.size == 0) {
            validated = false
            messageAlert(
                "Please provide min 1 account number"
            )
        }
        return validated
    }

    private fun setupAccountsAdapter() {
        adapterAccounts.setManageView(true)
        binding.recyclerViewAccounts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAccounts.adapter = adapterAccounts
        adapterAccounts.onItemDeleteListener = { position, item ->
            confirmAlert(
                "Are you sure to delete account number",
                onPrimaryClickListener = {
                    viewModel.accounts.value = viewModel.accounts.value?.apply {
                        this.removeAt(position)
                    }
                }
            )
        }
    }

    private fun requestCreateOrUpdateClient() = if (viewModel.clientId.value != null) {
        viewModel
            .requestUpdateClient()
            .observe(this) {
                handleResponse(it)
            }
    } else {
        viewModel
            .requestCreateClient()
            .observe(this) {
                handleResponse(it)
            }
    }

    private fun handleResponse(it: Response<ApiResponse<Any>>) {
        when (it.status) {
            Status.SUCCESS -> {
                binding.progressModule.layoutProgress.visibility = View.GONE
                it.data?.let { response ->
                    if (response.success == true) {
                        messageAlert(
                            response.message
                                ?: CONSTANTS.MESSAGES.SOMETHING_WENT_WRONG_PUBLIC,
                        ) {
                            finish()
                        }
                    } else if (response.success == false) {
                        messageAlert(
                            response.message
                                ?: CONSTANTS.MESSAGES.SOMETHING_WENT_WRONG_PUBLIC,
                        )
                    }
                }
            }
            Status.ERROR -> {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}