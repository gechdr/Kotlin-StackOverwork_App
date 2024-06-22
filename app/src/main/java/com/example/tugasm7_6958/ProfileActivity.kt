package com.example.tugasm7_6958

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasm7_6958.databinding.ActivityProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var db: AppDatabase

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val ioMain = CoroutineScope(Dispatchers.Main)

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var username:String
    private var user: UserEntity? = null
    private lateinit var questionList: MutableList<QuestionEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.build(this)
        username = intent.getStringExtra("username").toString()

        questionList = mutableListOf()

        binding.ProfileRvQuestion.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        questionAdapter = QuestionAdapter(questionList)
        binding.ProfileRvQuestion.adapter = questionAdapter

        ioScope.launch {
            user = db.userDao().get(username)

            val tempQuestions = db.questionDao().getByOwner(username)
            questionList.clear()
            if (tempQuestions != null) {
                questionList.addAll(tempQuestions)
            }

            ioMain.launch {
                Toast.makeText(this@ProfileActivity, "Welcome, ${user!!.name}", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this@ProfileActivity, "Question: ${questionList.size}", Toast.LENGTH_SHORT).show()

                binding.ProfileTvUser.text = user!!.name
                binding.ProfileTvCount.text = "${questionList.size} question(s)"

                questionAdapter.notifyDataSetChanged()
            }
        }

        questionAdapter.onDetailClickListener = {
            val intent = Intent(this@ProfileActivity, DetailActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("questionId", it.id.toString())
            startActivity(intent)
        }

        binding.ProfileEtSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val curString = s.toString()

                if (curString.isEmpty()){
                    ioScope.launch {
                        val tempQuestions = db.questionDao().getByOwner(username)
                        questionList.clear()
                        if (tempQuestions != null){
                            questionList.addAll(tempQuestions)
                        }

                        ioMain.launch {
                            questionAdapter.notifyDataSetChanged()
                        }
                    }
                    return
                } else {
                    ioScope.launch {
                        val newListQuestion = db.questionDao().searchByOwner(username, curString)

                        questionList.clear()
                        if (newListQuestion != null){
                            questionList.addAll(newListQuestion)
                        }

                        ioMain.launch {
                            questionAdapter.notifyDataSetChanged()
                        }
                    }
                    return
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        ioScope.launch {
            val tempQuestions = db.questionDao().getByOwner(username)
            questionList.clear()
            if (tempQuestions != null){
                questionList.addAll(tempQuestions)
            }

            ioMain.launch {
                questionAdapter.notifyDataSetChanged()
            }
        }
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