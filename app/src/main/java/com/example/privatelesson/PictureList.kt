package com.example.privatelesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.privatelesson.databinding.ActivityPictureListBinding

class PictureList : AppCompatActivity() {

    private lateinit var binding: ActivityPictureListBinding
    private lateinit var title: PageTitle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPictureListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.azarasiButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, AzarasiFragment())
                addToBackStack(null)
                commit()
            }
        }

        binding.rocketButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, RocketFragment())
                addToBackStack(null)
                commit()
            }
        }

        binding.dentakuButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, DentakuFragment())
                addToBackStack(null)
                commit()
            }
        }

        title = PageTitle()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.pictureListTitle, title)
            commit()
        }

        binding.pictureReturnButton.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        title.setTitle("PICTURE LIST")
    }

}