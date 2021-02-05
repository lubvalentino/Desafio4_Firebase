package com.example.desafio4_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio4_firebase.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val recyclerView:RecyclerView by lazy {
        findViewById(R.id.rvGamesList)
    }

    private val firebaseFirestore = Firebase.firestore

    private var listGamesFinal = mutableListOf<GamesItem>()
    private val gamesAdapter:GamesAdapter = GamesAdapter(listGamesFinal)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadGames()

        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = gamesAdapter

        binding.btSaveNewGame.setOnClickListener {
            startActivity(Intent(this,RegisterMovieActivity::class.java))
        }
    }

    fun getGamesList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("games").get()
    }

    fun loadGames(){
        getGamesList().addOnCompleteListener {
            if (it.isSuccessful){
                listGamesFinal = (it.result!!.toObjects(GamesItem::class.java))
                gamesAdapter.listGames = listGamesFinal
                gamesAdapter.notifyDataSetChanged()
            } else{
                Toast.makeText(this,"Falhou",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        loadGames()
    }
}