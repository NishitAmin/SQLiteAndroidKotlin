package org.sheridan.sqliteandroidkotlindemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDBOpenHelper(
    context: Context?,
    //name: String?,
    factory: SQLiteDatabase.CursorFactory?
    //version: Int,
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object{
        private val version = 1
        private val name = "SQLiteDemo.db"
        val TABLE_NAME = "products"
        val COLUMN_ID = "id"
        val COLUMN_NAME1 = "prodname"
        val COLUMN_NAME2 = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                                    + COLUMN_ID + " INTEGER PRIMARY KEY," +
                                    COLUMN_NAME1 + " TEXT," +
                                    COLUMN_NAME2 + " TEXT" + ")")

        db?.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2:Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    //Inserting a new row to the table
    fun addName(name: Product){
        val values = ContentValues()
        values.put(COLUMN_NAME1, name.prodName)
        values.put(COLUMN_NAME2, name.quantity)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Get all the records from table
    fun getAllName(): Cursor?{
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    //Searching a row from the table
    fun searchProduct(product: String): Product?{
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME1 = \"$product\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var product: Product? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val name = cursor.getString(1)
            val quantity = cursor.getString(2)
            product = Product(name, quantity)
            cursor.close()
        }

        db.close()
        return product
    }

    //Delete a product from the database
    fun deleteProduct(product: String): Boolean {

        var result = false

        val query =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME1 = \"$product\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val prod = cursor.getString(1)
            db.delete(
                TABLE_NAME, COLUMN_NAME1 + " = ?",
                arrayOf(prod))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
}