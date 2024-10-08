package com.example.khajakhoj.utils

import CredentialManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.khajakhoj.R
import com.example.khajakhoj.activity.LoginPage
import com.example.khajakhoj.viewmodel.UserViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

object Utils {
    fun logOut(context: Context, onLogoutConfirmed: () -> Unit): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Logout")
        alertDialogBuilder.setMessage("Do you want to log out?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Perform logout actions
            val credentialManager = CredentialManager(context)
            credentialManager.saveLoginState(false)

            // Invoke the callback to handle redirection
            onLogoutConfirmed()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.custom_dialog_background)
        alertDialog.window?.setBackgroundDrawable(backgroundDrawable)
        alertDialog.show()
        return alertDialog
    }


    fun showTermsAndConditions(context: Context) {
        val termsMessage = """
            <b>1. Acceptance of Terms:</b> By using our app, you agree to these terms. If you do not agree, please do not use the app.<br><br>
            
            <b>2. User Responsibilities:</b> You are responsible for maintaining the confidentiality of your account information.<br><br>
            
            <b>3. Prohibited Activities:</b><br>
                • No misuse of the app.<br>
                • No spamming or harmful content.<br><br>
            
            <b>4. Content Ownership:</b> All materials provided in the app are the property of Khaja Khoj.<br><br>
            
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

    fun showTellYourFriendsContent(context: Context) {
        val content = """
            <h1>Tell Your Friends about Khaja Khoj!</h1>
    
    <p>
        Discovering the best places to dine is even more enjoyable when shared with friends. Khaja Khoj makes it easy for you to spread the word and help your friends find their next favorite restaurant. With our user-friendly platform and personalized recommendations, everyone can enjoy a seamless and delightful dining experience.
    </p>

    <h2>Why Share Khaja Khoj with Your Friends?</h2>
    
    <p>
        By telling your friends about Khaja Khoj, you can:
    </p>

    <ul>
        <li><strong>Share the Experience:</strong> Let your friends know about the amazing dining spots you’ve discovered.</li>
        <li><strong>Enhance Social Dining:</strong> Make group outings and gatherings more enjoyable with personalized restaurant recommendations.</li>
        <li><strong>Build a Foodie Community:</strong> Join a network of food enthusiasts and share your culinary adventures.</li>
    </ul>

    <h2>How to Share Khaja Khoj</h2>
    
    <p>
        Spreading the word about Khaja Khoj is simple and quick:
    </p>

    <ul>
        <li><strong>Invite via App:</strong> Use the in-app sharing feature to send invitations directly to your contacts.</li>
        <li><strong>Social Media:</strong> Share your favorite restaurant discoveries and reviews on your social media profiles using #KhajaKhoj.</li>
        <li><strong>Word of Mouth:</strong> Tell your friends and family about the benefits of using Khaja Khoj for their dining choices.</li>
    </ul>

    <h2>Join the Khaja Khoj Community</h2>
    
    <p>
        At Khaja Khoj, we’re building a vibrant community of food lovers who appreciate great dining experiences. By sharing Khaja Khoj with your friends, you’re helping to grow this community and making it easier for everyone to find the best places to eat.
    </p>
    
    <p>
        Thank you for being a valued member of the Khaja Khoj family. Together, we can make dining out a more enjoyable and personalized experience for everyone!
    </p>
            
        """.trimIndent()


        val ad = AlertDialog.Builder(context)
        ad.setTitle("Tell Your Friends")
        ad.setMessage(Html.fromHtml(content))
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

    fun showPromotionsDialog(context: Context) {
        val promotionContent = """
            <h1>Promoting Khaja Khoj: Partner with Us!</h1>
    
    <p>
        At Khaja Khoj, we believe that finding the perfect dining experience should be as delightful as the meal itself. Our app is dedicated to making restaurant discovery seamless and enjoyable for users by prioritizing their preferences such as cuisine type, budget, proximity, and reliable reviews.
    </p>

    <h2>Why Promote Your Restaurant with Khaja Khoj?</h2>
    
    <p>
        We offer a unique platform that not only simplifies the process of finding restaurants but also ensures that each dining experience is personalized and memorable. By partnering with Khaja Khoj, you can:
    </p>

    <ul>
        <li><strong>Increase Visibility:</strong> Reach a wider audience and attract more customers to your restaurant.</li>
        <li><strong>Targeted Marketing:</strong> Utilize our advanced algorithms to connect with users who are looking for dining options that match what your restaurant offers.</li>
        <li><strong>Enhanced Reviews:</strong> Benefit from reliable and trustworthy reviews that can boost your restaurant’s reputation.</li>
    </ul>

    <h2>Join Our Network</h2>
    
    <p>
        We invite restaurant owners, managers, and food service providers to join our growing network. By collaborating with Khaja Khoj, you can showcase your restaurant’s unique offerings and attract more diners. Here’s how you can get involved:
    </p>

    <ul>
        <li><strong>Promotional Features:</strong> Highlight special deals, events, and menu items directly on our app.</li>
        <li><strong>Exclusive Listings:</strong> Get featured in our curated recommendations, ensuring that your restaurant stands out.</li>
        <li><strong>Engage with Users:</strong> Interact with your customers through reviews and feedback to enhance their dining experience.</li>
    </ul>

    <h2>Contact Us</h2>
    
    <p class="contact-info">
        If you are interested in promoting your restaurant through Khaja Khoj, we would love to hear from you. Contact us today to learn more about how we can help you reach more customers and elevate your restaurant's presence in the dining community.
    </p>
    
    <p class="contact-info">
        Email: <a href="mailto:promotions@khajakhoj.com">promotions@khajakhoj.com</a><br>
        Phone: +123-456-7890<br>
        Website: <a href="https://www.khajakhoj.com/promotions" target="_blank">www.khajakhoj.com/promotions</a>
    </p>
    
    <p>
        Join Khaja Khoj and take your restaurant to new culinary heights!
    </p>
                    """.trimIndent()


        val ad = AlertDialog.Builder(context)
        ad.setTitle("Promotions")
        ad.setMessage(Html.fromHtml(promotionContent))
        ad.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val adc = ad.create()
        val backgroundDrawable =
            ContextCompat.getDrawable(context, R.drawable.custom_dialog_background)
        adc.window?.setBackgroundDrawable(backgroundDrawable)
        adc.show()
    }

    fun showPasswordChangeDialog(
        context: Context, viewModel: UserViewModel, loadingUtil: LoadingUtil
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.change_password_dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        dialog.show()

        val currentPasswordEditText = dialog.findViewById<EditText>(R.id.currentPassword)
        val newPasswordEditText = dialog.findViewById<EditText>(R.id.newPassword)
        val confirmNewPasswordEditText = dialog.findViewById<EditText>(R.id.confirmPassword)
        val changeButton = dialog.findViewById<Button>(R.id.changePasswordButton)

        changeButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmNewPassword = confirmNewPasswordEditText.text.toString()
            viewModel.changePassword(currentPassword, newPassword, confirmNewPassword, loadingUtil)
                .observe(context as LifecycleOwner) { result ->
                    result.fold(onSuccess = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        if (it == "Password Changed Successfully") {
                            loadingUtil.dismiss()
                            dialog.dismiss()
                        }
                    }, onFailure = {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    })
                }

        }


    }

    fun showForgotPasswordDialog(context: Context, onEmailSubmitted: (String) -> Unit) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.forgot_password_dialog)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)

        val emailInput = dialog.findViewById<EditText>(R.id.forgotPasswordEmailInput)
        val submitButton = dialog.findViewById<Button>(R.id.forgotPasswordSubmit)

        submitButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isNotEmpty()) {
                onEmailSubmitted(email)
                dialog.dismiss()
            } else {
                emailInput.error = "Please enter your email"
            }
        }

        dialog.show()
    }

    fun showDeleteAccountDialog(
        context: Context, userViewModel: UserViewModel, loadingUtil: LoadingUtil
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.delete_account_dialog_layout)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.show()

        val passwordEditText = dialog.findViewById<EditText>(R.id.passwordEntry)
        val deleteButton = dialog.findViewById<Button>(R.id.deleteAccountButton)
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        deleteButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            if (password.isNotEmpty() && user != null && user.email != null) {
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                loadingUtil.showLoading()
                user.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {
                        userViewModel.deleteUser(user.uid)
                            .observe(context as LifecycleOwner) { result ->
                                if (result.isSuccess) {
                                    user.delete().addOnCompleteListener { deleteTask ->
                                        if (deleteTask.isSuccessful) {
                                            Toast.makeText(
                                                context, "User account deleted.", Toast.LENGTH_SHORT
                                            ).show()
                                            loadingUtil.dismiss()
                                            dialog.dismiss()
                                            navigateToLoginPage(context)
                                        } else {
                                            loadingUtil.dismiss()
                                            Toast.makeText(
                                                context,
                                                "Failed to delete user account.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    loadingUtil.dismiss()
                                    Toast.makeText(
                                        context,
                                        "Failed to delete teacher data.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        loadingUtil.dismiss()
                        Toast.makeText(
                            context,
                            "Authentication failed. Please check your password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(context, "Please enter your password.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLoginPage(context: Context) {
        val intent = Intent(context, LoginPage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}