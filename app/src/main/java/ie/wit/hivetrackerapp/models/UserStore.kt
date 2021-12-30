package ie.wit.hivetrackerapp.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    suspend fun findByUsername(userName: String) :UserModel?
    suspend fun findByEmail(email: String): UserModel?
    fun findById(id: Long): UserModel?
}