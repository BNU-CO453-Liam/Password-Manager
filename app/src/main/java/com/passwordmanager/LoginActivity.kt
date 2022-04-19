package com.passwordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.passwordmanager.handlers.UserDbHandler

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /**
         * REMOVE THIS AFTER TESTING
         */
        //val dbHandler: UserDbHandler = UserDbHandler(this)
        //dbHandler.deleteAll()

        // get reference to elements
        val button = findViewById<Button>(R.id.btn_login)
        val entEmail = findViewById<EditText>(R.id.tx_login_email)
        val entPassword = findViewById<EditText>(R.id.tx_login_password)
        val register = findViewById<TextView>(R.id.tv_register)

        // set on click listener
        button.setOnClickListener {
            when {
                TextUtils.isEmpty(entEmail.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(entPassword.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = entEmail.text.toString().trim { it <= ' ' }
                    val password: String = entPassword.text.toString().trim { it <= ' ' }

                    /**
                     * hash password
                     */
                    //val passhash = BCrypt.withDefaults().hashToString(12, password.toCharArray())

                    // Create instance and register user with credentials
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->

                                // If login is successful
                                if (task.isSuccessful) {

                                    // Firebase registered user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@LoginActivity,
                                        "You are logged in successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    /**
                                     * re-direct to the main screen
                                     */

                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)

                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // If login unsuccessful display error
                                    Toast.makeText(
                                        this@LoginActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }


        register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}