package com.wanologicalsolutions.apps.bankadmin.ui.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wanologicalsolutions.apps.bankadmin.R
import com.wanologicalsolutions.apps.bankadmin.common.adapters.ClientPaginationAdapter
import com.wanologicalsolutions.apps.bankadmin.common.adapters.PaginationLoadStateAdapter
import com.wanologicalsolutions.apps.bankadmin.common.adapters.SearchQueryAdapter
import com.wanologicalsolutions.apps.bankadmin.common.enums.Action
import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import com.wanologicalsolutions.apps.bankadmin.databinding.ActivityDashboardBinding
import com.wanologicalsolutions.apps.bankadmin.helpers.alert.confirmAlert
import com.wanologicalsolutions.apps.bankadmin.helpers.alert.messageAlert
import com.wanologicalsolutions.apps.bankadmin.helpers.extensions.toArrayList
import com.wanologicalsolutions.apps.bankadmin.helpers.session.SessionManager
import com.wanologicalsolutions.apps.bankadmin.network.enums.Status
import com.wanologicalsolutions.apps.bankadmin.ui.auth.LoginActivity
import com.wanologicalsolutions.apps.bankadmin.ui.client.ClientProfileActivity
import com.wanologicalsolutions.apps.bankadmin.ui.client.NewOrUpdateClientActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context) = Intent(context, DashboardActivity::class.java)
    }


    private val viewModel by viewModels<DashboardViewModel>()

    private lateinit var binding: ActivityDashboardBinding

    @Inject
    lateinit var adapter: ClientPaginationAdapter

    @Inject
    lateinit var adapterRecentSearches: SearchQueryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setupAdapter()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonNewClient.setOnClickListener {
            NewOrUpdateClientActivity.createInstance(this).let {
                startActivity(it)
            }
        }
    }

    private fun initViews() {
        binding.layoutToolbar.title = "Clients"
        binding.layoutToolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(binding.layoutToolbar)
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.clients.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = PaginationLoadStateAdapter { adapter.retry() }
        )
        adapter.onItemClickCallback = { _, item, action ->
            when (action) {
                Action.ACTION_DELETE -> confirmAlert(
                    "Are you sure to delete client",
                    onPrimaryClickListener = {
                        viewModel.clientId.value = item.id
                        requestDelete()
                    }
                )
                Action.ACTION_EDIT -> NewOrUpdateClientActivity.createInstance(this, item).let {
                    startActivity(it)
                }
                Action.ACTION_VIEW -> ClientProfileActivity.createInstance(this, item).let {
                    startActivity(it)
                }
            }

        }

        binding.progressModule.buttonRetry.setOnClickListener {
            adapter.retry()
        }

        // show the loading state for te first load
        adapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {

                binding.progressModule.buttonRetry.visibility = View.GONE

                // Show ProgressBar
                binding.progressModule.viewProgress.visibility = View.VISIBLE
                binding.progressModule.layoutProgress.visibility = View.VISIBLE
            } else {
                // Hide ProgressBar
                binding.progressModule.layoutProgress.visibility = View.VISIBLE
                binding.progressModule.viewProgress.visibility = View.GONE

                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> {
                        binding.progressModule.buttonRetry.visibility = View.VISIBLE
                        loadState.refresh as LoadState.Error
                    }
                    else -> null
                }
            }
        }
    }

    private fun requestDelete() = viewModel
        .requestDelete()
        .observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressModule.layoutProgress.visibility = View.GONE
                    it.data?.let { response ->
                        if (response.success == true) {
                            messageAlert(
                                response.message
                                    ?: CONSTANTS.MESSAGES.SOMETHING_WENT_WRONG_PUBLIC,
                            ) {
                                adapter.refresh()
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.dashboard_menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        val searchEditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(resources.getColor(R.color.white))
        searchEditText.setHintTextColor(resources.getColor(R.color.white))
        searchViewItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                binding.layoutRecentSearches.visibility = View.VISIBLE
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                binding.layoutRecentSearches.visibility = View.GONE
                return true
            }

        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                adapter.refresh()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText
                if (newText?.isEmpty() == true)
                    adapter.refresh()
                return false
            }
        })
        binding.recyclerViewRecentSearches.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerViewRecentSearches.adapter = adapterRecentSearches
        adapterRecentSearches.onItemClickListener = { query ->
            searchEditText.setText(query.query)
            searchView.clearFocus()
            adapter.refresh()
        }
        viewModel.recentSearches.asLiveData().observe(this) {
            it.toArrayList().let {
                adapterRecentSearches.clearAndAddItems(it)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            confirmAlert(
                "Are you sure to logout",
                onPrimaryClickListener = {
                    lifecycleScope.launch {
                        SessionManager.logout()
                        LoginActivity.createInstance(this@DashboardActivity).let {
                            startActivity(it)
                            finishAffinity()
                        }
                    }
                }
            )
        }
        return super.onOptionsItemSelected(item)
    }

}