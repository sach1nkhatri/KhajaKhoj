
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khajakhoj.test.AdsRepository
import com.example.khajakhoj.test.AdsRepositoryImpl
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdsViewModel() : ViewModel() {
    private val adsRepository: AdsRepository = AdsRepositoryImpl()

    private val _adsList = MutableLiveData<List<String>>()
    val adsList: LiveData<List<String>> get() = _adsList

    private val _restaurantImageUrls = MutableLiveData<List<String>>()
    val restaurantImageUrls: LiveData<List<String>> get() = _restaurantImageUrls

    fun fetchAds() {
        adsRepository.fetchAds().observeForever { ads ->
            _adsList.value = ads
        }
    }

    fun fetchRestaurantImagesByRestaurantId(restaurantId: String) {
        adsRepository.fetchRestaurantImagesByRestaurantId(restaurantId).observeForever { images ->
            _restaurantImageUrls.value = images
        }
    }
}