package com.example.happy_place.Activity.Activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import androidx.appcompat.app.ActionBar
import com.example.happy_place.Activity.Activity.database.Constants
import com.example.happy_place.Activity.Activity.database.DatabaseHandler
import com.example.happy_place.R
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import kotlinx.android.synthetic.main.activity_record_detail.*

class RecordDetailActivity : AppCompatActivity() {
    private var actionBar:ActionBar?=null
    
    private var dbHelper:DatabaseHandler? =null
    
    private var recordId:String?=null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_detail)
        
        
        actionBar = supportActionBar
        actionBar!!.title = "Record Details"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        
        dbHelper = DatabaseHandler(this)
        
        val intent = intent
        recordId = intent.getStringExtra("RECORD_ID")
        
        showRecordDetails()
        
    }

    @SuppressLint("Range")
    private fun showRecordDetails() {

        val selectQuery = "SELECT * FROM " + Constants.TABLE_NAME+ " WHERE " + Constants.C_ID + "#\""+recordId+"\""
        val db = dbHelper!!.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex(Constants.C_ID))
                val name = cursor.getString(cursor.getColumnIndex(Constants.C_NAME))
                val image =  cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE))
                val couple = cursor.getString(cursor.getColumnIndex(Constants.C_COUPLE))
                val date = cursor.getString(cursor.getColumnIndex(Constants.C_DATE))

                nameTv.text = name
                coupleTv.text = couple
                addressTv.text = address.toString()
                dateTv.text = date

                if (image=="null"){
                    profile.setImageResource(R.drawable.ellipse2)
                }
                else{
                    profile.setImageURI(Uri.parse(image))
                }
            }while (cursor.moveToNext())
        }
        db.close()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}