import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.khajakhoj.model.User

class CredentialManager(private val context: Context) {

    private val authSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_state", Context.MODE_PRIVATE)
    }

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

    // Saves the user data
    fun saveUser(user: User) {
        with(authSharedPreferences.edit()) {
            putString("uid", user.uid)
            putString("fullName", user.fullName)
            putString("email", user.email)
            putString("phoneNumber", user.phoneNumber)
            putString("address", user.address)
            putString("profilePictureUrl", user.profilePictureUrl)
            putStringSet("bookmarkedRestaurants", user.bookmarkedRestaurants.toSet())
            putStringSet("reviews", user.reviews.toSet())
            // For maps and complex objects, you'd need a different approach (e.g., JSON or custom serialization)
            // Skipping `rating` and `claimedCoupons` for simplicity in this example
            putLong("createdAt", user.createdAt)
            putLong("updatedAt", user.updatedAt)
            apply()
        }
        Log.d("CredentialManager", "saveUser: User data saved")
    }

    // Retrieves the user data
    fun getUser(): User? {
        val uid = authSharedPreferences.getString("uid", "") ?: return null
        val fullName = authSharedPreferences.getString("fullName", "") ?: return null
        val email = authSharedPreferences.getString("email", "") ?: return null
        val phoneNumber = authSharedPreferences.getString("phoneNumber", "") ?: return null
        val address = authSharedPreferences.getString("address", "") ?: return null
        val profilePictureUrl = authSharedPreferences.getString("profilePictureUrl", "") ?: return null
        val bookmarkedRestaurants = authSharedPreferences.getStringSet("bookmarkedRestaurants", emptySet())?.toList() ?: emptyList()
        val reviews = authSharedPreferences.getStringSet("reviews", emptySet())?.toList() ?: emptyList()
        // For maps and complex objects, you'd need a different approach (e.g., JSON or custom serialization)
        val createdAt = authSharedPreferences.getLong("createdAt", 0L)
        val updatedAt = authSharedPreferences.getLong("updatedAt", 0L)

        return User(
            uid,
            fullName,
            email,
            phoneNumber,
            address,
            profilePictureUrl,
            bookmarkedRestaurants,
            reviews,
            emptyMap(), // Placeholder for rating
            emptyList(), // Placeholder for claimedCoupons
            createdAt,
            updatedAt
        )
    }

    // Clears the user data
    fun clearUser() {
        with(authSharedPreferences.edit()) {
            remove("uid")
            remove("fullName")
            remove("email")
            remove("phoneNumber")
            remove("address")
            remove("profilePictureUrl")
            remove("bookmarkedRestaurants")
            remove("reviews")
            // Skipping `rating` and `claimedCoupons` for simplicity in this example
            remove("createdAt")
            remove("updatedAt")
            apply()
        }
        Log.d("CredentialManager", "clearUser: User data cleared")
    }
}

