package com.example.equalizeme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.equalizeme.model.UserProfileList
import com.example.equalizeme.services.profiles.UserProfileListSerializer
import com.example.equalizeme.services.profiles.UserProfileRepository

private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

private val Context.userProfileListStore: DataStore<UserProfileList> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserProfileListSerializer,
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the user profile Repository
        UserProfileRepository(
            this.baseContext.userProfileListStore,
            this.baseContext,
        )
    }
}