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

        val accName = findViewById<EditText>(R.id.et_edit_acc_username)
        val accUsername = findViewById<EditText>(R.id.et_edit_acc_username)
        val accPasswd = findViewById<EditText>(R.id.et_edit_acc_passwd)

        val newAccName = accName.text.toString().trim()
        val newUsername = accUsername.text.toString().trim()
        val newPasswd = accPasswd.text.toString().trim()

        val databaseHandlerAccount: AccountDbHandler = AccountDbHandler(this)

        if (newAccName.isNotEmpty() && newUsername.isNotEmpty() && newPasswd.isNotEmpty()) {
            val status = databaseHandlerAccount.updateAccount(
                AccModelClass(
                    accModelClass.id,
                    newAccName,
                    newUsername,
                    newPasswd
                )
            )

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
}