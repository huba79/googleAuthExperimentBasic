package com.example.googleauthexperimentbasic

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.googleauthexperimentbasic.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity(),OnClickListener {
    private val LOGGER_TAG = "login"
    private val GOOGLE_SIGN_IN_INTENT_CODE =1
    private lateinit var viewbindig: ActivityMainBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.client_server_id))
            .requestServerAuthCode(getString(R.string.client_server_id))
            .build()

        viewbindig = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(viewbindig.root)
        mGoogleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)

        viewbindig.resultViewId.movementMethod=ScrollingMovementMethod()

        val signInButton = viewbindig.googleLoginButtonId
        signInButton.setOnClickListener(this)

        val signOutButton = viewbindig.googleLogoutButtonId
        signOutButton.setOnClickListener(this)

        Log.d("login","Dev key hash is: ${logDevKeyHash()}")

    }
    private fun logDevKeyHash() {
//        try {
//            val packageName = packageName
//            val info = packageManager.getPackageInfo(
//                packageName,
//                PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.e(LOGGER_TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            Log.e("LOGGER_TAG:", "Package Not found!")
//        } catch (e: NoSuchAlgorithmException) {
//            Log.e("LOGGER_TAG:", "Hash algorythm not found!")
//        }
    }
    private fun signIn(googleSignInClient: GoogleSignInClient) {
        Log.d(LOGGER_TAG,"Signing in...")
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_INTENT_CODE)
    }
    private fun signOut(signInClient: GoogleSignInClient) {
        Log.d(LOGGER_TAG,"Signing out...")
        signInClient.signOut()
        updateUI(GoogleSignIn.getLastSignedInAccount(this)) //param should be null
    }
    @Deprecated("Deprecated in Java")
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(LOGGER_TAG,"processing ActivityResults")

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === GOOGLE_SIGN_IN_INTENT_CODE) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            if(task.isSuccessful){
                val account = task.result.account
                if (account !=null){
                    updateUI(task.result)
                    Log.d(LOGGER_TAG,"(oAR)The found account was: ${task.result.account.toString()}")
                    Log.d(LOGGER_TAG,"ID token: ${task.result.idToken}")
                    Log.d(LOGGER_TAG,"server auth code: ${task.result.serverAuthCode}")
                    Log.d(LOGGER_TAG,"Requested scopes: ${task.result.requestedScopes}")
                    Log.d(LOGGER_TAG,"Granted scopes: ${task.result.grantedScopes}")
                    Log.d(LOGGER_TAG,"Is it expired? ${task.result.isExpired}")
                    Log.d(LOGGER_TAG,"")
                } else {
                    updateUI(task.result)
                    Log.d(LOGGER_TAG,"(oAR)we have found no account data for ${task.result.email}. Shame on us.")
                }
            } else {
                Log.e(LOGGER_TAG,"(oAR)Login task was unsuccessful! Problem: ${task.exception!!.message} , Cause: ${task.exception!!.message}")
                viewbindig.resultViewId.text = "Unsuccessful login, please try again later. ${task.exception!!.message}"
            }

            //handleSignInResult(task)
        }
    }
    /*
    private fun handleSignInResult(signInTask: Task<GoogleSignInAccount>) {
    //if a listener system would be required
        signInTask
            .addOnSuccessListener {
                val account = it.account
                if (account !=null){
                    updateUI(it)
                    Log.d(LOGGER_TAG,"The found account was: ${it.account.toString()}")
                } else {
                    updateUI(it)
                    Log.d(LOGGER_TAG,"we have found no account data for ${it.email}. Shame on us.")
                }
            }
            .addOnFailureListener {
                Log.e(LOGGER_TAG,"Login task was unsuccessful! Problem: ${it.message} , Cause: ${it.message}")
                viewbindig.resultViewId.text = "Unsuccessful login, please try again later. ${it.message}"
            }

    }
    */
    @Override
    fun onStart(bundle:Bundle){
        super.onStart()
        Log.d(LOGGER_TAG,"onStart triggered")
        // Check for existing Google Sign In account, if the user is already signed in the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
        updateUI(account)
    }
    private fun updateUI(account: GoogleSignInAccount?) {
        if(account!=null){
            viewbindig.resultViewId.text = "Greetings, ${account.email}, \nyour id token is:${account.idToken} ,\nyour access token is: ${account.serverAuthCode} "
        } else {
            viewbindig.resultViewId.text = "You are not logged in, please log in to continue! "
        }
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            viewbindig.googleLoginButtonId.id ->{
                Log.d(LOGGER_TAG,"Login clicked")
                signIn(mGoogleSignInClient)
            }
            viewbindig.googleLogoutButtonId.id->{
                Log.d(LOGGER_TAG,"Logout clicked")
                signOut(mGoogleSignInClient)
            }
        }
    }

}