package com.tsu.wordsfactory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tsu.wordsfactory.databinding.ActivityMainBinding
import com.tsu.wordsfactory.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var builder: AlertDialog.Builder
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameField = binding.editTextTextName
        val emailField = binding.editTextTextEmail
        val passwordField = binding.editTextTextPassword

        builder = AlertDialog.Builder(this)


        binding.buttonSignUp.setOnClickListener {
            if (nameField.text.toString().trim().isEmpty() ||
                emailField.text.toString().trim().isEmpty() ||
                passwordField.text.toString().trim().isEmpty()) {
                    builder.setTitle("Error").
                            setMessage("Fill in the required fields").
                            setCancelable(true).
                            setPositiveButton("Ok") { dialog, id ->
                                Toast.makeText(this, "Error", Toast.LENGTH_SHORT)
                            }.show()
            } else {
                navigateToDictionary()
            }
        }

        binding.buttonBack.setOnClickListener {
            navigateToIntro()
        }
    }
    private fun navigateToDictionary() {
        startActivity(Intent(applicationContext, BottomNavigateActivity::class.java))
        finish()
    }
    private fun navigateToIntro() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
}