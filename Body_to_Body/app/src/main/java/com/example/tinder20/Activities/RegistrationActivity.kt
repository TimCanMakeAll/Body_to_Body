package com.example.tinder20.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tinder20.R
import com.example.tinder20.databinding.ActivityRegistrationBinding
import com.example.tinder20.functions.hashString
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)

        Log.d("TestTest", "RegistrationActivity - onCreate")

        if(mAuth.currentUser != null){
            Log.d("TestTest", "RegistrationActivity - currentUser!=null - ${mAuth.currentUser?.email.toString()}")
            if (intent.getBooleanExtra("AccountSingOut", false)){
                Log.d("TestTest", "RegistrationActivity - AccountSingOut - ${intent.getBooleanExtra("AccountSingOut", false)}")
                signOut()
                Log.d("TestTest", "RegistrationActivity - currentUser - ${mAuth.currentUser?.email.toString()}")
            } else {
                Log.d("TestTest", "this session is in progress from an account: ${mAuth.currentUser?.email.toString()}")
                startActivity(Intent(this, MainPageActivity::class.java))
            }
        }

        binding.btnConfirmRegistration.setOnClickListener {
            val dbEmail = binding.etEmail.text.toString().trim()
            val dbPassword = binding.etPassword.text.toString().trim()
            val hashedPassword = hashString(dbPassword)
            if (dbEmail.isEmpty()) {
                binding.layoutEmailInput.helperText = "You should enter email!"
                return@setOnClickListener
            }
            if (dbPassword.isEmpty()) {
                binding.layoutPasswordInput.helperText = "You should enter password!"
                return@setOnClickListener
            }
            if (binding.etPassword.text.toString().length < 8) {
                binding.layoutPasswordInput.helperText = "8+ symbols!"
                return@setOnClickListener
            } else {
                if (binding.etConfirmingPassword.text.toString() != binding.etPassword.text.toString()){
                    binding.layoutConfirmPasswordInput.helperText = "Duplicate your password correctly!"
                    return@setOnClickListener
                }
            }
            binding.layoutEmailInput.isHelperTextEnabled = false
            binding.layoutPasswordInput.isHelperTextEnabled = false
            binding.layoutConfirmPasswordInput.isHelperTextEnabled = false

            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(dbEmail)
                .addOnSuccessListener {
                    mAuth.createUserWithEmailAndPassword(dbEmail, hashedPassword)
                        .addOnSuccessListener {
                            Log.d("TestTest", "Entered e-mail")
                            mAuth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener{
                                    startActivity(Intent(this, verification::class.java))
                                }
                        }
                        .addOnFailureListener{
                            Log.d("TestTest", "This email registered in app")
                            binding.layoutEmailInput.helperText = "This account already exists!"
                        }
                }
                .addOnFailureListener{
                    Log.d("TestTest", "already exists")
                    binding.layoutEmailInput.helperText = "Incorrect e-mail!"
                    return@addOnFailureListener
                }
            binding.layoutEmailInput.isHelperTextEnabled = false
        }

        binding.btnGoogleSignIn.setOnClickListener{
            Log.d("TestTest", "btnGoogleSignIn was clicked")
            signInGoogle()
        }

        binding.btnGoToSignIn.setOnClickListener {
            Log.d("TestTest", "btnGoToSignIn was clicked")
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        Log.d("TestTest", "if(result.resultCode == Activity.RESULT_OK) - запуск на проверку")
        if(result.resultCode == Activity.RESULT_OK){
            Log.d("TestTest", "if(result.resultCode == Activity.RESULT_OK) - подтверждение")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount? = task.result
                if(account != null) {
                    updateUI(account)
                }
            }else{
                Log.d("TestTest", "Exception while trying to register with google: ${task.exception.toString()}")
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("TestTest", "if(result.resultCode == Activity.RESULT_OK) - не ок")
            Log.d("TestTest", "${result.resultCode} - $result")
        }
    }

    private fun signInGoogle(){
        val signInIntent = client.signInIntent
        Log.d("TestTest", "val signInIntent = client.signInIntent")
        launcher.launch(signInIntent)
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
         mAuth.signInWithCredential(credential)
             .addOnCompleteListener{
                 if(it.isSuccessful){
                     val intent = Intent(this, MainPageActivity::class.java)
                     startActivity(intent)
                 }else{
                     Log.d("TestTest", "Exception while trying to register with google: ${it.exception.toString()}")
                     Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                 }
         }
    }

    private fun signOut(){
        mAuth.signOut()
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegistrationActivity()
    }
}