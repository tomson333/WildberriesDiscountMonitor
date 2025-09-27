package com.example.wbmonitor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.wbmonitor.data.Product
import com.example.wbmonitor.repository.ProductRepository
import kotlinx.coroutines.launch

class AddProductActivity : AppCompatActivity() {
    private lateinit var repo: ProductRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        repo = ProductRepository(applicationContext)

        val urlInput = findViewById<EditText>(R.id.urlInput)
        val titleInput = findViewById<EditText>(R.id.titleInput)
        val addButton = findViewById<Button>(R.id.saveButton)

        addButton.setOnClickListener {
            val url = urlInput.text.toString().trim()
            val title = titleInput.text.toString().trim()
            if (url.isNotEmpty()) {
                val p = Product(url = url, title = title)
                lifecycleScope.launch {
                    repo.insert(p)
                    finish()
                }
            }
        }
    }
}
