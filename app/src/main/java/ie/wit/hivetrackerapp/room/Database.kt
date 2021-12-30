package ie.wit.hivetrackerapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.wit.hivetrackerapp.helpers.Converters
import ie.wit.hivetrackerapp.models.UserModel

@Database(entities = arrayOf(UserModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
}