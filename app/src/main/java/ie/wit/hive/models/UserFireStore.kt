package ie.wit.hivetrackerapp.models

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.Context

class UserFireStore(val context: Context) : UserStore {

    val users = ArrayList<UserModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference


    override fun findAll(): List<UserModel> {
        return users
    }

    override fun create(user: UserModel) {
        val key = db.child("users").push().key
        key?.let {
            user.fbId = key
            users.add(user)
            db.child("users").child(userId).child(key).setValue(user)
        }
    }

    override fun update(user: UserModel) {
        TODO("Not yet implemented")
    }

    override fun delete(user: UserModel) {
        TODO("Not yet implemented")
    }

    override suspend fun findByUsername(userName: String): UserModel? {
        return users.find { p -> p.userName == userName }
    }

    override suspend fun findByEmail(email: String): UserModel? {
        return UserManager.users.find { p -> p.email == email }
    }
}