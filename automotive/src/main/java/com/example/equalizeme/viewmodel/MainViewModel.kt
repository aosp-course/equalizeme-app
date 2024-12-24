package com.example.equalizeme.viewmodel

import android.provider.ContactsContract.Profile
import android.util.Log
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

class MainViewModel(private val repository: UserProfileRepository) : ViewModel() {
    //List of profiles
    val profilesFlow = repository.userProfilesFlow.shareIn(viewModelScope, SharingStarted.Eagerly)

    // CurrentProfile
    private val currentProfileIndexFlow = MutableStateFlow(-1)
    val currentProfileFlow = profilesFlow.combine(currentProfileIndexFlow) { profiles, index ->
        return@combine profiles.elementAtOrNull(index)
    }

    //List of equalizers
    val currentEqualizer = currentProfileFlow.map { profile -> profile?.equalizerInfo }

    fun addProfile() {
        viewModelScope.launch {
            val profiles = profilesFlow.firstOrNull() ?: emptyList()

            if(profiles.size >= 3) {
                Log.e("MainViewModel:addProfile", "Max number of profiles reached!")
                return@launch
            }

            val profile = createDefaultProfile("Profile ${profiles.size+1}")

            repository.createProfile(profile)
        }
    }

    fun selectProfile(profileIndex: Int) {
        currentProfileIndexFlow.value = profileIndex
    }

    fun updateEqualizer(equalizer: EqualizerInfo) {
        viewModelScope.launch {
            val index = currentProfileIndexFlow.value

            // Ensure profile exists
            val profile = currentProfileFlow.firstOrNull()
            if(profile == null) {
                Log.e("MainViewModel:updateEqualizer"," Profile not found!")
                return@launch
            }

            repository.setEqualizerInfo(index, equalizer)
        }
    }

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

//class MainViewModelFactory() : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return MainViewModel() as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}