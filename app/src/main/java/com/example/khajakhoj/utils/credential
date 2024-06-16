import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.khajakhoj.model.User

class CredentialManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
    }

    private val authSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_state", Context.MODE_PRIVATE)
    }

    fun saveUserCredentials(user: User) {
        with(sharedPreferences.edit()) {
            putString("uid", user.uid)
            putString("fullName", user.fullName)
            putString("email", user.email)
            putString("phoneNumber", user.phoneNumber)
            putString("address", user.address)
            apply()
        }
        saveLoginState(true)
        Log.d("CredentialManager", "User credentials saved: $user")
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        with(authSharedPreferences.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
        Log.d("CredentialManager", "Login state saved: $isLoggedIn")
    }

    fun clearUserCredentials() {
        sharedPreferences.edit().clear().apply()
        Log.d("CredentialManager", "User credentials cleared")
    }

    fun clearLoginState() {
        authSharedPreferences.edit().clear().apply()
        Log.d("CredentialManager", "Login state cleared")
    }

    fun getSavedCredentials(): User? {
        val uid = sharedPreferences.getString("uid", null)
        val fullName = sharedPreferences.getString("fullName", null)
        val email = sharedPreferences.getString("email", null)
        val phoneNumber = sharedPreferences.getString("phoneNumber", null)
        val address = sharedPreferences.getString("address", null)

        val savedUser = if (uid != null && fullName != null && email != null && phoneNumber != null && address != null) {
            User(uid, fullName, email, phoneNumber, address)
        } else {
            null
        }

        Log.d("CredentialManager", "Retrieved saved credentials: $savedUser")
        return savedUser
    }

    fun isLoggedIn(): Boolean {
        val loggedIn = authSharedPreferences.getBoolean("isLoggedIn", false)
        Log.d("CredentialManager", "Login state retrieved: $loggedIn")
        return loggedIn
    }
}
