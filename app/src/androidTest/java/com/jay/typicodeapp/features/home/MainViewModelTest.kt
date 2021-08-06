package com.jay.typicodeapp.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.jay.typicodeapp.MainCoroutineRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject

@HiltAndroidTest
class MainViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(testCoroutineRule)

    @Inject
    lateinit var mainRepository: MainRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        viewModel = MainViewModel( mainRepository)

    }

    @After
    fun tearDown() {
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testGetUsers() {



            testCoroutineRule.runBlockingTest {

                assertFalse(viewModel.getUsers().value?.size==0)


            }


    }
}