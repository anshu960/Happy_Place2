package com.example.happy_place.Activity.Activity


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.happy_place.Activity.Activity.database.DatabaseHandler
import com.example.happy_place.Activity.Activity.models.HappyPlaceModel
import com.example.happy_place.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class ContentActivity : AppCompatActivity(), View.OnClickListener {

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var saveImageToInternalStorage : Uri? = null
    private var mLatitude : Double = 0.0
    private var mLongitude : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_happy_place)
        setSupportActionBar(toolbar_add_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_add_place.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        date.setOnClickListener(this)
        tv_add_image.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        updateDateInView()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.date -> {
                DatePickerDialog(
                    this@ContentActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf(
                    "Select photo from Gallery",
                    "Capture photo from camera"
                )
                pictureDialog.setItems(pictureDialogItems) { _, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
            R.id.btn_save -> {
                when{
                    et_name.text.isNullOrEmpty() ->{
                        Toast.makeText(this, "please enter title", Toast.LENGTH_SHORT).show()
                    }
                    et_couple.text.isNullOrEmpty() ->{
                        Toast.makeText(this, "Please enter couple's name", Toast.LENGTH_SHORT).show()
                    }
                    et_address.text.isNullOrEmpty() ->{
                        Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show()
                    }
                    saveImageToInternalStorage == null ->{
                        Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show()
                    }else ->{
                        val happyPlaceModel = HappyPlaceModel(
                            0,
                            et_name.text.toString(),
                            saveImageToInternalStorage.toString(),
                            et_couple.text.toString(),
                            et_date.text.toString(),
                            et_address.text.toString(),
                            mLatitude,
                            mLongitude
                        )
                    val dbHandler = DatabaseHandler(this)
                    val addHappyPlace = dbHandler.addHappyPlace(happyPlaceModel)
                    if (addHappyPlace > 0){
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY){
                if (data != null){
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                       saveImageToInternalStorage =  saveImageToInternalStorage(selectedImageBitmap)
                        Log.e("Saved image: ", "Path :: $saveImageToInternalStorage")
                        iv_place_image.setImageBitmap(selectedImageBitmap)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this@ContentActivity, "Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }else if (requestCode == CAMERA){
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                 saveImageToInternalStorage =  saveImageToInternalStorage(thumbnail)
                Log.e("Saved image: ", "Path :: $saveImageToInternalStorage")
                iv_place_image.setImageBitmap(thumbnail)
            }
        }
    }
    private fun takePhotoFromCamera(){
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    val galleryIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(galleryIntent, CAMERA)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }

        }).onSameThread().check()
    }
    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }

        }).onSameThread().check()

    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permission required for this feature")
            .setPositiveButton("Go to Setting")
            { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }

            }.setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }.show()




    }
    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        et_date.setText(sdf.format(cal.time).toString())
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap):Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
          val stream : OutputStream = FileOutputStream(file)
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
          stream.flush()
          stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }
    companion object{
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "HappyplacesImages"
    }
}






