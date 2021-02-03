package com.example.desafio4_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.desafio4_firebase.databinding.ActivityRegisterMovieBinding
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterMovieActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterMovieBinding

    private val firebaseFirestore by lazy{
        Firebase.firestore
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservables()

    }

    private fun setupObservables() {

        binding.btSaveGame.setOnClickListener {
            //criar a tabela de dados
            val game = hashMapOf(
                "name" to binding.etNameGame.text.toString(),
                "detail" to binding.etDetailGame.text.toString()
            )

            //adicionar novo item na coleção de dados
            firebaseFirestore.collection("games").document(binding.etNameGame.text.toString())
                //mergeia as informações, só muda o que teve alteração
                .set(game, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Game cadastrado", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }

    }
}