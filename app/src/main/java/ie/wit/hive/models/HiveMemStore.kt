package ie.wit.hive.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class HiveMemStore : HiveStore {

    val hives = ArrayList<HiveModel>()

    override suspend fun findAll(): List<HiveModel> {
        return hives
    }

    override suspend fun create(hive: HiveModel) {
        hive.id = getId()
        hives.add(hive)
        logAll()
    }

    override suspend fun update(hive: HiveModel) {
        val foundHive: HiveModel? = hives.find { p -> p.id == hive.id }
        if (foundHive != null) {
            foundHive.title = hive.title
            foundHive.description = hive.description
            foundHive.image = hive.image
            foundHive.location = hive.location
            logAll()
        }
    }
    override suspend fun delete(hive: HiveModel) {
        hives.remove(hive)
        logAll()
    }

    private fun logAll() {
        hives.forEach { i("$it") }
    }
    override suspend fun findById(id:Long) : HiveModel? {
        val foundHive: HiveModel? = hives.find { it.id == id }
        return foundHive
    }
    override suspend fun clear(){
        hives.clear()
    }
}