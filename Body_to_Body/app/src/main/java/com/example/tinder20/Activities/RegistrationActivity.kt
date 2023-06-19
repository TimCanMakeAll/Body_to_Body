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

    private lateinit var auth: FirebaseAuth
    private lateinit var mAuth : FirebaseAuth
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var client: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        auth = Firebase.auth

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        client = GoogleSignIn.getClient(this, options)

        Log.d("TestTest", "RegistrationActivity - onCreate")

        if(mAuth.currentUser != null){
            //если пользователь зарегестрирован
            Log.d("TestTest", "RegistrationActivity - currentUser!=null - ${mAuth.currentUser?.email.toString()}")
            if (intent.getBooleanExtra("AccountSingOut", false)){
                //если пользователь выходит из аккаунта
                Log.d("TestTest", "RegistrationActivity - AccountSingOut - ${intent.getBooleanExtra("AccountSingOut", false)}")
                signOut()
                Log.d("TestTest", "RegistrationActivity - currentUser - ${mAuth.currentUser?.email.toString()}")
            } else {
                //логика перехода на основной экран
                Log.d("TestTest", "this session is in progress from an account: ${mAuth.currentUser?.email.toString()}")
                startActivity(Intent(this, MainPageActivity::class.java))
            }
        }

        binding.btnConfirmRegistration.setOnClickListener {

            val dbEmail = binding.etEmail.text.toString().trim()
            val dbPassword = binding.etPassword.text.toString().trim()
            if (dbEmail.isEmpty()) {
                Toast.makeText(this, "You should enter email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dbPassword.isEmpty()) {
                Toast.makeText(this, "You should enter password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.etPassword.text.toString().length < 8) {
                binding.layoutPasswordInput.setHelperTextColor(this.getColorStateList(R.color.redError))
                binding.layoutPasswordInput.helperText = "8+ symbols!"
                return@setOnClickListener
            } else {
                if (binding.etConfirmingPassword.text.toString() !=  binding.etPassword.text.toString()){
                    //Если
                    Toast.makeText(this, "Duplicate your password to register!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                binding.layoutPasswordInput.isHelperTextEnabled = false
            }

            val hashedPassword = hashString(dbPassword)
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(dbEmail)
                .addOnSuccessListener {
                    mAuth.createUserWithEmailAndPassword(dbEmail, hashedPassword)
                        .addOnSuccessListener {
                            //логика, когда произошла зарегистрация новый пользователь
                            Log.d("TestTest", "You registered in app")
                            startActivity(Intent(this, MainPageActivity::class.java))
                        }
                        .addOnFailureListener{
                            //логика когда юзер не зарегестрировался, тк в БД уже есть аккаунт с таким email
                            Log.d("TestTest", "This email registered in app")
                            Toast.makeText(this, "An account with this email is already registered, you can sing in to your account", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener{
                    //логика когда пользователь ввел неккоректный email, тип которого не подходит для БД
                    Log.d("TestTest", "invalid email(")
                    Toast.makeText(this, "Make sure, you are entering the correct email address", Toast.LENGTH_SHORT).show()
                }
        }
        binding.btnGoToSignIn.setOnClickListener{
            Log.d("TestTest", "btnSignIn was clicked")
            signInGoogle()
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
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
        }
    }

    private fun signInGoogle(){
        val signInIntent = client.signInIntent
        launcher.launch(signInIntent)
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
         auth.signInWithCredential(credential)
             .addOnCompleteListener{
                 if(it.isSuccessful){
                     val intent : Intent = Intent(this, MainPageActivity::class.java)
                     startActivity(intent)
                 }else{
                     Log.d("TestTest", "Exception while trying to register with google: ${it.exception.toString()}")
                     Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                 }
         }
    }

    private fun signOut(){
        auth.signOut()
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegistrationActivity()
    }
}