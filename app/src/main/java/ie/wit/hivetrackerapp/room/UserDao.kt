package ie.wit.hivetrackerapp.room

import androidx.room.Database
import androidx.room.*
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.wit.hivetrackerapp.models.UserManager
import ie.wit.hivetrackerapp.models.UserModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(user: UserModel)

    @Query("SELECT * FROM UserModel")
    suspend fun findAll(): List<UserModel>

    @Query("select * from UserModel where id = :id")
    suspend fun findById(id: Long): UserModel

    @Update
    suspend fun update(user: UserModel)

    @Delete
    suspend fun deleteUser(user: UserModel)

    @Query("SELECT * FROM UserModel where userName = :userName")
    suspend fun findByUsername(userName: String): UserModel

    @Query("SELECT * FROM UserModel where email = :email")
    suspend fun findByEmail(email: String): UserModel
}