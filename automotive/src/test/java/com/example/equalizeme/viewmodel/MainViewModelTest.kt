package com.example.equalizeme.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import android.app.Application
import android.os.Build
import com.example.equalizeme.model.UserProfile
import com.example.equalizeme.services.profiles.UserProfileRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.robolectric.util.ReflectionHelpers

@Config(sdk = [Build.VERSION_CODES.R])
@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var SUT: MainViewModel
    @MockK
    private lateinit var mockRepository: UserProfileRepository
    @MockK
    private lateinit var mockApplication: Application

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockApplication.applicationContext } returns mockApplication
        coEvery { mockRepository.userProfilesFlow } returns flowOf(emptyList())
        Dispatchers.setMain(testDispatcher)

        SUT = MainViewModel(mockApplication)
    }

    @Test
    fun `check view model is not null`(){
        //Arrange
        //Act
        //Assert
        assertNotNull(SUT)
    }

    @Test
    fun `addProfile does nothing when profiles are null`() = runTest {
        // Arrange
        val profiles = listOf(UserProfile.newBuilder().setName("Profile 1").build())
        coEvery { mockRepository.userProfilesFlow } returns flowOf(profiles)
        coEvery { mockRepository.createProfile(any()) } returns Unit
        SUT.profilesFlow = flowOf(profiles)
        SUT.repository = mockRepository

        // Act
        SUT.addProfile()
        testScheduler.runCurrent()

        // Assert
        // Assert
        coVerify { mockRepository.createProfile(any()) }
    }

    @Test
    fun `test addProfile does not add a profile when 3 profiles exist`() = runTest {
        // Arrange
        val profiles = List(3) { UserProfile.newBuilder().setName("Profile ${it + 1}").build() }
        coEvery { mockRepository.userProfilesFlow } returns flowOf(profiles)
        coEvery { mockRepository.createProfile(any()) } returns Unit
        SUT.profilesFlow = flowOf(profiles)
        SUT.repository = mockRepository

        // Act
        SUT.addProfile()
        testScheduler.runCurrent()

        // Assert
        coVerify(exactly = 0) { mockRepository.createProfile(any()) }
    }

    @Test
    fun `test selectProfile updates currentProfileIndexFlow`() = runTest {
        // Act
        SUT.selectProfile(1)
        val currentProfileIndexFlow: MutableStateFlow<*> = ReflectionHelpers.getField(SUT, "currentProfileIndexFlow") as MutableStateFlow<*>

        // Assert
        assertEquals(1, currentProfileIndexFlow.value)
    }

}
