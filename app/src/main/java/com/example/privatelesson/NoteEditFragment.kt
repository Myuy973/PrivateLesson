package com.example.privatelesson

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.privatelesson.databinding.ActivityNoteBinding
import com.example.privatelesson.databinding.FragmentNoteEditBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NoteEditFragment : Fragment() {

    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? NoteActivity)?.setFabVisible(View.INVISIBLE)
        binding.save.setOnClickListener { saveNote(it) }
    }

    private fun saveNote(view: View) {
        realm.executeTransaction { db: Realm ->
            val maxId = db.where<Notepad>().max("id")
            val nextId = (maxId?.toLong() ?: 0L) + 1L
            val notepad = db.createObject<Notepad>(nextId)
            val date = "${binding.dateEdit.text} ${binding.timeEdit.text}".toDate()
            if (date != null) notepad.date = date
            notepad.title = binding.titleEdit.text.toString()
            notepad.title = binding.dateEdit.text.toString()
        }

        Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
            .setAction("戻る") { findNavController().popBackStack()}
            .setActionTextColor(Color.YELLOW)
            .show()

    }

    private fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch (e: IllegalArgumentException) {
            return null
        } catch (e: ParseException) {
            return null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}