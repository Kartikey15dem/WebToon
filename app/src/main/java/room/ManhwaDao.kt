package room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.*
import com.example.webtoon.cat

@Dao
interface ManhwaDao {

    // Get all Manhwas
    @Query("SELECT * FROM manhwa_table")
    fun getAll(): List<Manhwa>

    // Load multiple Manhwas by their IDs
    @Query("SELECT * FROM manhwa_table WHERE id IN (:manhwaIds)")
    fun loadAllByIds(manhwaIds: IntArray): List<Manhwa>

    @Query("SELECT * FROM manhwa_table")
    fun getAllManhwas(): LiveData<List<Manhwa>>

    // Insert multiple Manhwas
    @Insert
    fun insertAll(manhwas: List<Manhwa>)

    // Insert a single Manhwa
    @Insert
    fun insert(manhwa: Manhwa)

    // Delete a Manhwa
    @Delete
    fun delete(manhwa: Manhwa)

    // Update a Manhwa
    @Update
    fun update(manhwa: Manhwa)

    // Get a Manhwa by its ID
    @Query("SELECT * FROM manhwa_table WHERE id = :manhwaId")
    fun getManhwaById(manhwaId: Int): LiveData<Manhwa>

    @Query("SELECT * FROM manhwa_table WHERE cat = :category")
    fun getManhwasByCategory(category: cat): LiveData<List<Manhwa>>

    @Query("SELECT * FROM manhwa_table WHERE isfav = 1")
    fun getFavManhwas(): LiveData<List<Manhwa>>


}

