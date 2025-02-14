package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {


    private val correctPassword = "secret123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // references to the UI components
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)


        loginButton.setOnClickListener { view ->
            val username = usernameEditText.text.toString() // if you need it later
            val password = passwordEditText.text.toString()

            if (password == correctPassword) {
                // can't test toast for some reason, changing to snackbar.
                //Toast.makeText(this, "Login successful! Welcome back comrade", Toast.LENGTH_SHORT).show()
                Snackbar.make(view,"Login successful! Welcome back comrade", Snackbar.LENGTH_SHORT).show()
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
                finish()  // Optionally finish the login activity so the user can’t go back to it
            }
            else if(username == correctPassword){
                Snackbar.make(view, "blyat check the field!", Snackbar.LENGTH_SHORT).show()
                //Toast.makeText(this, "blyat check the field!", Toast.LENGTH_SHORT).show()
            }
            else {
                Snackbar.make(view, "Invalid password", Snackbar.LENGTH_SHORT).show()
                //Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
