package com.passwordmanager

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.passwordmanager.handlers.UserDbHandler
import com.passwordmanager.models.UserModelClass


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val databaseHandler = UserDbHandler(this)

        // get reference to elements
        val button = findViewById<Button>(R.id.btn_register)
        val entEmail = findViewById<EditText>(R.id.tx_register_email)
        val entPassword = findViewById<EditText>(R.id.tx_register_password)
        val login = findViewById<TextView>(R.id.tv_login)

        // set on click listener for register button
        button.setOnClickListener {
            when {
                TextUtils.isEmpty(entEmail.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(entPassword.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    addProfile()
                    registerUser()
                }
            }
        }

        // set on click listener to login text
        login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    /**
     * Create instance and register user with credentials to online database
     */
    private fun registerUser() {

        // get elements
        val entEmail = findViewById<EditText>(R.id.tx_register_email)
        val entPassword = findViewById<EditText>(R.id.tx_register_password)

        // set values to user input
        val email: String = entEmail.text.toString().trim { it <= ' ' }
        val password: String = entPassword.text.toString().trim { it <= ' ' }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->

                    // If registration is successful
                    if (task.isSuccessful) {

                        // Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        // send email verification
                        firebaseUser.sendEmailVerification()

                        Toast.makeText(
                            this@RegisterActivity,
                            "You have been registered successfully.",
                            Toast.LENGTH_SHORT
                        ).show()


                        // Re-direct to the main screen with user id and email used for registration
                        //val intent = Intent(this@RegisterActivity, LoginActivity::class.java)

                        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        //intent.putExtra("user_id", firebaseUser.uid)
                        //intent.putExtra("email_id", email)
                        //startActivity(intent)
                        finish()
                    } else {
                        // If register unsuccessful display error
                        Toast.makeText(
                            this@RegisterActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

    /**
     * Add user profile to local database
     */
    private fun addProfile() {

        val databaseHandler = UserDbHandler(this)

        // get elements
        val entEmail = findViewById<EditText>(R.id.tx_register_email)
        val entPassword = findViewById<EditText>(R.id.tx_register_password)

        // set values to user input
        val profileUserName = entEmail.text.toString().trim()
        var profilePasswd = entPassword.text.toString().trim()

        // if fields are not empty, add profile to local database
        if (profileUserName.isNotEmpty() && profilePasswd.isNotEmpty()) {
            val status =
                databaseHandler.addProfile(UserModelClass(0,profileUserName))
            if (status > -1) {

                // start new activity and clear old activity of main task
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        } else {
            // show message
            Toast.makeText(
                applicationContext,
                "Email or password cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
