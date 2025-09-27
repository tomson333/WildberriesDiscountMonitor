package com.example.wbmonitor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wbmonitor.repository.ProductRepository
import com.example.wbmonitor.ui.ProductAdapter
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.BackoffPolicy
import androidx.work.WorkManager
import com.example.wbmonitor.worker.PriceCheckWorker

class MainActivity : AppCompatActivity() {
    private lateinit var repo: ProductRepository
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repo = ProductRepository(applicationContext)

        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(mutableListOf(),
            onDelete = { product ->
                lifecycleScope.launch {
                    repo.delete(product)
                    loadProducts()
                }
            },
            onRefresh = { product ->
                lifecycleScope.launch {
                    val newPrice = com.example.wbmonitor.worker.PriceCheckWorker.fetchPriceFromUrlStatic(product.url)
                    if (newPrice >= 0 && newPrice != product.lastKnownPrice) {
                        val updated = product.copy(lastKnownPrice = newPrice)
                        repo.update(updated)
                    }
                    loadProducts()
                }
            }
        )
        recycler.adapter = adapter

        findViewById<android.view.View>(R.id.addButton).setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        schedulePeriodicChecks()
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            val products = repo.all()
            adapter.updateData(products)
        }
    }

    private fun schedulePeriodicChecks() {
        val req = PeriodicWorkRequestBuilder<PriceCheckWorker>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "wb_price_check",
            ExistingPeriodicWorkPolicy.KEEP,
            req
        )
    }
}
