package com.example.desafio4_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.desafio4_firebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val firebaseAuth by lazy {
        Firebase.auth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if(email.isNotEmpty() && password.length > 5){
                //criar o usuário pelo email
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    //quando for sucesso no login
                    .addOnSuccessListener {
                        Toast.makeText(this, "Usuário logado", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        //quando der falha no login
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

            } else{
                Toast.makeText(this, "Falta preencher dados", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
    }
}