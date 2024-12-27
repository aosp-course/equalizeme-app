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
import kotlinx.coroutines.flow.firstOrNull
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
    @MockK
    private lateinit var mockProfiles: List<UserProfile>

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockApplication.applicationContext } returns mockApplication
        coEvery { mockRepository.userProfilesFlow } returns flowOf(emptyList())
        Dispatchers.setMain(testDispatcher)

        SUT = MainViewModel(mockApplication)
    }

    /*
    * Pre-condicoes: o SUT precisa ter sido instanciado
    * Test case: este teste faz uma verificacao inicial se o MainViewModel,
    * o SUT (system under test) foi instanciado corretamente.
    * Resultado esperado: o SUT nao pode estar nulo
    * */
    @Test
    fun `check view model is not null`(){
        //Arrange
        //Act
        //Assert
        assertNotNull(SUT)
    }

    /*
    * Pre-condicoes: a lista profiles nao esta null
    * Test case: este teste invoca o metodo addProfile do MainViewModel
    * Resultado esperado: criacao de um profile no repository
    * */
    @Test
    fun `addProfile creates a profile`() = runTest {
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
        coVerify (exactly = 1) { mockRepository.createProfile(any()) }
    }

    /*
    * Pre-condicoes: a lista profiles esta com 3 elementos
    * Test case: este teste invoca o metodo addProfile do MainViewModel
    * Resultado esperado: o profile no repository nao sera criado pois profiles ja esta com a quantidade maxima
    * */
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

    /*
    * Pre-condicoes:
    * Test case: este teste invoca o metodo selectProfile do MainViewModel passando um indice
    * Resultado esperado: o valor do currentProfileIndex passa a ser o valor recebido no metodo
    * */
    @Test
    fun `test selectProfile updates currentProfileIndexFlow`() = runTest {
        // Act
        SUT.selectProfile(1)
        val currentProfileIndexFlow: MutableStateFlow<*> = ReflectionHelpers.getField(SUT, "currentProfileIndexFlow") as MutableStateFlow<*>

        // Assert
        assertEquals(1, currentProfileIndexFlow.value)
    }

    /*
    * Pre-condicoes: a lista profiles possui um objeto, de nome Profile 1
    * Test case: este teste invoca o metodo setBass do MainViewModel
    * Resultado esperado: o profile tera o valor do Bass atualizado com o valor que foi passado
    * */
    @Test
    fun `setBass sets the bass level for the current profile when profile exists`() = runTest {
        // Arrange
        val profiles = listOf(UserProfile.newBuilder().setName("Profile 1").build())
        coEvery { mockRepository.userProfilesFlow } returns flowOf(profiles)
        coEvery { mockRepository.createProfile(any()) } returns Unit
        SUT.profilesFlow = flowOf(profiles)
        SUT.repository = mockRepository
        SUT.currentProfileFlow = flowOf(UserProfile.newBuilder().setName("Profile 1").build())
        coEvery { mockRepository.setBass(any(), any()) } returns Unit
        val newBassLevel = 7

        // Act
        SUT.setBass(newBassLevel)
        testScheduler.runCurrent()

        //Assert
        coVerify { mockRepository.setBass(any(), eq(7)) }
    }

    /*
    * Pre-condicoes: a lista profiles nao possui nenhum objeto
    * Test case: este teste invoca o metodo setBass do MainViewModel
    * Resultado esperado: nao serao feitas chamadas para o setBass ja que nao existem profiles
    * */
    @Test
    fun `setBass does not set the bass level when profile does not exist`() = runTest {
        coEvery { mockRepository.userProfilesFlow } returns flowOf(emptyList())
        SUT.selectProfile(0)
        SUT.repository = mockRepository
        val newBassLevel = 7
        coEvery { mockRepository.setBass(any(), any()) } returns Unit
        SUT.currentProfileFlow = flowOf(null)

        SUT.setBass(newBassLevel)
        testScheduler.runCurrent()

        coVerify(exactly = 0) { mockRepository.setBass(any(), any()) }
    }

    /*
    * Pre-condicoes: a lista profiles possui um objeto, de nome Profile 1
    * Test case: este teste invoca o metodo setMid do MainViewModel
    * Resultado esperado: o profile tera o valor do Mid atualizado com o valor que foi passado
    * */
    @Test
    fun `setMid sets the mid level for the current profile when profile exists`() = runTest {
        // Arrange
        val profiles = listOf(UserProfile.newBuilder().setName("Profile 1").build())
        coEvery { mockRepository.userProfilesFlow } returns flowOf(profiles)
        coEvery { mockRepository.createProfile(any()) } returns Unit
        SUT.profilesFlow = flowOf(profiles)
        SUT.repository = mockRepository
        SUT.currentProfileFlow = flowOf(UserProfile.newBuilder().setName("Profile 1").build())
        coEvery { mockRepository.setMid(any(), any()) } returns Unit
        val newLevel = 7

        // Act
        SUT.setMid(newLevel)
        testScheduler.runCurrent()

        //Assert
        coVerify { mockRepository.setMid(any(), eq(7)) }
    }

    /*
    * Pre-condicoes: a lista profiles nao possui nenhum objeto
    * Test case: este teste invoca o metodo setMid do MainViewModel
    * Resultado esperado: nao serao feitas chamadas para o setMid ja que nao existem profiles
    * */
    @Test
    fun `setMid does not set the mid level when profile does not exist`() = runTest {
        coEvery { mockRepository.userProfilesFlow } returns flowOf(emptyList())
        SUT.selectProfile(0)
        SUT.repository = mockRepository
        val newLevel = 7
        coEvery { mockRepository.setMid(any(), any()) } returns Unit
        SUT.currentProfileFlow = flowOf(null)

        SUT.setBass(newLevel)
        testScheduler.runCurrent()

        coVerify(exactly = 0) { mockRepository.setMid(any(), any()) }
    }

    /*
    * Pre-condicoes: a lista profiles possui um objeto, de nome Profile 1
    * Test case: este teste invoca o metodo setTreble do MainViewModel
    * Resultado esperado: o profile tera o valor do Treble atualizado com o valor que foi passado
    * */
    @Test
    fun `setTreble sets the treble level for the current profile when profile exists`() = runTest {
        // Arrange
        val profiles = listOf(UserProfile.newBuilder().setName("Profile 1").build())
        coEvery { mockRepository.userProfilesFlow } returns flowOf(profiles)
        coEvery { mockRepository.createProfile(any()) } returns Unit
        SUT.profilesFlow = flowOf(profiles)
        SUT.repository = mockRepository
        SUT.currentProfileFlow = flowOf(UserProfile.newBuilder().setName("Profile 1").build())
        coEvery { mockRepository.setTreble(any(), any()) } returns Unit
        val newLevel = 7

        // Act
        SUT.setTreble(newLevel)
        testScheduler.runCurrent()

        //Assert
        coVerify { mockRepository.setTreble(any(), eq(7)) }
    }

    /*
    * Pre-condicoes: a lista profiles nao possui nenhum objeto
    * Test case: este teste invoca o metodo setTreble do MainViewModel
    * Resultado esperado: nao serao feitas chamadas para o setTreble ja que nao existem profiles
    * */
    @Test
    fun `setTreble does not set the treble level when profile does not exist`() = runTest {
        coEvery { mockRepository.userProfilesFlow } returns flowOf(emptyList())
        SUT.selectProfile(0)
        SUT.repository = mockRepository
        val newLevel = 7
        coEvery { mockRepository.setTreble(any(), any()) } returns Unit
        SUT.currentProfileFlow = flowOf(null)

        SUT.setBass(newLevel)
        testScheduler.runCurrent()

        coVerify(exactly = 0) { mockRepository.setTreble(any(), any()) }
    }
}
