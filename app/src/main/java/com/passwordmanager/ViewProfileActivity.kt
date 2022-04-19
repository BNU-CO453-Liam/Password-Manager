package com.passwordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.passwordmanager.handlers.UserDbHandler

class ViewProfileActivity : AppCompatActivity() {

    var id = 0
    var email = ""
    var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        /**
         * REMOVE THIS AFTER TESTING
         */
        //val dbHandler: UserDbHandler = UserDbHandler(this)
        //dbHandler.deleteAll()

        getInfo()

        val backBtn = findViewById<FloatingActionButton>(R.id.back_btn)
        val editBtn = findViewById<Button>(R.id.btn_edit_profile)
        val userEmail = findViewById<TextView>(R.id.et_profile_email)
        val passwd = findViewById<TextView>(R.id.et_profile_passwd)

        userEmail.text = email
        passwd.text = "00000000"

        // Click event of back button
        backBtn.setOnClickListener {
            startActivity(Intent(this@ViewProfileActivity, MainActivity::class.java))
            finish()
        }

        // Click event of edit profile button
        editBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
                .setMessage("Are you sure you want to edit your profile?")
                .setPositiveButton("Yes") {d,e->
                    intent = Intent(this@ViewProfileActivity, EditProfileActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("username", email)
                    intent.putExtra("passwd", pass)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel") {d,e ->
                    d.dismiss()
                }
                .show()
        }
    }

    private fun sendEmailVerification() {

        val firebaseAuth = FirebaseAuth.getInstance()

        val user = firebaseAuth.currentUser
        user!!.sendEmailVerification()
            .addOnSuccessListener {
                if (user.isEmailVerified) {
                    intent = Intent(this@ViewProfileActivity, EditProfileActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("username", email)
                    intent.putExtra("passwd", pass)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@ViewProfileActivity,
                    "Verification unsuccessful",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    /**
     * Get profile information
     */
    private fun getInfo() {
        val dbHandler: UserDbHandler = UserDbHandler(this)

        val details = dbHandler.viewProfile()

        id = details[0].id
        email = details[0].username
        pass = details[0].passwd
    }
}