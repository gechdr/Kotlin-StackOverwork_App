package com.example.tugasm7_6958

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasm7_6958.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: AppDatabase

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val ioMain = CoroutineScope(Dispatchers.Main)

    private var user: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.build(this)

        binding.LoginBtnLogin.setOnClickListener {
            // Get Data Field
            val username = binding.LoginEtUsername.text.toString()
            val password = binding.LoginEtPassword.text.toString()

            // Check User
            ioScope.launch {
                user = db.userDao().get(username)
                if (user != null) {
                    ioMain.launch {
                        // Check Password
                        if (user!!.password != password) {
                            binding.LoginEtPassword.error = "Password is incorrect"
                            Toast.makeText(this@LoginActivity, "Password is incorrect", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        // Reset Field
                        clear()

                        // Login
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("username", user!!.username)
                        startActivity(intent)
                    }
                } else {
                    // User not Found
                    ioMain.launch {
                        binding.LoginEtUsername.error = "Please check your Username"
                        Toast.makeText(this@LoginActivity, "User not Found!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.LoginBtnRegister.setOnClickListener {
            // Reset Field
            clear()

            // Register
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Function Reset Field
    fun clear() {
        binding.LoginEtUsername.text.clear()
        binding.LoginEtPassword.text.clear()
    }
}