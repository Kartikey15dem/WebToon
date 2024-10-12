package room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.webtoon.cat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManhwaViewModel(application: Application) : AndroidViewModel(application) {

    private val manhwaDao: ManhwaDao = ManhwaDatabase.getDatabase(application).manhwaDao()

    // Expose LiveData for all Manhwas
    fun getAllManhwas(): LiveData<List<Manhwa>> {
        return manhwaDao.getAllManhwas() // Assuming you modify the DAO to return LiveData
    }

    fun getManhwaById(id: Int): LiveData<Manhwa> {
        return manhwaDao.getManhwaById(id)
    }
    // Function to insert all lists into Room database
    fun insertAllManhwas(manList: List<Manhwa>) {
        viewModelScope.launch(Dispatchers.IO) {
            manhwaDao.insertAll(manList)
        }
    }

     //Function to update the Manhwa
    fun updateManhwa(manhwa: Manhwa) {
        viewModelScope.launch(Dispatchers.IO) {
            manhwaDao.update(manhwa)
        }
    }

    fun deleteManhwa(manhwa: Manhwa) {
        viewModelScope.launch(Dispatchers.IO) {
            manhwaDao.delete(manhwa)
        }
    }

    fun getManhwasByCategory(category: cat): LiveData<List<Manhwa>> {
        return manhwaDao.getManhwasByCategory(category)
    }

    fun getFavManhwas(): LiveData<List<Manhwa>>{
        return manhwaDao.getFavManhwas()
    }

}
