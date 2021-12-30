package ie.wit.hive.room

import androidx.room.*
import ie.wit.hive.models.HiveModel

@Dao
interface HiveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(hive: HiveModel)

    @Query("SELECT * FROM HiveModel")
    suspend fun findAll(): List<HiveModel>

    @Query("select * from HiveModel where id = :id")
    suspend fun findById(id: Long): HiveModel

    @Query("select * from HiveModel where tag = :tag")
    suspend fun findByTag(tag: Long): HiveModel

    @Update
    suspend fun update(hive: HiveModel)

    @Delete
    suspend fun deleteHive(hive: HiveModel)
}