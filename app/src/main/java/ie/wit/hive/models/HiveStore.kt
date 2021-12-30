package ie.wit.hive.models

interface HiveStore {
    suspend fun findAll(): List<HiveModel>
    suspend fun create(hive: HiveModel)
    suspend fun update(hive: HiveModel)
    suspend fun findById(id:Long) : HiveModel?
    suspend fun delete(hive: HiveModel)
    suspend fun clear()
    suspend fun getTag():Long
}