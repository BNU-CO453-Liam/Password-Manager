package com.passwordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.passwordmanager.handlers.AccountDbHandler
import com.passwordmanager.models.AccModelClass
import kotlin.random.Random

/**
 * Activity where users can add new password accounts
 */
class AddAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        // Get elements
        val inputAccName = findViewById<EditText>(R.id.et_acc_name)
        val inputAccUserName = findViewById<EditText>(R.id.et_acc_user_name)
        val inputAccPasswd = findViewById<EditText>(R.id.et_acc_passwd)
        val saveBtn = findViewById<Button>(R.id.add_acc_save)
        val backBtn = findViewById<FloatingActionButton>(R.id.add_back_btn)
        val genPasswdBtn = findViewById<Button>(R.id.btn_gen_passwd)

        /**
         * Add new account to database
         */
        fun addRecord() {

            val accName = inputAccName.text.toString().trim()
            val accUserName = inputAccUserName.text.toString().trim()
            var accPasswd = inputAccPasswd.text.toString().trim()

            val databaseHandlerAccount: AccountDbHandler = AccountDbHandler(this)

            if (accName.isNotEmpty() && accUserName.isNotEmpty() && accPasswd.isNotEmpty()) {
                val status =
                    databaseHandlerAccount.addAccount(AccModelClass(0, accName, accUserName, accPasswd))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Account saved", Toast.LENGTH_LONG).show()

                    // start new activity and clear old activity of main task
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Account name, username or password cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Click event of back button
        backBtn.setOnClickListener {
            startActivity(Intent(this@AddAccountActivity, MainActivity::class.java))
            finish()
        }

        // Click event for generate password button
        genPasswdBtn.setOnClickListener {
            val genPasswd = generatePassword() + "-" + generatePassword() + "-" + generatePassword()

            // Set input to generated password
            inputAccPasswd.text = Editable.Factory.getInstance().newEditable(genPasswd)
        }

        // Click event of save button.
        saveBtn.setOnClickListener { view ->
            addRecord()
        }
    }

    /**
     * Generate a password
     */
    private fun generatePassword(): String {
        val characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@£$%^&*()_+"

        val random = Random(System.nanoTime())
        val password = StringBuilder()

        for (i in 0 until 5)
        {
            val rIndex = random.nextInt(characterSet.length)
            password.append(characterSet[rIndex])
        }

        return password.toString()
    }
}