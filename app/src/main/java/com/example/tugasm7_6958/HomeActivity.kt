package com.example.tugasm7_6958

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasm7_6958.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: AppDatabase

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val ioMain = CoroutineScope(Dispatchers.Main)

    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var username:String
    private var user: UserEntity? = null
    private lateinit var questionList: MutableList<QuestionEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.build(this)
        username = intent.getStringExtra("username").toString()

        questionList = mutableListOf()

        binding.HomeRvQuestion.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        questionAdapter = QuestionAdapter(questionList)
        binding.HomeRvQuestion.adapter = questionAdapter

        ioScope.launch {
            user = db.userDao().get(username)

            val tempQuestions = db.questionDao().fetch()
            questionList.clear()
            questionList.addAll(tempQuestions)

            ioMain.launch {
                Toast.makeText(this@HomeActivity, "Welcome, ${user!!.name}", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this@HomeActivity, "Question: ${questionList.size}", Toast.LENGTH_SHORT).show()
                questionAdapter.notifyDataSetChanged()
            }
        }

        questionAdapter.onDetailClickListener = {
            val intent = Intent(this@HomeActivity, DetailActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("questionId", it.id.toString())
            startActivity(intent)
        }

        binding.HomeEtSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val curString = s.toString()

                if (curString.isEmpty()){
                    ioScope.launch {
                        val tempQuestions = db.questionDao().fetch()
                        questionList.clear()
                        questionList.addAll(tempQuestions)

                        ioMain.launch {
                            questionAdapter.notifyDataSetChanged()
                        }
                    }
                    return
                } else {
                    ioScope.launch {
                        val newListQuestion = db.questionDao().search(curString)

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

        binding.HomeBtnLogout.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        ioScope.launch {
            val tempQuestions = db.questionDao().fetch()
            questionList.clear()
            questionList.addAll(tempQuestions)

            ioMain.launch {
                questionAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.miAsk -> {
                val intent = Intent(this@HomeActivity, AskActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                true
            }
            R.id.miProfile -> {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
}