package ie.wit.hivetrackerapp.room

import android.content.Context
import androidx.room.Room
import ie.wit.hivetrackerapp.models.UserModel
import ie.wit.hivetrackerapp.models.UserStore

class UserStoreRoom(val context: Context) : UserStore {

    var dao: UserDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.userDao()
    }

    override  fun findAll(): List<UserModel> {

        return dao.findAll()
    }

    override fun findById(id: Long): UserModel? {
        return dao.findById(id)
    }

    override  fun create(user: UserModel) {
        return dao.create(user)
    }

    override  fun update(user: UserModel) {
        return dao.update(user)
    }

    override  fun delete(user: UserModel) {
        return dao.deleteUser(user)
    }

    override suspend fun findByUsername(userName: String): UserModel? {
        return dao.findByUsername(userName)
    }

    override suspend fun findByEmail(email: String): UserModel? {
        return dao.findByEmail(email)
    }
}