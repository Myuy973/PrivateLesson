package com.example.privatelesson

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.privatelesson.databinding.FragmentNoteEditBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.lang.IllegalArgumentException
import java.sql.Time
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NoteEditFragment : Fragment() {

    private var _binding: FragmentNoteEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var realm: Realm
    private val args: NoteEditFragmentArgs by navArgs()

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
        if (args.noteId != -1L) {
            val notepad = realm.where<Notepad>().equalTo("id", args.noteId).findFirst()
            binding.dateEdit.setText(DateFormat.format("yyyy/MM/dd", notepad?.date))
            binding.timeEdit.setText(DateFormat.format("HH:mm", notepad?.date))
            binding.titleEdit.setText(notepad?.title)
            binding.detailEdit.setText(notepad?.detail)
            binding.delete.visibility = View.VISIBLE
        } else {
            binding.delete.visibility = View.INVISIBLE

        }

        binding.dateButton.setOnClickListener {
            // date: onTimeSetのなかのonSelectの引数
            DateDialog { date ->
                binding.dateEdit.setText(date)
            }.show(parentFragmentManager, "date_dialog")
        }

        binding.timeButton.setOnClickListener {
            TimeDialog { date ->
                binding.timeEdit.setText(date)
            }.show(parentFragmentManager, "time_dialog")
        }

        (activity as? NoteActivity)?.setFabVisible(View.INVISIBLE)
        binding.save.setOnClickListener {
            val dialog = ConfirmDialog("保存しますか？",
                "保存", { saveNote(it) },
                "キャンセル", {
                    Snackbar.make(view, "キャンセルしました", Snackbar.LENGTH_SHORT)
                        .show()
                })
            dialog.show(parentFragmentManager, "save_dialog")
        }
        binding.delete.setOnClickListener {
            val dialog = ConfirmDialog("削除しますか？",
                "削除", { deleteNote(it) },
                "キャンセル", {
                    Snackbar.make(view, "キャンセルしました", Snackbar.LENGTH_SHORT)
                        .show()
                })
            dialog.show(parentFragmentManager, "delete_dialog")

        }

    }

    private fun saveNote(view: View) {
        when(args.noteId) {
            -1L -> {
                realm.executeTransaction { db: Realm ->
                    val maxId = db.where<Notepad>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val notepad = db.createObject<Notepad>(nextId)
                    val date = "${binding.dateEdit.text} ${binding.timeEdit.text}".toDate()
                    if (date != null) notepad.date = date
                    notepad.title = binding.titleEdit.text.toString()
                    notepad.detail = binding.detailEdit.text.toString()
                }

                Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る") { findNavController().popBackStack() }
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
            else -> {
                realm.executeTransaction { db: Realm ->
                    val notepad = db.where<Notepad>()
                        .equalTo("id", args.noteId).findFirst()
                    val date = "${binding.dateEdit.text} ${binding.timeEdit.text}".toDate()
                    if (date != null) notepad?.date = date
                    notepad?.title = binding.titleEdit.text.toString()
                    notepad?.detail = binding.detailEdit.text.toString()
                }

                Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る") { findNavController().popBackStack() }
                    .setActionTextColor(Color.YELLOW)
                    .show()

            }
        }

    }

    private fun deleteNote(view: View) {
        realm.executeTransaction { db: Realm ->
            db.where<Notepad>().equalTo("id", args.noteId)
                ?.findFirst()
                ?.deleteFromRealm()
        }

        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()

        findNavController().popBackStack()

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