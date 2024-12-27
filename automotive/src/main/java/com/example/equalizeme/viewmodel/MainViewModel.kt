package com.example.equalizeme.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.equalizeme.model.EqualizerInfo
import com.example.equalizeme.model.UserProfile
import com.example.equalizeme.services.profiles.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

open class MainViewModel(private val application: Application) : AndroidViewModel(application) {
    // CurrentProfile
    private val currentProfileIndexFlow = MutableStateFlow(-1)
    var repository = UserProfileRepository(application.applicationContext)

    //List of profiles
    var profilesFlow = repository.userProfilesFlow

    val currentProfileFlow = profilesFlow.combine(currentProfileIndexFlow) { profiles, index ->
        return@combine profiles.elementAtOrNull(index)
    }

    //List of equalizers
    val currentEqualizer = currentProfileFlow.map { profile -> profile?.equalizerInfo }

    /**
     * Adds a new user profile to the repository.
     *
     * This function launches a coroutine to perform the following steps:
     * 1. Retrieves the current list of profiles.
     * 2. Checks if the list of profiles is null. If it is, the function returns early.
     * 3. Checks if the maximum number of profiles (3) has been reached. If it has, logs an error and returns early.
     * 4. Creates a new default profile with a name based on the current number of profiles.
     * 5. Adds the newly created profile to the repository.
     */
    fun addProfile() {
        viewModelScope.launch {
            val profiles = profilesFlow.firstOrNull()
            if (profiles == null) {
                return@launch
            }

            if(profiles.size >= 3) {
                //Log.e("MainViewModel:addProfile", "Max number of profiles reached!")
                return@launch
            }

            val profile = createDefaultProfile("Profile ${profiles.size+1}")

            repository.createProfile(profile)
        }
    }

    /**
     * Selects a user profile by its index.
     *
     * @param profileIndex The index of the profile to select.
     */
    fun selectProfile(profileIndex: Int) {
        currentProfileIndexFlow.value = profileIndex
    }

    /**
     * Sets the bass level for the currently selected profile.
     *
     * This function launches a coroutine to perform the following steps:
     * 1. Retrieves the index of the currently selected profile.
     * 2. Ensures that the profile exists. If it does not, logs an error and returns early.
     * 3. Sets the bass level for the profile in the repository.
     *
     * @param bass The new bass level to apply.
     */
    fun setBass(bass: Int) {
        viewModelScope.launch {
            val index = currentProfileIndexFlow.value

            // Ensure profile exists
            val profile = currentProfileFlow.firstOrNull()
            if(profile == null) {
                Log.e("MainViewModel:setBass"," Profile not found!")
                return@launch
            }
            repository.setBass(index, bass)
        }
    }

    /**
     * Sets the mid level for the currently selected profile.
     *
     * This function launches a coroutine to perform the following steps:
     * 1. Retrieves the index of the currently selected profile.
     * 2. Ensures that the profile exists. If it does not, logs an error and returns early.
     * 3. Sets the mid level for the profile in the repository.
     *
     * @param mid The new mid level to apply.
     */
    fun setMid(mid: Int) {
        viewModelScope.launch {
            val index = currentProfileIndexFlow.value

            // Ensure profile exists
            val profile = currentProfileFlow.firstOrNull()
            if(profile == null) {
                Log.e("MainViewModel:updateEqualizer"," Profile not found!")
                return@launch
            }
            repository.setMid(index, mid)
        }
    }

    /**
     * Sets the treble level for the currently selected profile.
     *
     * This function launches a coroutine to perform the following steps:
     * 1. Retrieves the index of the currently selected profile.
     * 2. Ensures that the profile exists. If it does not, logs an error and returns early.
     * 3. Sets the treble level for the profile in the repository.
     *
     * @param treble The new treble level to apply.
     */
    fun setTreble(treble: Int) {
        viewModelScope.launch {
            val index = currentProfileIndexFlow.value

            // Ensure profile exists
            val profile = currentProfileFlow.firstOrNull()
            if(profile == null) {
                Log.e("MainViewModel:updateEqualizer"," Profile not found!")
                return@launch
            }
            repository.setTreble(index, treble)
        }
    }

    /**
     * Creates a default user profile with the given name.
     *
     * @param name The name of the profile to create.
     * @return The newly created user profile.
     * By default the equalizer settings are set to 5 for bass, mid, and treble.
     */
    private fun createDefaultProfile(name: String) : UserProfile {
        return UserProfile.newBuilder()
            .setName(name)
            .setEqualizerInfo(
                EqualizerInfo.newBuilder()
                    .setBass(5)
                    .setMid(5)
                    .setTreble(5)
                    .build()
            )
            .build()
    }
}
