package com.example.desafio4_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.desafio4_firebase.Constants.List.KEY_INTENT_DESCRIPTION
import com.example.desafio4_firebase.Constants.List.KEY_INTENT_FOTO
import com.example.desafio4_firebase.Constants.List.KEY_INTENT_NAME
import com.example.desafio4_firebase.databinding.ActivityGameDetailBinding

class GameDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image = intent.getStringExtra(KEY_INTENT_FOTO)
        val name = intent.getStringExtra(KEY_INTENT_NAME)
        val description = intent.getStringExtra(KEY_INTENT_DESCRIPTION)

        binding.tvDetailName.text = name
        binding.tvDetailDetail.text = description
        Glide.with(this).load(image).into(binding.ivPictureDetail)

        binding.ibBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
    }
}