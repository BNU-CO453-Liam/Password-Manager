package com.passwordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.passwordmanager.handlers.AccountDbHandler
import com.passwordmanager.models.AccModelClass

class EditAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        // Get elements
        val accName = findViewById<EditText>(R.id.et_edit_acc_name)
        val accUsername = findViewById<EditText>(R.id.et_edit_acc_username)
        val accPasswd = findViewById<EditText>(R.id.et_edit_acc_passwd)

        // Get intent values
        val accId = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val passwd = intent.getStringExtra("passwd")

        // Get view elements
        val backBtn = findViewById<FloatingActionButton>(R.id.edit_back_btn)
        val updateBtn = findViewById<Button>(R.id.btn_update_account)

        // Set element values
        accName.hint = "$name"
        accUsername.hint = "$username"
        accPasswd.hint = "$passwd"

        // Create model
        val accmodel = AccModelClass(accId, name, username, passwd)

        // Click event of back button
        backBtn.setOnClickListener {
            finish()
        }

        // Click event of update button
        updateBtn.setOnClickListener {
            updateAccount(accmodel)
        }
    }

    /**
     * Update account
     */
    private fun updateAccount(accModelClass: AccModelClass) {

        // get intent values
        val accId = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val passwd = intent.getStringExtra("passwd")

        // get elements
        val accName = findViewById<EditText>(R.id.et_edit_acc_name)
        val accUsername = findViewById<EditText>(R.id.et_edit_acc_username)
        val accPasswd = findViewById<EditText>(R.id.et_edit_acc_passwd)

        // set values of user input
        var newAccName = accName.text.toString().trim()
        var newUsername = accUsername.text.toString().trim()
        var newPasswd = accPasswd.text.toString().trim()

        // set database handler
        val databaseHandlerAccount = AccountDbHandler(this)

        /**
         * keep original values if null or blank
         */
        if (newAccName.isNullOrBlank()) {
            newAccName = name!!
        }

        if (newUsername.isNullOrBlank()) {
            newUsername = username!!
        }

        if (newPasswd.isNullOrBlank()) {
            newPasswd = passwd!!
        }

        // create model
        val accModel = AccModelClass(id = accId, accName = newAccName, username = newUsername, passwd = newPasswd)

        // if fields are not empty then update account
        val status = databaseHandlerAccount.updateAccount(accModel)

        if (status > -1) {
            Toast.makeText(applicationContext, "Account updated", Toast.LENGTH_SHORT).show()

            // re-direct to login and clear previous activities
            val intent = Intent(this@EditAccountActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                applicationContext, "Ensure fields are not blank",
                Toast.LENGTH_LONG
            ).show()
        }

    }
}