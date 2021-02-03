package com.example.privatelesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.privatelesson.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var title: PageTitle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = PageTitle()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.secondActivityTitle, title)
            commit()
        }

        val radio_value = intent.getIntExtra("RADIO_VALUE", 0)
        Log.d("value", "$radio_value")

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var red_total = pref.getInt("RED_TOTAL", 0)
        var blue_total = pref.getInt("BLUE_TOTAL", 0)
        var green_total = pref.getInt("GREEN_TOTAL", 0)

        when(radio_value) {
            R.id.redButton -> {
                binding.radioOutputText.text = "RED"
                ++red_total
                pref.edit { putInt("RED_TOTAL", red_total) }
            }

            R.id.blueButton -> {
                binding.radioOutputText.text = "BLUE"
                ++blue_total
                pref.edit { putInt("BLUE_TOTAL", blue_total) }
            }

            R.id.greenButton -> {
                binding.radioOutputText.text = "GREEN"
                ++green_total
                pref.edit { putInt("GREEN_TOTAL", green_total) }
            }
            else ->
                binding.radioOutputText.text = "NULL"
        }

        binding.redTotal.text = "red: " + red_total.toString()
        binding.blueTotal.text = "blue: " + blue_total.toString()
        binding.greenTotal.text = "green: " + green_total.toString()

        binding.returnButton.setOnClickListener { active_back() }

    }

    override fun onResume() {
        super.onResume()
        title.setTitle("SUB PAGE")
    }

    private fun active_back () {
        finish()
    }

}