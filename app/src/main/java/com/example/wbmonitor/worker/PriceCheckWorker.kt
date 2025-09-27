package com.example.wbmonitor.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wbmonitor.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import com.example.wbmonitor.util.NotificationUtils

class PriceCheckWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    private val repo = ProductRepository(appContext)
    private val client = OkHttpClient()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val products = repo.all()
            for (p in products) {
                try {
                    val price = fetchPriceFromUrl(p.url)
                    if (price >= 0 && price != p.lastKnownPrice) {
                        val previous = p.lastKnownPrice
                        repo.update(p.copy(lastKnownPrice = price))
                        if (previous >= 0 && price < previous) {
                            NotificationUtils.showPriceDrop(applicationContext, p, previous, price)
                        }
                    }
                } catch (e: Exception) {
                    // per-product errors ignored
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun fetchPriceFromUrl(url: String): Long {
        val req = Request.Builder().url(url).get().header("User-Agent","Mozilla/5.0").build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return -1
            val body = resp.body?.string() ?: return -1
            val doc = Jsoup.parse(body)
            val priceText = doc.selectFirst("[data-link=price], .price, .price-block, [itemprop=price]")?.text()
                ?: doc.selectFirst("span.price")?.text()
                ?: doc.selectFirst("meta[itemprop=price]")?.attr("content")

            if (priceText == null) return -1
            val digits = priceText.replace(Regex("[^0-9]"), "")
            if (digits.isEmpty()) return -1
            return digits.toLong()
        }
    }

    companion object {
        fun fetchPriceFromUrlStatic(url: String): Long {
            val client = OkHttpClient()
            val req = Request.Builder().url(url).get().header("User-Agent","Mozilla/5.0").build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return -1
                val body = resp.body?.string() ?: return -1
                val doc = Jsoup.parse(body)
                val priceText = doc.selectFirst("[data-link=price], .price, .price-block, [itemprop=price]")?.text()
                    ?: doc.selectFirst("span.price")?.text()
                    ?: doc.selectFirst("meta[itemprop=price]")?.attr("content")
                if (priceText == null) return -1
                val digits = priceText.replace(Regex("[^0-9]"), "")
                if (digits.isEmpty()) return -1
                return digits.toLong()
            }
        }
    }
}
