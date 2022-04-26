package com.passwordmanager

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.passwordmanager.handlers.AccountDbHandler
import com.passwordmanager.handlers.UserDbHandler
import com.passwordmanager.models.UserModelClass

/**
 * Activity where users can edit their profile (email address),
 * delete their profile, and send an email to reset password
 */
class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // get values from intent
        val email = intent.getStringExtra("username")

        // get elements
        val etProfileEmail = findViewById<EditText>(R.id.et_profile_email)
        val etPassword = findViewById<TextView>(R.id.tv_profile_passwd)
        val backBtn = findViewById<FloatingActionButton>(R.id.back_btn)
        val updateBtn = findViewById<Button>(R.id.btn_update_profile)
        val deleteBtn = findViewById<Button>(R.id.btn_delete_profile)
        val resetPassword = findViewById<TextView>(R.id.reset_password)

        // set field hints
        etProfileEmail.hint = email

        // Create user model
        val userModel = UserModelClass(1, email!!)

        // Click event of back button
            backBtn.setOnClickListener {
            startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
            finish()
        }

        // Click event of update button
        updateBtn.setOnClickListener {
            updateProfile(userModel)
            updateFirebase()
        }

        // Click event of delete button
        deleteBtn.setOnClickListener {

            // profile database handler
            val databaseHandlerProfile = UserDbHandler(this)

            // account database handler
            val databaseHandlerAccount = AccountDbHandler(this)

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Delete Profile")
            //set message for alert dialog
            builder.setMessage("Are you sure you wants to delete your profile?" +
                    "All passwords will be deleted.")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, _ ->

                // drop and re-create user table
                databaseHandlerProfile.reCreateTable()

                // delete password accounts
                databaseHandlerAccount.deleteAll()

                // dismiss dialogue
                dialogInterface.dismiss()

                // delete firebase user
                deleteFirebase()

                // re direct to login
                startActivity(Intent(this@EditProfileActivity, LoginActivity::class.java))
                finish()
            }
                //perform negative action
                builder.setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.dismiss() // Dialog will be dismissed
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
                alertDialog.show()
        }

        // Click event of password reset button
        resetPassword.setOnClickListener {
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "An email has been sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    /**
     * delete firebase user
     */
    private fun deleteFirebase() {
        val user = Firebase.auth.currentUser

        user!!.delete()
            .addOnSuccessListener {
                    // show message
                    Toast.makeText(
                        applicationContext,
                        "Profile deleted successfully.",
                        Toast.LENGTH_LONG
                    ).show()

            }
    }

    /**
     * Update firebase email
     */
    private fun updateFirebase() {

        // get elements
        val etProfileEmail = findViewById<EditText>(R.id.et_profile_email)

        // set text of elements
        val newEmail = etProfileEmail.text.toString().trim { it <= ' ' }

        // get current firebase user
        val user = Firebase.auth.currentUser

        // update firebase user email
        user!!.updateEmail("$newEmail")
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Profile updated",
                    Toast.LENGTH_SHORT)
                    .show()

                // sign out user
                FirebaseAuth.getInstance().signOut()

                // re-direct to login and clear previous activities
                val intent = Intent(this@EditProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
    }

    /**
     * Update local profile info
     */
    private fun updateProfile(UserModelClass: UserModelClass) {

        // get elements
        val etProfileEmail = findViewById<EditText>(R.id.et_profile_email)

        // set values of text input
        val newEmail = etProfileEmail.text.toString().trim { it <= ' ' }

        val databaseHandlerProfile = UserDbHandler(this)

        // if text input is not empty, update user profile
        if (newEmail.isNotEmpty()) {
            val status = databaseHandlerProfile.updateProfile(
                UserModelClass(
                    1,
                    newEmail
                )
            )
            // display message
            Toast.makeText(
                applicationContext, "Account updated",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}