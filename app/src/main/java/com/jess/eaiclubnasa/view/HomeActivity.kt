package com.jess.eaiclubnasa.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jess.eaiclubnasa.R
import com.jess.eaiclubnasa.model.ApodResult
import com.jess.eaiclubnasa.repository.ApodRepository
import com.jess.eaiclubnasa.viewmodel.ApodViewModel
import com.jess.eaiclubnasa.viewmodel.ApodViewModelFactory
import com.jess.eaiclubnasa.viewmodel.event.ApodEvent
import com.jess.eaiclubnasa.viewmodel.interactor.ApodInteractor
import com.jess.eaiclubnasa.viewmodel.state.ApodState
import kotlinx.coroutines.Dispatchers

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerViewApod: RecyclerView
    private lateinit var loading: ProgressBar
    private val viewModel: ApodViewModel by lazy {
        val factory = ApodViewModelFactory(Dispatchers.IO, ApodRepository())
        ViewModelProvider(this, factory).get(ApodViewModel::class.java)
    }

    private val adapter: ApodAdapter by lazy {
        ApodAdapter(
            ArrayList()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
        initViewModel()
    }

    private fun initViews() {
        recyclerViewApod = findViewById(R.id.rv_apod)
        loading = findViewById(R.id.loading)
    }

    private fun initViewModel() {
        viewModel.viewState.observe(this, Observer { state ->
            state?.let {
                when (it) {
                    is ApodState.ApodListSuccess -> showList(it.list)
                }
            }
        })

        viewModel.viewEvent.observe(this, Observer { event ->
            event?.let {
                when (it) {
                    is ApodEvent.Loading -> showLoading(it.status)
                }
            }
        })

        viewModel.interpret(ApodInteractor.GetListApod("2020-10-10"))
    }

    private fun showList(listApod: MutableList<ApodResult>) {
        adapter.update(listApod)
        recyclerViewApod.adapter = adapter
        recyclerViewApod.layoutManager = LinearLayoutManager(this)
    }

    private fun showLoading(status: Boolean) {
        when {
            status -> {
                loading.visibility = View.VISIBLE
            }
            else -> {
                loading.visibility = View.GONE
            }
        }
    }
}