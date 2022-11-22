package com.dicoding.submissionintermediatedicoding.ui.story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.submissionintermediatedicoding.MainActivity
import com.dicoding.submissionintermediatedicoding.R
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences
import com.dicoding.submissionintermediatedicoding.data.remote.api.RetrofitConfig
import com.dicoding.submissionintermediatedicoding.databinding.ActivityUploadStoryBinding
import com.dicoding.submissionintermediatedicoding.ui.map.MapsActivity
import com.dicoding.submissionintermediatedicoding.utils.Constants
import com.dicoding.submissionintermediatedicoding.utils.LocationMaps
import com.dicoding.submissionintermediatedicoding.utils.Status
import com.dicoding.submissionintermediatedicoding.utils.progress.ProgressDialog
import com.dicoding.submissionintermediatedicoding.utils.story.UploadStoryUtilities
import com.dicoding.submissionintermediatedicoding.utils.story.UploadStoryUtilities.reduceFileImage
import com.dicoding.submissionintermediatedicoding.viewmodel.UploadStoryViewModel
import com.dicoding.submissionintermediatedicoding.viewmodel.ViewModelStoryFactory
import com.shashank.sony.fancytoastlib.FancyToast
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var currentPath: String
    private lateinit var usrLoginPref: UserLoginPreferences
    private var getFile: File? = null
    private var _latitude: Double? = null
    private var _longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_story)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usrLoginPref = UserLoginPreferences(this)
        progressDialog = ProgressDialog(this)
        supportActionBar?.title = getString(R.string.upload_story)
        initView()
        initVM()
    }

    private fun initView() {
        binding.apply {
            btnOpenCamera.setOnClickListener { openCamera() }
            btnOpenGallery.setOnClickListener { openGallery() }
            btnUpload.setOnClickListener {
                uploadStory()
            }

            textLocation.setOnClickListener {
                getMyLocationToShare()
            }
        }

    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private val getMyLocLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val lat = it.data?.getDoubleExtra(Constants.LAT, 0.0)
                val lng = it.data?.getDoubleExtra(Constants.LNG, 0.0)
                _latitude = lat
                _longitude = lng
                binding.textLocation.text = LocationMaps.parseAddressLocation(this,
                    lat ?: 0.0, lng?: 0.0)
            }
        }

    private fun getMyLocationToShare() {
        if (!isLocationEnabled()) {
            showLocationNotEnabledDialog()
        } else {
            FancyToast.makeText(
                this@UploadStoryActivity,
                getString(R.string.get_location_data),
                FancyToast.LENGTH_LONG,
                FancyToast.INFO,
                false
            ).show()
            val intent = Intent(this@UploadStoryActivity, MapsActivity::class.java)
            intent.putExtra("UPLOAD_REQUEST_CODE", MY_LOCATION_TO_SHARE)
            getMyLocLauncher.launch(intent)
        }
    }

    private fun showLocationNotEnabledDialog() {
        AlertDialog.Builder(this@UploadStoryActivity).apply {
            setTitle("Enable Location")
            setMessage("Please Enable Location To Share Your Location")
            setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            setNegativeButton("Cancel") { _, _ ->
                FancyToast.makeText(
                    this@UploadStoryActivity,
                    "Location is not enabled",
                    FancyToast.LENGTH_LONG,
                    FancyToast.INFO,
                    false
                ).show()
            }
            create()
            show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                FancyToast.makeText(
                    this,
                    "Permission is not granted",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    false
                ).show()
                finish()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        UploadStoryUtilities.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadStoryActivity,
                "com.dicoding.submissionintermediatedicoding",
                it
            )
            currentPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraIntentLauncher.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }

        val chooser = Intent.createChooser(intent, "Choose Image")
        galleryIntentLauncher.launch(chooser)

    }

    private val cameraIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.imageViewPreview.setImageBitmap(result)
        }
    }

    private val galleryIntentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = UploadStoryUtilities.uriToFile(selectedImg, this@UploadStoryActivity)

            getFile = myFile

            binding.imageViewPreview.setImageURI(selectedImg)
        }
    }


    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadStory() {
        if (getFile != null && binding.editTextAddDescription.text?.isNotEmpty()!!) {
            val file = reduceFileImage(getFile as File)
            val descriptionText = binding.editTextAddDescription.text.toString()
            val description = descriptionText.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val token = "Bearer ${usrLoginPref.getLoginData().token}"
            if(_latitude != null && _longitude != null){
                getAuthViewModel().uploadStory(
                    token,
                    imageMultipart,
                    description,
                    _latitude,
                    _longitude
                )
            } else {
                getAuthViewModel().uploadStory(
                    token,
                    imageMultipart,
                    description,
                    null,
                    null
                )
            }

        } else {
            FancyToast.makeText(
                this@UploadStoryActivity,
                getString(R.string.please_insert_img_desc),
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                false
            ).show()
        }

    }

    private fun initVM(){
        getAuthViewModel().storyUpload.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.startProgressDialog()
                }
                Status.SUCCESS -> {
                    progressDialog.dismissProgressDialog()
                    FancyToast.makeText(
                        this@UploadStoryActivity,
                        getString(R.string.upload_success),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                Status.ERROR -> {
                    progressDialog.dismissProgressDialog()
                    FancyToast.makeText(
                        this@UploadStoryActivity,
                        getString(R.string.upload_failed),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }
                Status.EMPTY -> {
                    progressDialog.dismissProgressDialog()
                }
            }
        }
    }
    private fun getAuthViewModel(): UploadStoryViewModel {
        val viewModel: UploadStoryViewModel by viewModels {
            ViewModelStoryFactory(
                this,
                RetrofitConfig.getApiService()
            )
        }
        return viewModel
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val MY_LOCATION_TO_SHARE = 11

    }
}