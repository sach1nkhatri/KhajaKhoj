
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khajakhoj.test.AdsRepository
import com.example.khajakhoj.test.AdsRepositoryImpl
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdsViewModel : ViewModel() {
    private val adsRepository: AdsRepository = AdsRepositoryImpl()
    val adsList: LiveData<List<String>> get() = adsRepository.fetchAds()

//    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("ads")
//    private val _adsList = MutableLiveData<List<String>>()
//    val adsList: LiveData<List<String>> get() = _adsList
//
//    fun fetchAds() {
//        storageReference.listAll().addOnSuccessListener { listResult ->
//            val items = listResult.items
//            if (items.isNotEmpty()) {
//                val urls = mutableListOf<String>()
//                items.forEach { item ->
//                    item.downloadUrl.addOnSuccessListener { uri ->
//                        urls.add(uri.toString())
//                        if (urls.size == items.size) {
//                            _adsList.value = urls
//                        }
//                    }
//                }
//            } else {
//                _adsList.value = emptyList()
//            }
//        }
//    }
}
