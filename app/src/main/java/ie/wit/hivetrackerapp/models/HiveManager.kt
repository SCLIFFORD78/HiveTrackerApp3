package ie.wit.hivetrackerapp.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object HiveManager : HiveStore {

    private val hives = ArrayList<HiveModel>()

    override fun findAll(): List<HiveModel> {
        return hives
    }

    override fun findByType(type: String): List<HiveModel> {
        val resp: MutableList<HiveModel> = mutableListOf()
        for (hive in hives) if(hive.type == type) {
            resp.add(0,hive)
        }
        return if (resp.isNotEmpty()){
            resp
        } else emptyList()
    }

    override fun findByOwner(userID: Long): List<HiveModel> {
        val resp: MutableList<HiveModel> = mutableListOf()
        for (hive in hives) if(hive.userID == userID) {
            resp.add(0,hive)
        }
        return if (resp.isNotEmpty()){
            resp
        } else emptyList()
    }

    override fun findByTag(tag: Long): HiveModel? {
        return hives.find { p -> p.tag == tag }
    }

    override fun create(hive: HiveModel) {
        hive.id = getId()
        hives.add(hive)
        logAll()
    }

    override fun update(hive: HiveModel) {
        val foundHive: HiveModel? = hives.find { p -> p.id == hive.id }
        if (foundHive != null) {
            foundHive.tag = hive.tag
            foundHive.description = hive.description
            foundHive.image = hive.image
            foundHive.lat = hive.lat
            foundHive.lng = hive.lng
            foundHive.zoom = hive.zoom
            logAll()

        }
    }

    override fun delete(hive: HiveModel) {
        hives.remove(hive)
    }

    override fun find(hive: HiveModel): HiveModel? {
        return hives.find { p -> p.id == hive.id }
    }

    override fun getTag(): Long {
        var num:Long = 1
        while (hives.find { p -> p.tag == num } != null){
            num++
        }
        return num
    }

    private fun logAll() {
        hives.forEach { i("$it") }
    }
}