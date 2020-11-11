package com.jess.eaiclubnasa.view

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jess.eaiclubnasa.ApodUtils.getCurrentDate
import com.jess.eaiclubnasa.R
import com.jess.eaiclubnasa.model.ApodResult
import com.jess.eaiclubnasa.repository.ApodRepository
import com.jess.eaiclubnasa.viewmodel.ApodViewModel
import com.jess.eaiclubnasa.viewmodel.ApodViewModelFactory
import com.jess.eaiclubnasa.viewmodel.event.ApodEvent
import com.jess.eaiclubnasa.viewmodel.interactor.ApodInteractor
import com.jess.eaiclubnasa.viewmodel.state.ApodState
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers

class HomeActivity : AppCompatActivity() {
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

        rv_apod.adapter = adapter
        rv_apod.layoutManager = LinearLayoutManager(this)

        initViewModel()
        scrollPaginationList()
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

        viewModel.interpret(ApodInteractor.GetListApod(getCurrentDate()))
    }

    private fun showList(listApod: MutableList<ApodResult>) {
        adapter.update(listApod)
    }

    private fun scrollPaginationList() {

        rv_apod.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                @NonNull recyclerView: RecyclerView, dx: Int, dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val totalItemCount: Int = layoutManager?.itemCount ?: 0
                val lastVisible: Int = layoutManager?.findLastVisibleItemPosition() ?: 0
                val ultimoItem = lastVisible + 5 >= totalItemCount

                if (totalItemCount > 0 && ultimoItem &&
                    viewModel.viewEvent.value == ApodEvent.Loading(false)
                ) {
                    viewModel.interpret(ApodInteractor.GetListApod(adapter.getLastItem().date))
                }
            }
        })
    }

    private fun showLoading(status: Boolean) {
        pb_loading.visibility = if (status)
            View.VISIBLE
        else
            View.GONE
    }
}
