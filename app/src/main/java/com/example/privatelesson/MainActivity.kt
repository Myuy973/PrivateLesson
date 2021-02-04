package com.example.privatelesson

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.privatelesson.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addStringButton.setOnClickListener {
            binding.textView.text = "${binding.textView.text}" + "!"
        }

        binding.pageMove.setOnClickListener {

            var radiobutton = binding.radioGroup.checkedRadioButtonId
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("RADIO_VALUE", radiobutton)
            startActivity(intent)
        }

        binding.clearButton.setOnClickListener { radio_clear(it) }

        binding.pictureListMove.setOnClickListener {
            val intent = Intent(this, PictureList::class.java)
            startActivity(intent)
        }

        binding.takePictureButton.setOnClickListener {
            val intent = Intent(this, TakePictureActivity::class.java)
            startActivity(intent)
        }

        binding.textRealm.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

    }

    private fun radio_clear(view: View) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit { clear() }
        Snackbar.make(view, "クリアーしました", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.YELLOW)
                .show()
    }



}