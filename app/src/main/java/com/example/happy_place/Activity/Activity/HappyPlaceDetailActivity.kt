package com.example.happy_place.Activity.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happy_place.Activity.Activity.models.HappyPlaceModel
import com.example.happy_place.R
import kotlinx.android.synthetic.main.activity_happy_place_detail.*

class HappyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_place_detail)

        var happyPlaceDetailModel : HappyPlaceModel ?= null
        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
           happyPlaceDetailModel =
               intent.getSerializableExtra(
                   MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }
        if (happyPlaceDetailModel!= null){
            setSupportActionBar(toolbar_happy_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title

            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }
            iv_image.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            tv_name.text = happyPlaceDetailModel.title
            tv_description.text = happyPlaceDetailModel.description
            tv_address.text = happyPlaceDetailModel.location
            tv_date.text = happyPlaceDetailModel.date
        }
    }
}