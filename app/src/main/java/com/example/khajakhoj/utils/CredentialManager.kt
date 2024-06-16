import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.khajakhoj.model.Coupon
import com.example.khajakhoj.model.User

class CredentialManager(private val context: Context) {

    private val authSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_state", Context.MODE_PRIVATE)
    }

    val coupon1 = Coupon(1, "CODE123", 10, "ChiyaHub", "2024-06-02", false)
    val coupon2 = Coupon(2, "SAVE50", 50, "Himalayan Java Outlets", "2024-06-15", false)

    // Saves the login state: true for login, false for logout
    fun saveLoginState(isLoggedIn: Boolean) {
        with(authSharedPreferences.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
        Log.d("CredentialManager", "saveLoginState: Login state saved: $isLoggedIn")
    }

    // Checks the login state and returns true if logged in, false otherwise
    fun isLoggedIn(): Boolean {
        val loggedIn = authSharedPreferences.getBoolean("isLoggedIn", false)
        Log.d("CredentialManager", "isLoggedIn: Login state retrieved: $loggedIn")
        return loggedIn
    }

    fun getSavedCredentials(): User {
        val uid = "uid879"
        val fullName = "John Milton Dhami"
        val email = "john.c.calhoun@examplepetstore.com"
        val phoneNumber = "0712345678"
        val address = "123 Main St, Anytown, USA"
        val profilePictureUrl = "https://example.com/profile_picture.jpg"
        val bookmarkedRestaurants = listOf("restaurant1", "restaurant2")
        val reviews = listOf("review1", "review2")
        val rating = mapOf("restaurant1" to 4.5, "restaurant2" to 3.8)
        val claimedCoupons = listOf(coupon1, coupon2)
        val createdAt = 1718557256026L
        val updatedAt = 0L

        val savedUser = User(
            uid,
            fullName,
            email,
            phoneNumber,
            address,
            profilePictureUrl,
            bookmarkedRestaurants,
            reviews,
            rating,
            claimedCoupons,
            createdAt,
            updatedAt
        )

        Log.d("CredentialManager", "Retrieved saved credentials: $savedUser")
        return savedUser
    }

}
