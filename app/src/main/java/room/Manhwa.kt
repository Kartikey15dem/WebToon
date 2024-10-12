package room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.webtoon.cat
import java.io.Serializable

@Entity(tableName = "manhwa_table")
data class Manhwa(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generated primary key
    val name: String,
    val year: String,
    val creator: String,
    val reads: String,
    val des: String,
    val rating: Float,
    var MyRating: Float,
    val img: String,
    val image: String,
    var isfav: Boolean,
    val cat:cat
) {
    // Secondary constructor without primary key
    constructor(
        name: String,
        year: String,
        creator: String,
        reads: String,
        des: String,
        rating: Float,
        mRating: Float,
        img: String,
        image: String,
        isfav: Boolean,
        cat:cat
    ) : this(
        0, // Room will auto-generate this primary key
        name, year, creator, reads, des, rating, mRating, img, image, isfav,cat
    )
}

