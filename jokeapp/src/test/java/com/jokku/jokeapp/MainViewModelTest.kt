package com.jokku.jokeapp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jokku.jokeapp.data.Model
import com.jokku.jokeapp.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.*
import org.junit.Test

class MainViewModelTest {

    @ExperimentalCoroutinesApi
    @Test
    fun test_get_joke_from_cloud_success(): Unit = runBlocking {
        val testModel = TestModel()
        val testCommunicator = TestCommunicator()
        val viewModel = MainViewModel(testModel, testCommunicator, StandardTestDispatcher())

        testModel.success = true
        viewModel.chooseFavorites(false)
        viewModel.getJoke()

        val actualText = testCommunicator.text
        val actualId = testCommunicator.id
        val expectedText = "cloudJokeSetup\ncloudJokePunchline"
        val expectedId = 0
        assertEquals(expectedText, actualText)
        assertNotEquals(expectedId, actualId)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_get_joke_from_cloud_fail(): Unit = runBlocking {
        val testModel = TestModel()
        val testCommunicator = TestCommunicator()
        val viewModel = MainViewModel(testModel, testCommunicator, StandardTestDispatcher())

        testModel.success = false
        viewModel.chooseFavorites(false)
        viewModel.getJoke()

        val actualText = testCommunicator.text
        val actualId = testCommunicator.id
        val expectedText = "noConnection\n"
        val expectedId = 0
        assertEquals(expectedText, actualText)
        assertEquals(expectedId, actualId)
    }

    private inner class TestModel : Model {
        private val cacheJokeUiModel = BaseJokeUiModel("cachedJokeSetup", "cachedJokePunchline")
        private val cacheJokeFailure = FailedJokeUiModel("cacheFailed")
        private val cloudJokeUiModel = BaseJokeUiModel("cloudJokeSetup", "cloudJokePunchline")
        private val cloudJokeFailure = FailedJokeUiModel("noConnection")
        var success: Boolean = false
        private var getFromCache = false
        private var cachedJoke: JokeUiModel? = null

        override suspend fun getJoke(): JokeUiModel {
            return if (success) {
                if (getFromCache) {
                    cacheJokeUiModel.also {
                        cachedJoke = it
                    }
                } else {
                    cloudJokeUiModel.also {
                        cachedJoke = it
                    }
                }
            } else {
                cachedJoke = null
                if (getFromCache) {
                    cacheJokeFailure
                } else {
                    cloudJokeFailure
                }
            }
        }

        override suspend fun changeJokeStatus(): JokeUiModel? {
            TODO("Not yet implemented")
        }

        override fun chooseDataSource(isCached: Boolean) {
            getFromCache = isCached
        }
    }

    private inner class TestCommunicator : Communicator {
        var text = ""
        var id = -1
        var observedCount = 0

        override fun showData(data: Pair<String, Int>) {
            text = data.first
            id = data.second
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<Pair<String, Int>>) {
            observedCount++
        }
    }
}