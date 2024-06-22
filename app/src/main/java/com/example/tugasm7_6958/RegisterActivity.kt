package com.example.tugasm7_6958

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.tugasm7_6958.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: AppDatabase

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val ioMain = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.build(this)

        binding.RegisterBtnRegister.setOnClickListener {
            // Get Data Field
            val username = binding.RegisterEtUsername.text.toString()
            val name = binding.RegisterEtName.text.toString()
            val password = binding.RegisterEtPassword.text.toString()
            val confirmPassword = binding.RegisterEtConfirmPassword.text.toString()
            val phone = binding.RegisterEtPhone.text.toString()

            // Check Field
            if (username.isEmpty()) {
                binding.RegisterEtUsername.error = "Username is required"
                Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if (name.isEmpty()) {
                binding.RegisterEtName.error = "Name is required"
                Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                binding.RegisterEtPassword.error = "Password is required"
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (confirmPassword.isEmpty()) {
                binding.RegisterEtConfirmPassword.error = "Confirm Password is required"
                Toast.makeText(this, "Confirm Password is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (phone.isEmpty()) {
                binding.RegisterEtPhone.error = "Phone is required"
                Toast.makeText(this, "Phone is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password != confirmPassword) {
                binding.RegisterEtConfirmPassword.error = "Password not match"
                Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!phone.isDigitsOnly()) {
                binding.RegisterEtPhone.error = "Phone must be a number"
                Toast.makeText(this, "Phone must be a number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (phone.length != 12) {
                binding.RegisterEtPhone.error = "Phone must be 12 characters"
                Toast.makeText(this, "Phone must be 12 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check User
            ioScope.launch {
                val user = db.userDao().get(username)
                if (user != null) {
                    // Username is already taken
                    ioMain.launch {
                        binding.RegisterEtUsername.error = "Username is already taken"
                        Toast.makeText(this@RegisterActivity, "Username is already taken", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Create New User
                    val newUser = UserEntity(
                        username = username,
                        name = name,
                        password = password,
                        phoneNumber = phone
                    )

                    // Register
                    db.userDao().insert(newUser)
                    ioMain.launch {

                        // Reset Field
                        clear()

                        Toast.makeText(this@RegisterActivity, "Register Success", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

        }

        binding.RegisterBtnLogin.setOnClickListener {
            // Login
            clear()
            finish()
        }
    }

    // Function Reset Field
    fun clear() {
        binding.RegisterEtUsername.text.clear()
        binding.RegisterEtName.text.clear()
        binding.RegisterEtPassword.text.clear()
        binding.RegisterEtConfirmPassword.text.clear()
        binding.RegisterEtPhone.text.clear()
    }
}