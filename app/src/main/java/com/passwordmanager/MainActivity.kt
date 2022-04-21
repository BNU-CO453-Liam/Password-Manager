package com.passwordmanager

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.passwordmanager.adapters.ItemAdapter
import com.passwordmanager.handlers.AccountDbHandler
import com.passwordmanager.handlers.UserDbHandler
import com.passwordmanager.models.AccModelClass
import kotlin.collections.ArrayList

/**
 * Main page of the app where the user is taken after successful login.
 * User can view a list of password accounts
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListofDataIntoRecyclerView()

        // get elements
        val btnLogout = findViewById<Button>(R.id.btn_logout)
        val btnAddNew = findViewById<FloatingActionButton>(R.id.acc_add_new)
        val viewProfile = findViewById<FloatingActionButton>(R.id.viewProfile)

        // Logout user
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        // Add new user
        btnAddNew.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddAccountActivity::class.java))
            finish()
        }

        // View/Edit profile
        viewProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, ViewProfileActivity::class.java))
        }
    }

    /**
     * Function is used to show the list of password accounts in the recycler view.
     */
    private fun setupListofDataIntoRecyclerView() {

        val recycler = findViewById<RecyclerView>(R.id.recycler_view_items)
        val noRecords = findViewById<TextView>(R.id.no_records)

        if (getItemsList().size > 0) {

            recycler.visibility = View.VISIBLE
            noRecords.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            recycler.layoutManager = LinearLayoutManager(this)

            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, getItemsList())

            // Adapter instance is set to the recyclerview to inflate the items.
            recycler.adapter = itemAdapter

            // Set on click event for recycler holder view
            itemAdapter.setOnItemClickListener(object : ItemAdapter.onItemClickListener{
                override fun onItemClick(position: Int) {
                    doIntent(itemAdapter, position)
                }
            })

        } else {

            recycler.visibility = View.GONE
            noRecords.visibility = View.VISIBLE
        }
    }

    /**
     * Create intent for view account
     */
    fun doIntent(adapter: ItemAdapter, Int: Int) {

        val intent = Intent(this, ViewAccountActivity::class.java)
        val id = adapter.items.get(Int).id
        val name = adapter.items.get(Int).accName
        val username = adapter.items.get(Int).username
        val passwd = adapter.items.get(Int).passwd

        intent.putExtra("id", id)
        intent.putExtra("name", name)
        intent.putExtra("username", username)
        intent.putExtra("passwd", passwd)
        startActivity(intent)
    }

    /**
     * Function is used to get the Items List added in the database table.
     */
    private fun getItemsList(): ArrayList<AccModelClass> {

        //creating the instance of DatabaseHandler class
        val databaseHandlerAccount = AccountDbHandler(this)

        // read the records
        val accList: ArrayList<AccModelClass> = databaseHandlerAccount.viewAccount()

        return accList
    }
}


