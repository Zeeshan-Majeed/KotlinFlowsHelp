package com.fcmx.helper.kotlinflowshelper

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fcmx.helper.kotlinflowshelper.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observables()

        with(binding) {
            btnLivedata.setOnClickListener {
                mainViewModel.triggerLiveData()
            }
            btnStateFlow.setOnClickListener {
                mainViewModel.triggerStateFlow()
            }
            btnSharedFlow.setOnClickListener {
                mainViewModel.triggerSharedFlow()
            }
            btnSimpleFlow.setOnClickListener {
                collectLifecycleFlow(mainViewModel.simpleFLow) {
                    tvSimpleFlow.text = it
                }
            }
        }
    }

    private fun observables() {
        with(binding) {
            mainViewModel.liveData.observe(this@MainActivity) {
                tvLivedata.text = it
            }
            collectLifecycleFlow(mainViewModel.stateFlow) {
                tvStateFlow.text = it
            }

            collectLifecycleFlow(mainViewModel.sharedFlow) {
                tvSharedFlow.text = it
            }

            collectLifecycleFlow(mainViewModel.simpleFLow) {
                tvSimpleFlow.text = it
            }
        }
    }


    fun <T> AppCompatActivity.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

    fun <T> AppCompatActivity.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect(collect)
            }
        }
    }
}