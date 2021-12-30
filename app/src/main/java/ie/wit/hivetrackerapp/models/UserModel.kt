package ie.wit.hivetrackerapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*



@Parcelize
@Entity
data class UserModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                     var fbId: String = "",
                     var firstName: String = "",
                     var lastName: String = "",
                     var image: Uri = Uri.EMPTY,
                     var userName : String = "",
                     var email: String = "",
                     var dateJoined: Date = Date()): Parcelable


