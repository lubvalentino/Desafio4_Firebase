package com.example.desafio4_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio4_firebase.Constants.List.KEY_INTENT_DESCRIPTION
import com.example.desafio4_firebase.Constants.List.KEY_INTENT_FOTO
import com.example.desafio4_firebase.Constants.List.KEY_INTENT_NAME
import com.example.desafio4_firebase.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val firebaseFirestore = Firebase.firestore

    private var listGamesFinal = mutableListOf<GamesItem>()
    private val gamesAdapter:GamesAdapter = GamesAdapter(listGamesFinal)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadGames()



        binding.btSaveNewGame.setOnClickListener {
            startActivity(Intent(this,RegisterGameActivity::class.java))
        }
    }

    private fun getGamesList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("games").get()
    }

    fun loadGames(){
        getGamesList().addOnCompleteListener {
            if (it.isSuccessful){
                listGamesFinal = (it.result?.toObjects(GamesItem::class.java) as MutableList<GamesItem>)
                gamesAdapter.listGames = listGamesFinal
                gamesAdapter.notifyDataSetChanged()
                setupRecyclerView(listGamesFinal)
            } else{
                Toast.makeText(this,"Falhou",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecyclerView(games: MutableList<GamesItem>){
        binding.rvGamesList.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            adapter = GamesAdapter(games)

//            {positon ->
//                val intent = Intent (this@MainActivity, GameDetailActivity::class.java)
//                intent.putExtra(KEY_INTENT_NAME,games[positon].name)
//                intent.putExtra(KEY_INTENT_FOTO,games[positon].foto)
//                intent.putExtra(KEY_INTENT_DESCRIPTION,games[positon].detail)
//                startActivity(intent)
//            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        loadGames()
    }


}