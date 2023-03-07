package com.tsu.wordsfactory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
class SignUpActivity : AppCompatActivity() {

    private lateinit var builder: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val nameField = findViewById<EditText>(R.id.editTextTextName)
        val emailField = findViewById<EditText>(R.id.editTextTextEmail)
        val passwordField = findViewById<EditText>(R.id.editTextTextPassword)

        builder = AlertDialog.Builder(this)

        findViewById<Button>(R.id.buttonSignUp).setOnClickListener {
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
                navigateToActivity()
            }
        }

        findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            navigateToIntro()
        }
    }

    //поменять, чтоб переходило на активити с словарем
    private fun navigateToActivity() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }

    private fun navigateToIntro() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
}