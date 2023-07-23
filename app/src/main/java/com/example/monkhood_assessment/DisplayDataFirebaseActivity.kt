package com.example.monkhood_assessment

import ProfileDataAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monkhood_assessment.databinding.ActivityDisplayDataFirebaseBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class DisplayDataFirebaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayDataFirebaseBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayDataFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Retrieve user data from Firebase and display in RecyclerView
        getUserDataFromFirebase()
    }

    private fun getUserDataFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userDataList = mutableListOf<ProfileData>()
                for (dataSnapshot in snapshot.children) {
                    val profileData = dataSnapshot.getValue(ProfileData::class.java)
                    profileData?.let { userDataList.add(it) }
                }

                // Set up RecyclerView and adapter
                val profileDataAdapter = ProfileDataAdapter(userDataList)
                binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this@DisplayDataFirebaseActivity)
                binding.recyclerViewUsers.adapter = profileDataAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if necessary
            }
        })
    }
}
