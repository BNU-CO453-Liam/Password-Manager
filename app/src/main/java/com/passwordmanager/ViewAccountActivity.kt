package com.passwordmanager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.passwordmanager.handlers.AccountDbHandler
import com.passwordmanager.models.AccModelClass

class ViewAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_account)

        val profile = Firebase.auth.currentUser!!.uid

        // Get intent values
        val accId = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val passwd = intent.getStringExtra("passwd")

        // Get view elements
        val backBtn = findViewById<FloatingActionButton>(R.id.add_back_btn)
        val editBtn = findViewById<Button>(R.id.btn_edit_account)
        val deleteBtn = findViewById<Button>(R.id.btn_delete_account)
        val accName = findViewById<TextView>(R.id.tv_view_acc_name)
        val accUsername = findViewById<TextView>(R.id.tv_view_acc_username)
        val accPasswd = findViewById<TextView>(R.id.tv_view_acc_passwd)

        // Set text view values to intent values
        accName.text = "$name"
        accUsername.text = "$username"
        accPasswd.text = "$passwd"

        // Create model
        val accmodel = AccModelClass(accId, name, username, passwd, profile)

        // Click event of back button
        backBtn.setOnClickListener {
            startActivity(Intent(this@ViewAccountActivity, MainActivity::class.java))
            finish()
        }

        // Click event of edit button
        editBtn.setOnClickListener {
            val intent = Intent(this@ViewAccountActivity, EditAccountActivity::class.java)

            intent.putExtra("id", accId)
            intent.putExtra("name", name)
            intent.putExtra("username", username)
            intent.putExtra("passwd", passwd)

            startActivity(intent)
            // flag maybe
        }

        // Click event of delete button
        deleteBtn.setOnClickListener {
            deleteAccount(accmodel)
        }
    }

    /**
     * Method is used to show the Alert Dialog.
     */
    private fun deleteAccount(accModelClass: AccModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${accModelClass.accName}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            //creating the instance of DatabaseHandler class
            val databaseHandlerAccount = AccountDbHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandlerAccount.deleteAccount(
                AccModelClass(
                    accModelClass.id,
                    accModelClass.accName,
                    accModelClass.username,
                    accModelClass.passwd,
                    accModelClass.profile
                )
            )

            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
            }

            // dismiss dialogue
            dialogInterface.dismiss()

            // start new activity and clear old activity of main task
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
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