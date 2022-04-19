package com.passwordmanager.handlers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.passwordmanager.models.AccModelClass

// Set up database logic
class AccountDbHandler(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "AccountDatabase"
            private const val TABLE_ACCOUNTS = "AccountTable"

            private const val KEY_ID = "_id"
            private const val KEY_NAME = "name"
            private const val KEY_USERNAME = "username"
            private const val KEY_PASSWD = "password"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            // create table and fields
            val CREATE_ACCOUNTS_TABLE = ("CREATE TABLE " + TABLE_ACCOUNTS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_USERNAME + " TEXT," + KEY_PASSWD + " TEXT)")
            db?.execSQL(CREATE_ACCOUNTS_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, v1: Int, v2: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNTS")
            onCreate(db)
        }

        /**
        * Create an account and add to database
        */
        fun addAccount(acc: AccModelClass): Long {
            val db = this.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(KEY_NAME, acc.accName)
            contentValues.put(KEY_USERNAME, acc.username)
            contentValues.put(KEY_PASSWD, acc.passwd)

            // Insert account details using insert query.
            val success = db.insert(TABLE_ACCOUNTS, null, contentValues)

            // Close database connection
            db.close()
            return success
        }

    /**
     * Read the records from database in form of ArrayList
     */
    @SuppressLint("Range")
    fun viewAccount(): ArrayList<AccModelClass> {

        val accList: ArrayList<AccModelClass> = ArrayList<AccModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_ACCOUNTS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var username: String
        var passwd: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
                passwd = cursor.getString(cursor.getColumnIndex(KEY_PASSWD))

                val acc = AccModelClass(id = id, accName = name, username = username, passwd = passwd)
                accList.add(acc)

            } while (cursor.moveToNext())
        }
        return accList
    }

    /**
     * Update data in database
     */
    fun updateAccount(acc: AccModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, acc.accName)
        contentValues.put(KEY_USERNAME, acc.username)
        contentValues.put(KEY_PASSWD, acc.passwd)

        // update row
        val success = db.update(TABLE_ACCOUNTS, contentValues, KEY_ID + "=" + acc.id, null)
        db.close()
        return success
    }

    /**
     * Delete data from the database
     */
    fun deleteAccount(acc: AccModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, acc.id)

        val success = db.delete(TABLE_ACCOUNTS, KEY_ID + "=" + acc.id, null)

        db.close()
        return success
    }

    /**
     * Delete all accounts
     */
    fun deleteAll() {
        val db = this.writableDatabase
        val removeAccounts = db.execSQL("DELETE FROM $TABLE_ACCOUNTS")
    }
}