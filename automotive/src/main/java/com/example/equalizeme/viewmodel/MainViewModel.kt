package com.example.equalizeme.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.equalizeme.model.Equalizer
import com.example.equalizeme.model.Profile

class MainViewModel : ViewModel() {
    //List of profiles
    private val _profiles = MutableLiveData<ArrayList<Profile>>()
    val profiles: LiveData<ArrayList<Profile>> get() = _profiles

    //List of equalizers
    private val _currentEqualizer = MutableLiveData<Equalizer>()
    val currentEqualizer: LiveData<Equalizer> get() = _currentEqualizer

    fun addProfile(profile: Profile) {
        val id = profile.id
        _profiles.value?.add(profile)
        _currentEqualizer.value = _profiles.value?.get(id)?.mEqualizer
    }

    fun selectProfile(profile: Profile) : Boolean {
        //Load profile equalization (by default, all values are 5)
        if(_profiles.value?.contains(profile) == true) {
            _currentEqualizer.value = _profiles.value?.get(profile.id)?.mEqualizer
            return true
        }
        return false
    }

    fun updateEqualizer(equalizer: Equalizer) {
        // Update from stored values
        _currentEqualizer.value = equalizer
    }
}

class MainViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}