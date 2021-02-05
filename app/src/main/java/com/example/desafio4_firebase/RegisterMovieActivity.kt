package com.example.desafio4_firebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.desafio4_firebase.databinding.ActivityRegisterMovieBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.*

class RegisterMovieActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterMovieBinding

    private val firebaseFirestore by lazy{
        Firebase.firestore
    }

    private val storageRef by lazy{
        Firebase.storage.reference
    }

    private lateinit var file: Uri
    private lateinit var imageUrl:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibPoster.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, IMAGE_PICK_CODE)
        }
        setupObservables()

    }

    private fun uploadImageToFirebase(fileUri: Uri) {
        fileUri.let{
            val fileName = UUID.randomUUID().toString() +".jpg"
            val refStorage = storageRef.child("images/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            Glide.with(this@RegisterMovieActivity).load(imageUrl).into(binding.ibPoster)
                        }
                    })

                .addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
//            findViewById<TextView>(R.id.tvPhoto).text = data?.data?.path.toString()
            file = data?.data!!
            uploadImageToFirebase(file as Uri)
        }
    }

    private fun setupObservables() {

        binding.btSaveGame.setOnClickListener {
            //criar a tabela de dados
            val game = hashMapOf(
                "name" to binding.etNameGame.text.toString(),
                "created" to binding.etCreated.text.toString(),
                "detail" to binding.etDetailGame.text.toString(),
                "foto" to imageUrl.toString()
            )

            //adicionar novo item na coleção de dados
            firebaseFirestore.collection("games").document(binding.etNameGame.text.toString())
                //mergeia as informações, só muda o que teve alteração
                .set(game, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Game cadastrado", Toast.LENGTH_LONG).show()
//                    startActivity(Intent(this, MainActivity::class.java))
                    onBackPressed()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }

    }

    companion object{
        const val IMAGE_PICK_CODE = 1000
    }
}