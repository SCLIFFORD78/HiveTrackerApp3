<<<<<<< HEAD:app/src/main/java/ie/wit/hive/helpers/Converters.kt
package ie.wit.hive.helpers
=======
package ie.wit.hivetrackerapp.helpers
>>>>>>> 20b8acfc740a8551b1e6255db86bc297bfe953b9:app/src/main/java/ie/wit/hivetrackerapp/helpers/Converters.kt

import android.net.Uri
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromUri(value: Uri): String {
<<<<<<< HEAD:app/src/main/java/ie/wit/hive/helpers/Converters.kt
        return value.toString()
    }

    @TypeConverter
    fun toUri(string: String): Uri {
=======
        return value?.toString()
    }

    @TypeConverter
    fun toUri(string: String?): Uri? {
>>>>>>> 20b8acfc740a8551b1e6255db86bc297bfe953b9:app/src/main/java/ie/wit/hivetrackerapp/helpers/Converters.kt
        return Uri.parse(string)
    }
}