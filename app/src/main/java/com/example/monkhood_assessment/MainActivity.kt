package com.example.monkhood_assessment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monkhood_assessment.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson

import java.util.*

class MainActivity : AppCompatActivity() {
    private val PREFS_NAME = "MyPrefs"
    private val KEY_NAME = "name"
    private val KEY_PHONE = "phone"
    private val KEY_EMAIL = "email"
    private val KEY_DOB = "dob"

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    companion object {
        private const val REQUEST_IMAGE_PICK = 1
        const val PREFS_NAME = "MyPrefs"
    }
    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val imageViewProfilePic = binding.imageViewProfilePic
        val buttonChooseProfilePic = binding.buttonChooseProfilePic

        // Set up the click listener for the Date of Birth EditText
        binding.editTextDateOfBirth.setOnClickListener {
            showDatePickerDialog()

        }
//select image from galery
        buttonChooseProfilePic.setOnClickListener {
            openGallery()
        }
        loadData()

        // Save Data Button
        binding.buttonSaveData.setOnClickListener {
            saveData()
            saveDataToFirebase()
            val intent = Intent(this, ProfileListActivity::class.java)
            startActivity(intent)
        }

        // Edit Data Button
        binding.buttonEditData.setOnClickListener {
            // Retrieve data from SharedPreferences and set to views
            val intent=Intent(this,DisplayDataFirebaseActivity::class.java)
            startActivity(intent)
        }
    }
    private fun saveData() {
        // Get the data from views
        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhoneNumber.text.toString()
        val email = binding.editTextEmail.text.toString()
        val dob = binding.editTextDateOfBirth.text.toString()

        // Save the data to SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Retrieve existing profile list from SharedPreferences
        val profileListJson = sharedPreferences.getString("PROFILE_LIST", null)
        val profileList: MutableList<ProfileData> =
            if (profileListJson != null) {
                val typeToken = object : TypeToken<List<ProfileData>>() {}.type
                Gson().fromJson(profileListJson, typeToken)
            } else {
                mutableListOf()
            }

        // Add the new profile to the list
        val newProfile = ProfileData(name, phone, email, dob)
        profileList.add(newProfile)

        // Convert the updated list to JSON and store it in SharedPreferences
        val updatedProfileListJson = Gson().toJson(profileList)
        editor.putString("PROFILE_LIST", updatedProfileListJson)
        editor.apply()
    }

    private fun loadData() {
        // Retrieve data from SharedPreferences and set to views
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val name = sharedPreferences.getString(KEY_NAME, "")
        val phone = sharedPreferences.getString(KEY_PHONE, "")
        val email = sharedPreferences.getString(KEY_EMAIL, "")
        val dob = sharedPreferences.getString(KEY_DOB, "")

        binding.editTextName.setText(name)
        binding.editTextPhoneNumber.setText(phone)
        binding.editTextEmail.setText(email)
        binding.editTextDateOfBirth.setText(dob)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            // Handle the selected image from the gallery
            val selectedImageUri = data?.data
            if (selectedImageUri != null) {
                binding.imageViewProfilePic.setImageURI(selectedImageUri)
                // Now you have the image URI, and you can handle it as needed (e.g., upload to Firebase, save to local storage, etc.)
            }
        }
    }

    private fun showDatePickerDialog() {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            // Update the calendar with the selected date
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Update the EditText with the selected date
            updateDateInView()
        }
        DatePickerDialog(
            this,
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.editTextDateOfBirth.setText(sdf.format(calendar.time))
    }

    private fun saveDataToFirebase() {
        val name = binding.editTextName.text.toString()
        val phone = binding.editTextPhoneNumber.text.toString()
        val email = binding.editTextEmail.text.toString()
        val dob = binding.editTextDateOfBirth.text.toString()

database=FirebaseDatabase.getInstance().getReference("Users")
val ProfileData=ProfileData(name,phone,email,dob)
        database.child(name).setValue(ProfileData).addOnSuccessListener {
            binding.editTextName.text.clear()
            binding.editTextPhoneNumber.text.clear()
            binding.editTextEmail.text.clear()
            binding.editTextEmail.text.clear()

            Toast.makeText(this,"Successfully saved",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show()
        }
    }

    }

