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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.passwordmanager.handlers.UserDbHandler

/**
 * Activity where users can view their profile.
 */
class ViewProfileActivity : AppCompatActivity() {

    var id = 0
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        /**
         * REMOVE THIS AFTER TESTING
         */
        val dbHandler: UserDbHandler = UserDbHandler(this)
        dbHandler.deleteAll()

        getInfo()

        // get elements
        val backBtn = findViewById<FloatingActionButton>(R.id.back_btn)
        val editBtn = findViewById<Button>(R.id.btn_edit_profile)
        val userEmail = findViewById<TextView>(R.id.et_profile_email)
        val passwd = findViewById<TextView>(R.id.tv_profile_passwd)

        // set text of elements
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
                .setPositiveButton("Yes") { _, _ ->
                    intent = Intent(this@ViewProfileActivity, EditProfileActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("username", email)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel") { d, _ ->
                    d.dismiss()
                }
                .show()
        }
    }

    /**
     * Get profile information
     */
    private fun getInfo() {
        val dbHandler = UserDbHandler(this)

        val details = dbHandler.viewProfile()

        if (details.count() > 0) {
            id = details[0].id
            email = details[0].username
        }
    }
}