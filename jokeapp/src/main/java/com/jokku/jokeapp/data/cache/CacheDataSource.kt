package com.jokku.jokeapp.data.cache

import com.jokku.jokeapp.core.JokeRealmMapper
import com.jokku.jokeapp.core.Mapper
import com.jokku.jokeapp.core.ModelMapper
import com.jokku.jokeapp.core.QuoteRealmMapper
import com.jokku.jokeapp.data.DataFetcher
import com.jokku.jokeapp.data.RepoModel
import com.jokku.jokeapp.domain.NoCachedDataException
import kotlin.reflect.KClass

interface CacheDataSource : DataFetcher, StatusChanger

interface StatusChanger {
    suspend fun addOrRemove(id: Int, model: RepoModel): RepoModel
}

abstract class BaseCacheDataSource<T: RealmModel>(
    private val realmProvider: RealmProvider,
    private val mapper: ModelMapper<T>
) : CacheDataSource {

    protected abstract val realmModelClazz : KClass<T>

    override suspend fun addOrRemove(id: Int, model: RepoModel): RepoModel =
        realmProvider.provide().writeBlocking {
            val realmItem = this.query(realmModelClazz,"id == $id", id).first().find()
            if (realmItem == null) {
                val newJoke = model.map(mapper)
                this.copyToRealm(newJoke)
                model.changeCached(true)
            } else {
                delete(realmItem)
                model.changeCached(false)
            }
        }

    override suspend fun getData(): RepoModel = realmProvider.provide().writeBlocking {
        val list = this.query(realmModelClazz).find()
        if (list.isEmpty()) throw NoCachedDataException()
        else (list.random() as Mapper<RepoModel>).map()
    }
}

class JokeCacheDataSource(
    realmProvider: RealmProvider,
    mapper: JokeRealmMapper
) : BaseCacheDataSource<JokeRealmModel>(realmProvider, mapper) {
    override val realmModelClazz = JokeRealmModel::class
}

class QuoteCacheDataSource(
    realmProvider: RealmProvider,
    mapper: QuoteRealmMapper
) : BaseCacheDataSource<QuoteRealmModel>(realmProvider, mapper) {
    override val realmModelClazz = QuoteRealmModel::class
}