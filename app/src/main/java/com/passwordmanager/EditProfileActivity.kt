package com.passwordmanager

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.passwordmanager.handlers.AccountDbHandler
import com.passwordmanager.handlers.UserDbHandler
import com.passwordmanager.models.AccModelClass
import com.passwordmanager.models.UserModelClass

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val etProfileEmail = findViewById<EditText>(R.id.et_profile_email)
        val etPassword = findViewById<EditText>(R.id.et_profile_passwd)

        val email = intent.getStringExtra("username")
        val passwd = intent.getStringExtra("passwd")

        val backBtn = findViewById<FloatingActionButton>(R.id.back_btn)
        val updateBtn = findViewById<Button>(R.id.btn_update_profile)
        val deleteBtn = findViewById<Button>(R.id.btn_delete_profile)

        // set field hints
        etProfileEmail.hint = email
        etPassword.hint = passwd

        // Create user model
        val userModel = UserModelClass(1, email!!, passwd!!)

        // Click event of back button
            backBtn.setOnClickListener {
            startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
            finish()
        }

        // Click event of update button
        updateBtn.setOnClickListener {
            val localUpdate = updateProfile(userModel)
            val cloudUpdate = updateFire()
        }

        // Click event of delete button
        deleteBtn.setOnClickListener {
            val databaseHandlerProfile = UserDbHandler(this)
            val databaseHandlerAccount = AccountDbHandler(this)

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Delete Record")
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

                Toast.makeText(
                    applicationContext,
                    "Profile deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                // dismiss dialogue
                dialogInterface.dismiss()

                // sign out user
                FirebaseAuth.getInstance().signOut()

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
    }

    private fun updateFire() {

        // get elements
        val etProfileEmail = findViewById<EditText>(R.id.et_profile_email)
        val etPassword = findViewById<EditText>(R.id.et_profile_passwd)

        // set text of elements
        val newEmail = etProfileEmail.text.toString().trim { it <= ' ' }
        val newPasswd = etPassword.text.toString().trim { it <= ' ' }

        val user = Firebase.auth.currentUser
        //val newPassword = "$newPasswd"
        //val newerEmail = "$newEmail"

        // try by email reset
        //user.updatePassword(newPasswd)

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

        // This will not work in conjunction with email update
        //user!!.updatePassword("$newPassword")
        //    .addOnSuccessListener {
        //    }
    }

    /**
     * Update profile
     */
    private fun updateProfile(UserModelClass: UserModelClass) {

        // get elements
        val etProfileEmail = findViewById<EditText>(R.id.et_profile_email)
        val etPassword = findViewById<EditText>(R.id.et_profile_passwd)

        // set values of text input
        val newEmail = etProfileEmail.text.toString().trim { it <= ' ' }
        val newPasswd = etPassword.text.toString().trim { it <= ' ' }

        val databaseHandlerProfile: UserDbHandler = UserDbHandler(this)

        // if text input is not empty, update user profile
        if (newEmail.isNotEmpty() && newPasswd.isNotEmpty()) {
            val status = databaseHandlerProfile.updateProfile(
                UserModelClass(
                    1,
                    newEmail,
                    newPasswd
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