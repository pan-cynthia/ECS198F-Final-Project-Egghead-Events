package com.egghead.events

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val anonymousButton = view.findViewById<Button>(R.id.anonymous_button)
        anonymousButton.setOnClickListener {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val action = R.id.action_signinFragment_to_eventFeedFragment
                        findNavController().navigate(action)
                    } else {
                        // If sign in fails, display a message to the user.

                    }

                }
        }

        val loginBtn = view.findViewById<Button>(R.id.google_button)
        loginBtn.setOnClickListener{
            startActivityForResult(googleSignInClient.signInIntent, 1)
        }

        callbackManager = CallbackManager.Factory.create();


        val EMAIL = "email"

        val loginButton = view.findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions("email", "public_profile")
        loginButton.setFragment(this)

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
                Log.d(tag, "Successful login")
                firebaseAuthWithFacebook(loginResult?.accessToken?.token ?: "")
            }

            override fun onCancel() {
                Log.d(tag, "Cancelled login")
                // App code
            }

            override fun onError(exception: FacebookException) {
                Log.d(tag, "Failed login")
                // App code
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("tag", "Result")
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) { // Take in a Task API of Type GoogleSignInAccount
        try {
            val account = completedTask.getResult(ApiException::class.java)!! // Use complete task API to check method calls
            Log.w("Google Sign-in", "Sign-in successful")
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign-in", "signInResult:failed code=" + e.statusCode)
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "signInWithCredential:success")
                    findNavController().navigate(R.id.action_signinFragment_to_eventFeedFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "signInWithCredential:failure", task.exception)
                    Snackbar.make(requireView(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun firebaseAuthWithFacebook(idToken: String) {
        val credential = FacebookAuthProvider.getCredential(idToken)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "signInWithCredential:success")
                    findNavController().navigate(R.id.action_signinFragment_to_eventFeedFragment)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "signInWithCredential:failure", task.exception)
                }
            }
    }
}