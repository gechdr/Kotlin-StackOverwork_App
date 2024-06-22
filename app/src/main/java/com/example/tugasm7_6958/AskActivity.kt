package com.example.tugasm7_6958

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasm7_6958.databinding.ActivityAskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAskBinding
    private lateinit var db: AppDatabase

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val ioMain = CoroutineScope(Dispatchers.Main)

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.build(this)
        username = intent.getStringExtra("username").toString()

        binding.AskBtnAsk.setOnClickListener {
            // Get Data Field
            val title = binding.AskEtTitle.text.toString()
            val detail = binding.AskEtDetail.text.toString()
            val effort = binding.AskEtEffort.text.toString()
            val tags = binding.AskEtTags.text.toString()

            // Check
            if (title.isEmpty()) {
                binding.AskEtTitle.error = "Title is empty"
                Toast.makeText(this, "Title is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (detail.isEmpty()) {
                binding.AskEtDetail.error = "Detail is empty"
                Toast.makeText(this, "Detail is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (effort.isEmpty()) {
                binding.AskEtEffort.error = "Effort is empty"
                Toast.makeText(this, "Effort is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (tags.isEmpty()) {
                binding.AskEtTags.error = "Tags is empty"
                Toast.makeText(this, "Tags is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check Duplicate Tag
            val tagList = tags.split(",")
            if (tagList.size != 2) {
                binding.AskEtTags.error = "2 Tags required"
                Toast.makeText(this, "2 Tags required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tagSet = tagList.toSet()
            if (tagList.size != tagSet.size) {
                binding.AskEtTags.error = "Duplicate tag"
                Toast.makeText(this, "Duplicate tag", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Process
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val current = LocalDateTime.now().format(formatter)

            val ask = QuestionEntity(
                title = title,
                detail = detail,
                effort = effort,
                tags = tags,
                owner = username.toString(),
                date = current
            )

            Log.i("ASK", "Title: $title, Detail: $detail, Effort: $effort, Tags: $tags, Owner: $username, Date: $current")

            ioScope.launch {
                db.questionDao().insert(ask)
                ioMain.launch {
                    Toast.makeText(this@AskActivity, "Question has been asked", Toast.LENGTH_SHORT).show()
                    clear()
                }
            }
        }
    }

    fun clear() {
        binding.AskEtTitle.text.clear()
        binding.AskEtDetail.text.clear()
        binding.AskEtEffort.text.clear()
        binding.AskEtTags.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.miBack -> {
                finish()
                true
            }
            else -> false
        }
    }
}