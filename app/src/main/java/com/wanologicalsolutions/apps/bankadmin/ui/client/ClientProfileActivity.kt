package com.wanologicalsolutions.apps.bankadmin.ui.client

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wanologicalsolutions.apps.bankadmin.R
import com.wanologicalsolutions.apps.bankadmin.common.adapters.AccountsAdapter
import com.wanologicalsolutions.apps.bankadmin.databinding.ActivityClientProfileBinding
import com.wanologicalsolutions.apps.bankadmin.models.Client
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientProfileActivity : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context, client: Client? = null) =
            Intent(context, ClientProfileActivity::class.java).apply {
                client?.let {
                    this.putExtra("client", it)
                }
            }
    }

    private val viewModel by viewModels<ClientViewModel>()

    @Inject
    lateinit var adapterAccounts: AccountsAdapter

    private lateinit var binding: ActivityClientProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setupObservers()
        loadData()
    }

    private fun initViews() {
        binding.layoutToolbar.title = "Client Profile"
        binding.layoutToolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.layoutToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupAccountsAdapter()
    }

    private fun setupObservers() {
        viewModel.accounts.observe(this) {
            it?.let {
                adapterAccounts.clearAndAddList(it)
            }
        }
        viewModel.client.observe(this) {
            it?.let {
                fillUpData(it)
            }
        }
    }

    private fun setupAccountsAdapter() {
        adapterAccounts.setManageView(false)
        binding.recyclerViewAccounts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAccounts.adapter = adapterAccounts
    }

    private fun loadData() {
        intent.getParcelableExtra<Client>("client")?.let {
            viewModel.client.value = it
            supportActionBar?.title = "%s %s".format(it.firstName, it.lastName)
        }
    }

    private fun fillUpData(it: Client) {

        Glide.with(binding.root.context)
            .load(it.profilePhoto)
            .placeholder(R.drawable.img_placeholder_client)
            .into(binding.imageViewUserProfile)

        binding.textViewName.text = "%s %s".format(it.firstName, it.lastName)
        binding.textViewEmail.text = it.email
        binding.textViewMobileNumber.text = it.mobileNumber
        binding.textViewPersonalID.text = it.personalId
        binding.textViewGender.text = it.gender
        binding.textViewCountry.text = it.country
        binding.textViewCity.text = it.city
        binding.textViewStreet.text = it.street
        binding.textViewZipCode.text = it.zip
        viewModel.accounts.value = it.processAccounts()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}