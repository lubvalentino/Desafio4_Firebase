package com.example.desafio4_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.desafio4_firebase.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private val firebaseAuth by lazy {
        Firebase.auth
    }

    private val firebaseFirestore by lazy{
        Firebase.firestore
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btRegister2.setOnClickListener {
            val email = binding.etEmail2.text.toString()
            val nome = binding.etName.text.toString()
            val password = binding.etPassword2.text.toString()

            if(email.isNotEmpty() && nome.isNotEmpty() && password.length > 5){
                //criar o usuário pelo email
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    //quando for sucesso no login
                    .addOnSuccessListener {
                        setupObservables()
                        //quando der falha no login
                    }.addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }

            } else{
                Toast.makeText(this, "Falta preencher dados", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupObservables() {
        //criar a tabela de dados
        val user = hashMapOf(
            "name" to binding.etName.text.toString(),
            "email" to binding.etEmail2.text.toString(),
            "password" to binding.etPassword2.text.toString()
        )

        //adicionar novo item na coleção de dados
        firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid ?: "")
                //mergeia as informações, só muda o que teve alteração
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Usuário cadastrado", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}