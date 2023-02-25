package com.example.googleauthexperimentbasic

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.googleauthexperimentbasic.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    private lateinit var googleLoginActivityLauncherIntent: ActivityResultLauncher<Intent>
    private lateinit var viewbindig: ActivityMainBinding
    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewbindig = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(viewbindig.root)

        val signinButon = viewbindig.googleLoginButtonId
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)

        Log.d("login","Dev key hash is: ${logDevKeyHash()}")

        googleLoginActivityLauncherIntent= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.d("login","Just returned from launched activity. result = ${result.resultCode}")
            if (result.resultCode == -1) { //Activity.RESULT_OK
                val intent = result.data
                val getSignedInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(intent)
                Log.d("login","Login task was successful: ${result.resultCode}")
                if (getSignedInAccountTask.isSuccessful  && getSignedInAccountTask.result.account != null) {
                    val openId = getSignedInAccountTask.result.id
                    val email = getSignedInAccountTask.result.email
                    Log.d("login","Got openid: $openId")
                    viewbindig.textviewFirst.text = "Hello, $email, your open ID is: ${openId}"
                    Toast.makeText(this@MainActivity,"Greetings, user +$email",Toast.LENGTH_SHORT).show()
                } else {
                    Toast
                        .makeText(this@MainActivity,
                        "Login was unsuccessful! ${getSignedInAccountTask.result}",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        signinButon.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            googleLoginActivityLauncherIntent.launch(signInIntent)
        }

    }
    private fun logDevKeyHash() {
        try {
            val packageName = packageName
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("KeyHash:", "Package Not found!")
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHash:", "Hash algorythm not found!")
        }
    }
}