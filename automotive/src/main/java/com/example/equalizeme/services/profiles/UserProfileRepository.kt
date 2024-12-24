package com.example.equalizeme.services.profiles

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.equalizeme.model.EqualizerInfo
import com.example.equalizeme.model.UserProfile
import com.example.equalizeme.model.UserProfileList
import com.example.equalizeme.model.copy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserProfileRepository (
    private val userProfileListDatastore: DataStore<UserProfileList>,
    context: Context
) {
    val userProfilesFlow : Flow<List<UserProfile>> = userProfileListDatastore.data.map { value ->
       value.profilesList
    }

    suspend fun getProfileByIndex(index: Int): UserProfile? {
        return userProfilesFlow.first().elementAtOrNull(index)
    }

    fun getProfileFlowByIndex(index: Int): Flow<UserProfile?> {
        return userProfilesFlow.map { profiles ->
            profiles.elementAtOrNull(index)
        }
    }

    suspend fun createProfile(profile: UserProfile) {
        val profiles = userProfilesFlow.first().toMutableList()
        profiles.add(profile)
        saveProfiles(profiles)
    }

    suspend fun setEqualizerInfo(profileIndex: Int, equalizer: EqualizerInfo) {
        val profiles = userProfilesFlow.first().toMutableList()

            profiles[profileIndex] = profiles[profileIndex].copy { equalizerInfo = equalizer }
            saveProfiles(profiles)
    }

    private suspend fun saveProfiles(profiles: List<UserProfile>) {
        withContext(Dispatchers.IO) {
            userProfileListDatastore.updateData { currentData ->
                currentData.toBuilder().clearProfiles().addAllProfiles(profiles).build()
            }
        }
    }
}