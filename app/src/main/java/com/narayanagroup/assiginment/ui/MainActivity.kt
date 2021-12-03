package com.narayanagroup.assiginment.ui

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.narayanagroup.assiginment.AllPlayersApplication.*
import com.narayanagroup.assiginment.R
import com.narayanagroup.assiginment.adapters.ReposAdapter
import com.narayanagroup.assiginment.adapters.LoadingStateAdapter
import com.narayanagroup.assiginment.databinding.ActivityMainBinding
import com.narayanagroup.assiginment.utils.value

import com.narayanagroup.assiginment.utils.RecyclerViewItemDecoration
import com.narayanagroup.assiginment.viewmodels.MainViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter =
        ReposAdapter { name: String -> snackBarClickedPlayer(name) }

    private var searchJob: Job? = null
    private var myquery: String=""
    private var dup: String=""
    private var net: Boolean=false
    private val context: Context? = null
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        var ua = WebView(this).settings.userAgentString
        value=ua

        val ConnectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected == true) {
            net=true
            //Toast.makeText(this@MainActivity, "Network Available", Toast.LENGTH_LONG).show()
        } else {
            net=false
            Toast.makeText(this@MainActivity, "Network Not Available", Toast.LENGTH_LONG).show()
        }
        setUpAdapter()
        startSearchJob()
        binding.swipeRefreshLayout.setOnRefreshListener {

            adapter.refresh()

        }

        val mSearchView = findViewById<View>(R.id.floating_search_view) as FloatingSearchView
        mSearchView.setOnQueryChangeListener { oldQuery, newQuery ->
            //get suggestions based on newQuery
            dup=newQuery
            //pass them on to the search viewmSearchView.swapSuggestions(newSuggestions);
        }


        val card = findViewById(R.id.mycard) as CardView
        card.setOnClickListener{
            myquery=dup
            setUpAdapter()
            startSearchJob()
            hideSoftKeyboard(this)
        }

    }

    @ExperimentalPagingApi
    private fun startSearchJob() {

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPlayers(myquery,net)
                .collectLatest {
                    adapter.submitData(it)
                }
        }
        /**
         * Same thing but with Livedata
         */

//        searchJob?.cancel()
//        searchJob = lifecycleScope.launch {
//            viewModel.searchPlayersLiveData().observe(this@MainActivity, {
//
//                adapter.submitData(this@MainActivity.lifecycle, it)
//
//            })
//
//        }
    }

    private fun snackBarClickedPlayer(name: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        Snackbar.make(parentLayout, name, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun setUpAdapter() {

        binding.allProductRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            addItemDecoration(RecyclerViewItemDecoration())
        }
        binding.allProductRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { retry() }
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {

                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = true
                }
                binding.errorTxt.isVisible = false

            } else {
                binding.progress.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        binding.errorTxt.isVisible = true
                        binding.errorTxt.text = it.error.localizedMessage
                    }

                }

            }
        }

    }

    private fun retry() {
        adapter.retry()
    }
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }


}
