package com.example.tinder20.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tinder20.Activities.MainPageActivity
import com.example.tinder20.Activities.RegistrationActivity
import com.example.tinder20.R
import com.example.tinder20.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var client: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        checkForSingOut(options)
        checkForDeletingCurrentAccount(options)

        return binding.root
    }

    private fun checkForSingOut(options: GoogleSignInOptions){

        binding.btnSingOut.setOnClickListener {

            Log.d("TestTest", "\nbtnSingOut was clicked")
            options.let {
                client = GoogleSignIn.getClient(requireActivity(), it)
            }
            if (client != null) {
                Log.d("TestTest", "Revoke access")
                client.revokeAccess()
            }
            FirebaseAuth.getInstance().signOut()

            startActivity(
                Intent(requireActivity(), RegistrationActivity::class.java).putExtra(
                    "AccountSingOut",
                    true
                )
            )
        }
    }

    private fun checkForDeletingCurrentAccount(options: GoogleSignInOptions){

        binding.btnDeleteCurrentAccount.setOnClickListener {

            Log.d("TestTest", "\nbtnDeleteCurrentAccount was clicked")
            val currentUser = FirebaseAuth.getInstance().currentUser

            options.let {
                client = GoogleSignIn.getClient(requireActivity(), it)
            }
            if (client != null) {
                Log.d("TestTest", "Revoke access")
                client.revokeAccess()
            }
            if (currentUser != null) {

                currentUser.delete()
            }

            startActivity(
                Intent(requireActivity(), RegistrationActivity::class.java).putExtra(
                    "AccountDeleted", true
                )
            )
        }
    }

    companion object {

        fun newInstance(param1: String, param2: String) = ProfileFragment()
    }
}