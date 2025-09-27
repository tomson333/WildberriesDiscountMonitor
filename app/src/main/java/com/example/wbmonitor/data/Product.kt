package com.example.wbmonitor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val url: String,
    val lastKnownPrice: Long = -1,
    val checkIntervalMinutes: Long = 60
)
