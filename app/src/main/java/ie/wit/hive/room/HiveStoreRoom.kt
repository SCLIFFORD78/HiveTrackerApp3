package ie.wit.hive.room

import android.content.Context
import androidx.room.Room
import ie.wit.hive.models.HiveModel
import ie.wit.hive.models.HiveStore

class HiveStoreRoom(val context: Context) : HiveStore {

    var dao: HiveDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.hiveDao()
    }

    override suspend fun findAll(): List<HiveModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): HiveModel? {
        return dao.findById(id)
    }

    override suspend fun create(hive: HiveModel) {
        dao.create(hive)
    }

    override suspend fun update(hive: HiveModel) {
        dao.update(hive)
    }

    override suspend fun delete(hive: HiveModel) {
        dao.deleteHive(hive)
    }
    override suspend fun clear() {
    }

    override suspend fun getTag(): Long {
        var num:Long = 1
        var hives = this.findAll()
        while (hives.find { p -> p.tag == num } != null){
            num++
        }
        return num
    }
}