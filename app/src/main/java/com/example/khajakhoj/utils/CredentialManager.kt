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

    fun getSavedCredentials(): User? {
        val uid = "uid879"
        val fullName = "John Milton Dhami"
        val email = "john.c.calhoun@examplepetstore.com"
        val phoneNumber = "0712345678"
        val address = "123 Main St, Anytown, USA"

        val savedUser = if (uid != null && fullName != null && email != null && phoneNumber != null && address != null) {
            User(uid, fullName, email, phoneNumber, address)
        } else {
            null
        }

        Log.d("CredentialManager", "Retrieved saved credentials: $savedUser")
        return savedUser
    }
}
