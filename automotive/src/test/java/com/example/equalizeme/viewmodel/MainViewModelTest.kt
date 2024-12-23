package com.example.equalizeme.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equalizeme.model.Equalizer
import com.example.equalizeme.model.Profile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var SUT: MainViewModel

    @Before
    fun setUp() {
        SUT = MainViewModel()
    }

    @Test
    fun `check view model is not null`(){
        //Arrange
        //Act
        //Assert
        assertNotNull(SUT)
    }

    @Test
    fun `check profile added`() {
        //Arrange
        val id = 10
        val newProfile = Profile(id, "testName", Equalizer())

        //Act
        SUT.addProfile(newProfile)

        //Assert
        assertFalse(SUT.profiles.value.isNullOrEmpty())
        assertEquals(SUT.profiles.value?.get(0)?.id, id)
    }

    @Test
    fun `check profile selected correctly`() {
        //Arrange
        val id = 1
        val eq = 10
        val equalizer = Equalizer(eq, eq, eq)
        val newProfile = Profile(id, "testName", equalizer)
        SUT.addProfile(newProfile)

        //Act
        SUT.selectProfile(newProfile)

        //Assert
        assertEquals(eq, SUT.currentEqualizer.value?.mBass)
        assertEquals(eq, SUT.currentEqualizer.value?.mMid)
        assertEquals(eq, SUT.currentEqualizer.value?.mTreble)
    }

    @Test
    fun `check update equalizer`() {
        //Arrange
        val id = 1
        val newEq = 7
        val equalizer = Equalizer(10, 10, 10)
        val newProfile = Profile(id, "testName", equalizer)
        SUT.addProfile(newProfile)

        val newEqualizer = Equalizer(newEq, newEq, newEq)

        //Act
        SUT.updateEqualizer(newEqualizer)

        //Assert
        assertEquals(newEq, SUT.currentEqualizer.value?.mBass)
        assertEquals(newEq, SUT.currentEqualizer.value?.mMid)
        assertEquals(newEq, SUT.currentEqualizer.value?.mTreble)
    }

}