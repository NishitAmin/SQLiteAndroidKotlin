package org.sheridan.sqliteandroidkotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState!=null){
            tvDataBaseContent1.text = savedInstanceState.getString("prods")
            tvDataBaseContent2.text = savedInstanceState.getString("quan")
        }

        //implement button to click to add info to SQLite database file
        btnAddToDB.setOnClickListener{

            var name = etName.text.toString()
            var quantity = etQuantity.text.toString()

            if(name.toString()!=null && name.trim()!="" && quantity.toString()!=null && quantity.trim()!="") {

                //creating an instance of MyDBOpenHandler
                val db = MyDBOpenHelper(this, null)

                val user = Product(etName.text.toString(), etQuantity.text.toString())

                //Inserting a new row to the table
                db.addName(user)

                Toast.makeText(
                    this, etName.text.toString() + " with Quantity " +
                            etQuantity.text.toString() + " added to DB", Toast.LENGTH_LONG
                ).show()

                //clear both the fields
                etName.setText("")
                etQuantity.setText("")

            }else{

            Toast.makeText(this, "Fields cannot be empty, please try again!", Toast.LENGTH_LONG).show()

            }
        }

        //implement the button to show data from database
        btnShowDataFromDb.setOnClickListener {

            //clear data output fields
            tvDataBaseContent1.setText("")
            tvDataBaseContent2.setText("")

            //create an instance of MyDBOpenHandler
            var db = MyDBOpenHelper(this, null)

            // call a query to get data from DB
            var cursor = db.getAllName()

            cursor!!.moveToFirst()

            //append the text O/P with the prod name
            tvDataBaseContent1.append(
                (cursor.getString(
                    cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME1)
                ))
            )
            tvDataBaseContent1.append("\n")

            //append the text O/P with the quantity
            tvDataBaseContent2.append(
                (cursor.getString(
                    cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME2)
                ))
            )
            tvDataBaseContent2.append("\n")

            //looping inside query result returned by cursor
            while (cursor.moveToNext()){

                //add product name
                tvDataBaseContent1.append(
                    cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME1)
                    ))
                tvDataBaseContent1.append("\n")

                //add product quantity
                tvDataBaseContent2.append(
                    cursor.getString(
                        cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME2)
                    ))
                tvDataBaseContent2.append("\n")
            }
            cursor.close()
        }

        btnSearch.setOnClickListener {

            //clear data output fields
            tvDataBaseContent1.setText("")
            tvDataBaseContent2.setText("")

            //create an instance of MyDBOpenHandler
            var db = MyDBOpenHelper(this, null)

            var product = etSearch.text.toString()

            // call a query to get data from DB
            var cursor = db.searchProduct(product)

            if(cursor?.prodName != null) {

                tvDataBaseContent1.setText(cursor?.prodName.toString())
                tvDataBaseContent2.setText(cursor?.quantity.toString())
                Toast.makeText(this, "Product: " + product + " found", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Product not found", Toast.LENGTH_LONG).show()
            }
        }

        btnDelete.setOnClickListener {

            //create an instance of the MyDbOpenHandler
            val db = MyDBOpenHelper(this, null)

            var product = etSearch.text.toString()

            //Storing the result into a variable
            val result = db.deleteProduct(product)

            if (result) {
                tvDataBaseContent1.setText("")
                tvDataBaseContent2.setText("")
                etSearch.setText("")
                Toast.makeText(this, "Product: " + product + " deleted", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(this, "Product not found", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
            savedInstanceState.putString("prods", tvDataBaseContent1.text.toString())
            savedInstanceState.putString("quan", tvDataBaseContent2.text.toString())
    }
}
