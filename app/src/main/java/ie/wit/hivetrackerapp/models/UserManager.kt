package ie.wit.hivetrackerapp.models

import ie.wit.usertrackerapp.models.generateRandomId
import timber.log.Timber
import timber.log.Timber.i
import java.util.*
import kotlin.collections.ArrayList

var lastIdUser = 0L

internal fun getIdUser(): Long {
    return lastIdUser++
}

object UserManager  : UserStore {

    val users = ArrayList<UserModel>()
    override fun findAll(): MutableList<UserModel> {
        logAll()
        return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        user.dateJoined = Date ()
        users.add(user)
    }

    override fun update(user: UserModel) {
        val founduser: UserModel? = users.find { p -> p.id == user.id }
        if (founduser != null) {
            founduser.email = user.email
            founduser.firstName = user.firstName
            founduser.image = user.image
            founduser.lastName = user.lastName
            founduser.userName = user.userName
            logAll()
        }
    }

    override fun delete(user: UserModel) {
        users.remove(user)
    }

    override suspend fun findByUsername(userName: String): UserModel? {
        return users.find { p -> p.userName == userName }
    }

    override suspend fun findByEmail(email: String): UserModel? {
        return users.find { p -> p.email == email }
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }


}