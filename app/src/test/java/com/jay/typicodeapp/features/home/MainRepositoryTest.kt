package com.jay.typicodeapp.features.home

import com.jay.typicodeapp.util.TestCoroutineRule
import com.jay.typicodeapp.services.data.UserData
import com.jay.typicodeapp.services.networking.MainApiHelperInterface

import com.nhaarman.mockito_kotlin.validateMockitoUsage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.IllegalStateException


class MainRepositoryTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @Mock
    lateinit var mainApiHelperInterface: MainApiHelperInterface


    private lateinit var repositoryTest: MainRepository

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
        repositoryTest = MainRepository(mainApiHelperInterface)

    }

    @After
    fun tearDown() = validateMockitoUsage()


    @Test
    fun testGetUsers() {

        try {

            testCoroutineRule.runBlockingTest {
                Mockito.doReturn(emptyList<UserData>())
                    .`when`(mainApiHelperInterface)
                    .getUsers()

                repositoryTest.getUsers()
                Mockito.verify(mainApiHelperInterface).getUsers()

            }
        } catch (e: IllegalStateException) {
        }

    }
}