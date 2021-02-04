package com.example.privatelesson

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.privatelesson.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        val naviController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(naviController)

        binding.fab.setOnClickListener {
            naviController.navigate(R.id.action_to_noteEditFragment)
        }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp()

    fun setFabVisible(visibility: Int) {
        binding.fab.visibility = visibility
    }

}