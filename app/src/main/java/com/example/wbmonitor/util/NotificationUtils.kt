package com.example.wbmonitor.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wbmonitor.data.Product

object NotificationUtils {
    private const val CHANNEL_ID = "wb_monitor_channel"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Price alerts"
            val descriptionText = "Notifications for price drops"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val nm: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    fun showPriceDrop(context: Context, product: Product, oldPrice: Long, newPrice: Long) {
        ensureChannel(context)

        val title = "Цена упала: ${product.title.ifEmpty { "Товар" }}"
        val text = "Было ${formatPrice(oldPrice)}, стало ${formatPrice(newPrice)}"

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(product.id.toInt(), builder.build())
        }
    }

    private fun formatPrice(p: Long): String {
        return p.toString()
    }
}
