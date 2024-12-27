package com.example.equalizeme.services.profiles

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.equalizeme.model.EqualizerInfo
import com.example.equalizeme.model.UserProfile
import com.example.equalizeme.model.UserProfileList
import com.example.equalizeme.model.copy
import com.example.equalizeme.model.equalizerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

private val Context.userProfileListStore: DataStore<UserProfileList> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = UserProfileListSerializer,
)

class UserProfileRepository (
    context: Context
) {
    private val userProfileListDatastore: DataStore<UserProfileList> = context.userProfileListStore

    /**
     * A flow of the list of user profiles.
     */
    val userProfilesFlow : Flow<List<UserProfile>> = userProfileListDatastore.data.map { value ->
       value.profilesList
    }

    /**
     * Returns the user profile at the given index.
     */
    suspend fun getProfileByIndex(index: Int): UserProfile? {
        return userProfilesFlow.first().elementAtOrNull(index)
    }

    /**
     * Returns a flow of the user profile at the given index.
     */
    fun getProfileFlowByIndex(index: Int): Flow<UserProfile?> {
        return userProfilesFlow.map { profiles ->
            profiles.elementAtOrNull(index)
        }
    }

    /**
     * Creates a new user profile with the given name and adds it to the repository.
     */
    suspend fun createProfile(profile: UserProfile) {
        val profiles = userProfilesFlow.first().toMutableList()
        profiles.add(profile)
        saveProfiles(profiles)
    }

    /**
     * Set a new equalizer for the user profile at the given index.
     */
    suspend fun setEqualizerInfo(profileIndex: Int, equalizer: EqualizerInfo) {
        val profiles = userProfilesFlow.first().toMutableList()

            profiles[profileIndex] = profiles[profileIndex].copy { equalizerInfo = equalizer }
            saveProfiles(profiles)
    }

    /**
     * Set a new bass value for the user profile at the given index.
     */
    suspend fun setBass(profileIndex: Int, newBass: Int) {
        val profiles = userProfilesFlow.first().toMutableList()
        val equalizer = equalizerInfo {
            this.bass = newBass
            this.mid = profiles[profileIndex].equalizerInfo.mid
            this.treble = profiles[profileIndex].equalizerInfo.treble}
        profiles[profileIndex] = profiles[profileIndex].copy {
            equalizerInfo = equalizer
        }
        saveProfiles(profiles)
    }

    /**
     * Set a new midrange value for the user profile at the given index.
     */
    suspend fun setMid(profileIndex: Int, newMid: Int) {
        val profiles = userProfilesFlow.first().toMutableList()
        val equalizer = equalizerInfo {
            this.bass = profiles[profileIndex].equalizerInfo.bass
            this.mid = newMid
            this.treble = profiles[profileIndex].equalizerInfo.treble}
        profiles[profileIndex] = profiles[profileIndex].copy {
            equalizerInfo = equalizer
        }
        saveProfiles(profiles)
    }

    /**
     * Set a new treble value for the user profile at the given index.
     */
    suspend fun setTreble(profileIndex: Int, newTreble: Int) {
        val profiles = userProfilesFlow.first().toMutableList()
        val equalizer = equalizerInfo {
            this.bass = profiles[profileIndex].equalizerInfo.bass
            this.mid = profiles[profileIndex].equalizerInfo.mid
            this.treble = newTreble}
        profiles[profileIndex] = profiles[profileIndex].copy {
            equalizerInfo = equalizer
        }
        saveProfiles(profiles)
    }

    /**
     * Save the list of user profiles to the data store.
     */
    private suspend fun saveProfiles(profiles: List<UserProfile>) {
        withContext(Dispatchers.IO) {
            userProfileListDatastore.updateData { currentData ->
                val data = currentData.toBuilder().clearProfiles().addAllProfiles(profiles).build()
                data
            }
        }
    }
}
