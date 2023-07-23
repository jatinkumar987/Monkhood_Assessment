package com.example.monkhood_assessment
import ProfileDataAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monkhood_assessment.MainActivity.Companion.PREFS_NAME
import com.example.monkhood_assessment.databinding.ActivityProfileListBinding
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class ProfileListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileListBinding
    private lateinit var profileDataAdapter: ProfileDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileDataAdapter = ProfileDataAdapter(getProfileDataFromSharedPreferences())
        binding.recyclerViewProfiles.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProfiles.adapter = profileDataAdapter
    }

    private fun getProfileDataFromSharedPreferences(): List<ProfileData> {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val profileListJson = sharedPreferences.getString("PROFILE_LIST", null)

        return if (profileListJson != null) {
            val typeToken = object : TypeToken<List<ProfileData>>() {}.type
            Gson().fromJson(profileListJson, typeToken)
        } else {
            mutableListOf()
        }
    }
}
