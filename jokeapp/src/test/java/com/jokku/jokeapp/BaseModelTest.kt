package com.jokku.jokeapp

import com.jokku.jokeapp.data.BaseModel
import com.jokku.jokeapp.data.entity.Joke
import com.jokku.jokeapp.data.entity.JokeServerModel
import com.jokku.jokeapp.data.source.CacheDataSource
import com.jokku.jokeapp.data.source.CloudDataSource
import com.jokku.jokeapp.data.source.ErrorType
import com.jokku.jokeapp.data.source.Result
import com.jokku.jokeapp.model.BaseJokeUiModel
import com.jokku.jokeapp.model.JokeUiModel
import com.jokku.jokeapp.util.ResourceManager
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test


class BaseModelTest {

    @Test
    fun test_change_data_source(): Unit = runBlocking {
        val cacheDataSource = TestCacheDataSource()
        val cloudDataSource = TestCloudDataSource()
        val model = BaseModel(cacheDataSource, cloudDataSource, TestResourceManager())
        model.chooseDataSource(false)
        cloudDataSource.getJokeWithResult(true)
        val joke = model.getJoke()
        assertEquals(joke is BaseJokeUiModel, true)
        model.changeJokeStatus()
        assertEquals(cacheDataSource.checkContainsId(0), true)
    }

    private inner class TestCacheDataSource : CacheDataSource {
        private val map = HashMap<Int, Joke>()
        private var success = true
        private var nextJokeIdToGet = -1

        fun getNextJokeWithResult(success: Boolean, id: Int) {
            this.success = success
            nextJokeIdToGet = id
        }

        fun checkContainsId(id: Int) = map.containsKey(id)

        override suspend fun addOrRemove(id: Int, joke: Joke): JokeUiModel {
            return if (map.containsKey(id)) {
                val uiModel = map[id]!!.toBaseJoke()
                map.remove(id)
                uiModel
            } else {
                map[id] = joke
                joke.toFavoriteJoke()
            }
        }

        override suspend fun getJoke(): Result<Joke, Unit> {
            return if (success)
                Result.Success(map[nextJokeIdToGet]!!)
            else
                Result.Error(Unit)
        }
    }

    private inner class TestCloudDataSource : CloudDataSource {
        private var success = true
        private var count = 0

        fun getJokeWithResult(success: Boolean) {
            this.success = success
        }

        override suspend fun getJoke(): Result<JokeServerModel, ErrorType> {
            return if (success) {
                Result.Success(
                    JokeServerModel(
                        count++, "TestPunchline$count", "TestSetup$count", "TestType"
                    )
                )
            } else {
                Result.Error(ErrorType.NO_CONNECTION)
            }
        }
    }

    private inner class TestResourceManager : ResourceManager {
        val message: String = ""

        override fun getString(stringResId: Int) = message
    }
}