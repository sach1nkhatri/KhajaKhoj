package com.example.khajakhoj.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.khajakhoj.Dashboard
import com.example.khajakhoj.LoginPage
import com.example.khajakhoj.R

object Utils {
    fun logOut(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Logout")
        alertDialogBuilder.setMessage("Do you want to log out?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Clear the EncryptedSharedPreferences
            val encryptedSharedPreferences = EncryptedSharedPreferences.create(
                "MyEncryptedPrefs",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            with(encryptedSharedPreferences.edit()) {
                clear()
                apply()
            }

            // Clear the regular SharedPreferences
            val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                clear()
                apply()
            }

            // Start the LoginPage activity and clear the task stack
            val intent = Intent(context, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.custom_dialog_background)
        alertDialog.window?.setBackgroundDrawable(backgroundDrawable)
        alertDialog.show()
    }


    fun showTermsAndConditions(context: Context) {
        val termsMessage = """
            <b>1. Acceptance of Terms:</b> By using our app, you agree to these terms. If you do not agree, please do not use the app.<br><br>
            
            <b>2. User Responsibilities:</b> You are responsible for maintaining the confidentiality of your account information.<br><br>
            
            <b>3. Prohibited Activities:</b><br>
                • No misuse of the app.<br>
                • No spamming or harmful content.<br><br>
            
            <b>4. Content Ownership:</b> All materials provided in the app are the property of Siksha Sanskriti.<br><br>
            
            <b>5. Privacy:</> We respect your privacy and handle your personal information according to our Privacy Policy.<br><br>
            
            <b>6. Changes to Terms:</b> We may update these terms periodically. Please review them regularly.<br><br>
            
            <b>7. Contact Us:</b> If you have any questions or concerns, please contact us at support@khajakhoj.com.<br><br>
        """.trimIndent()


        val ad = AlertDialog.Builder(context)
        ad.setTitle("Terms and Conditions")
        ad.setMessage(Html.fromHtml(termsMessage))
        ad.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val adc = ad.create()
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.custom_dialog_background)
        adc.window?.setBackgroundDrawable(backgroundDrawable)
        adc.show()
    }

    fun showPolicy(context: Context) {
        val privacyMessage = """
            <p>Welcome to Khaja Khoj! This privacy policy outlines the types of information we collect, how we use it, and the measures we take to protect your privacy when you use our mobile application. By using Food Hunt, you agree to the terms of this privacy policy.</p>
        
            <h2>Information We Collect</h2>
        
            <h3>Personal Information</h3>
            <p>When you register or use Food Hunt, we may collect personal information such as:</p>
            <ul>
                <li>Name</li>
                <li>Email address</li>
                <li>Phone number</li>
                <li>Profile picture</li>
                <li>Location data</li>
            </ul>
        
            <h3>Usage Information</h3>
            <p>We collect information about how you interact with the app, including:</p>
            <ul>
                <li>Search queries</li>
                <li>Restaurants visited</li>
                <li>Reviews and ratings given</li>
                <li>Favorites and bookmarks</li>
            </ul>
        
            <h3>Device Information</h3>
            <p>We gather information about the device you use to access Food Hunt, including:</p>
            <ul>
                <li>Device type</li>
                <li>Operating system</li>
                <li>Device identifiers</li>
                <li>Mobile network information</li>
            </ul>
        
            <h3>Location Data</h3>
            <p>With your consent, we collect precise location data to provide personalized recommendations and improve the app's functionality.</p>
        
            <h2>How We Use Your Information</h2>
        
            <h3>To Provide and Improve Our Services</h3>
            <p>We use your information to:</p>
            <ul>
                <li>Personalize your experience</li>
                <li>Provide relevant search results and recommendations</li>
                <li>Improve app functionality and user experience</li>
            </ul>
        
            <h3>Communication</h3>
            <p>We may use your contact information to:</p>
            <ul>
                <li>Send you updates and notifications</li>
                <li>Respond to your inquiries and support requests</li>
            </ul>
        
            <h3>Analytics</h3>
            <p>We use data analytics to:</p>
            <ul>
                <li>Understand app usage patterns</li>
                <li>Enhance and develop new features</li>
                <li>Monitor and analyze trends and usage</li>
            </ul>
        
            <h3>Legal Compliance and Security</h3>
            <p>We may use your information to:</p>
            <ul>
                <li>Comply with legal obligations</li>
                <li>Protect our rights and property</li>
                <li>Prevent fraudulent activities</li>
            </ul>
        
            <h2>Sharing Your Information</h2>
        
            <h3>Third-Party Service Providers</h3>
            <p>We may share your information with third-party service providers who perform services on our behalf, such as:</p>
            <ul>
                <li>Data analysis</li>
                <li>Payment processing</li>
                <li>Customer support</li>
            </ul>
        
            <h3>Business Transfers</h3>
            <p>In the event of a merger, acquisition, or sale of all or a portion of our assets, your information may be transferred to the acquiring entity.</p>
        
            <h3>Legal Requirements</h3>
            <p>We may disclose your information if required by law or in response to valid requests by public authorities.</p>
        
            <h2>Your Choices</h2>
        
            <h3>Access and Update</h3>
            <p>You can access and update your personal information through your account settings in the app.</p>
        
            <h3>Opt-Out</h3>
            <p>You can opt out of receiving promotional communications by following the instructions provided in those communications. You can also control app permissions related to location data through your device settings.</p>
        
            <h2>Data Security</h2>
            <p>We implement appropriate security measures to protect your information from unauthorized access, alteration, disclosure, or destruction. However, no method of transmission over the internet or electronic storage is 100% secure.</p>
        
            <h2>Children's Privacy</h2>
            <p>Food Hunt is not intended for use by children under the age of 13. We do not knowingly collect personal information from children under 13. If we become aware that we have collected personal information from a child under 13, we will take steps to delete such information.</p>
        
            <h2>Changes to This Privacy Policy</h2>
            <p>We may update this privacy policy from time to time. We will notify you of any changes by posting the new privacy policy on this page and updating the effective date at the top. We encourage you to review this policy periodically.</p>
        
            <h2>Contact Us</h2>
            <p>If you have any questions about this privacy policy or our privacy practices, please contact us at:</p>
            <p>
                Email: <a href="mailto:support@khajakhoj.com">support@khajakhoj.com</a><br>
                Address: Tokha-08, F087,Nepal
            </p>
        """.trimIndent()

        val ad = AlertDialog.Builder(context)
        ad.setTitle("Privacy Policy")
        ad.setMessage(Html.fromHtml(privacyMessage))
        ad.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val adc = ad.create()
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.custom_dialog_background)
        adc.window?.setBackgroundDrawable(backgroundDrawable)
        adc.show()
    }

    fun showPasswordChangeDialog(context: Context){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.change_password_dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        dialog.show()
    }

    fun showForgotPasswordDialog(context: Context){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.forgot_password_dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        dialog.show()

    }
}