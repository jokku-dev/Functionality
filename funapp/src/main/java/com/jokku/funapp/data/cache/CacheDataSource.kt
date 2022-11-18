package com.jokku.funapp.data.cache

import com.jokku.funapp.core.*
import com.jokku.funapp.data.DataFetcher
import com.jokku.funapp.data.RepoModel
import com.jokku.funapp.domain.NoCachedDataException
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import kotlin.reflect.KClass

interface CacheDataSource<E> : DataFetcher<E>, StatusChanger<E> {
    suspend fun getDataList(): List<RepoModel<E>>
}

interface StatusChanger<E> {
    suspend fun addOrRemove(id: E, model: RepoModel<E>): RepoModel<E>
}

abstract class BaseCacheDataSource<T : RealmObject, E>(
    private val realmProvider: RealmProvider,
    private val fromRepoMapper: FromRepoMapper<T, E>,
    private val toRepoMapper: RealmToRepoMapper<T, E>
) : CacheDataSource<E> {

    protected abstract val realmClazz: KClass<T>
    protected abstract fun findRealmObject(realm: MutableRealm, id: E): T?

    override suspend fun addOrRemove(id: E, model: RepoModel<E>): RepoModel<E> =
        realmProvider.provide().writeBlocking {
            val realmItem = findRealmObject(this, id)
            if (realmItem == null) {
                val newJoke = model.map(fromRepoMapper)
                this.copyToRealm(newJoke)
                model.changeCached(true)
            } else {
                delete(realmItem)
                model.changeCached(false)
            }
        }

    override suspend fun getData(): RepoModel<E> = getRealmData { toRepoMapper.map(it.random()) }

    override suspend fun getDataList(): List<RepoModel<E>> = getRealmData { results ->
        results.map { toRepoMapper.map(it) }
    }

    private fun <R> getRealmData(block: (list: RealmResults<T>) -> R) =
        realmProvider.provide().writeBlocking {
            val list = this.query(realmClazz).find()
            if (list.isEmpty()) throw NoCachedDataException()
            else block.invoke(list)
        }
}

class JokeCacheDataSource(
    realmProvider: RealmProvider,
    toRealmMapper: JokeRealmMapper,
    toRepoMapper: JokeRealmToRepoMapper
) : BaseCacheDataSource<JokeRealmModel, Int>(realmProvider, toRealmMapper, toRepoMapper) {
    override val realmClazz = JokeRealmModel::class
    override fun findRealmObject(realm: MutableRealm, id: Int) =
        realm.query(realmClazz, "id == $0", id).first().find()
}

class QuoteCacheDataSource(
    realmProvider: RealmProvider,
    toRealmMapper: QuoteRealmMapper,
    toRepoMapper: QuoteRealmToRepoMapper
) : BaseCacheDataSource<QuoteRealmModel, String>(realmProvider, toRealmMapper, toRepoMapper) {
    override val realmClazz = QuoteRealmModel::class
    override fun findRealmObject(realm: MutableRealm, id: String) =
        realm.query(realmClazz, "id == $0", id).first().find()
}