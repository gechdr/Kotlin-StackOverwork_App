package com.example.tugasm7_6958

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasm7_6958.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val ioMain = CoroutineScope(Dispatchers.Main)

    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var username:String
    private var user: UserEntity? = null
    private lateinit var idQuestion:String
    private var question: QuestionEntity? = null
    private lateinit var answerList: MutableList<AnswerEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.build(this)
        username = intent.getStringExtra("username").toString()
        idQuestion = intent.getStringExtra("questionId").toString()

        answerList = mutableListOf()

        binding.DetailRvAnswer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        answerAdapter = AnswerAdapter(answerList)
        binding.DetailRvAnswer.adapter = answerAdapter

        ioScope.launch {
            user = db.userDao().get(username)
            question = db.questionDao().get(idQuestion.toInt())

            val tempAnswers = db.answerDao().getByQuestion(idQuestion.toInt())
            answerList.clear()
            if (tempAnswers != null){
                answerList.addAll(tempAnswers)
            }

            ioMain.launch {
                binding.DetailTvTitle.text = question!!.title
                binding.DetailTvUser.text = question!!.owner
                binding.DetailTvDate.text = question!!.date
                binding.DetailTvDetail.text = question!!.detail
                binding.DetailTvEffort.text = question!!.effort

                val tags = question!!.tags.split(",")
                binding.DetailTvTag1.text = tags[0]
                binding.DetailTvTag2.text = tags[1]

                answerAdapter.notifyDataSetChanged()
            }
        }

        binding.DetailBtnSend.setOnClickListener {
            val answer = binding.DetailEtAnswer.text.toString()

            // Check Answer
            if (answer.isEmpty()) {
                binding.DetailEtAnswer.error = "Answer must be filled"
                Toast.makeText(this, "Answer must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newAnswer = AnswerEntity(
                idQuestion = idQuestion.toInt(),
                answer = answer,
                owner = username
            )

            val updateQuestion = QuestionEntity(
                id = idQuestion.toInt(),
                title = question!!.title,
                detail = question!!.detail,
                tags = question!!.tags,
                owner = question!!.owner,
                date = question!!.date,
                effort = question!!.effort,
                answers = question!!.answers + 1
            )

            ioScope.launch {
                db.answerDao().insert(newAnswer)
                db.questionDao().update(updateQuestion)

                val tempAnswers = db.answerDao().getByQuestion(idQuestion.toInt())
                answerList.clear()
                if (tempAnswers != null) {
                    answerList.addAll(tempAnswers)
                }

                ioMain.launch {
                    binding.DetailEtAnswer.text.clear()
                    answerAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        ioScope.launch {
            val tempAnswers = db.answerDao().getByQuestion(idQuestion.toInt())
            answerList.clear()
            if (tempAnswers != null){
                answerList.addAll(tempAnswers)
            }

            ioMain.launch {
                answerAdapter.notifyDataSetChanged()
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