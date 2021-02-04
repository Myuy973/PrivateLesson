package com.example.privatelesson

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.privatelesson.databinding.FragmentSecondBinding
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*

class SecondFragment : Fragment() {

    var _binding: FragmentSecondBinding? = null
    val binding get() = _binding!!
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        (activity as? NoteActivity)?.setFabVisible(View.INVISIBLE)
        binding.list.layoutManager = LinearLayoutManager(context)
        var dateTime = Calendar.getInstance().apply {
            // 日付・日時のコード（数字の羅列。例: 1612452375989)
            timeInMillis = binding.calendarView.date
        }
        findNote(
            // 正確な日時に変換
            dateTime.get(Calendar.YEAR),
            // 1ヵ月少ないがおそらく月は0からスタートしてると思われる
            dateTime.get(Calendar.MONTH),
            dateTime.get(Calendar.DAY_OF_MONTH)
        )
        binding.calendarView
            .setOnDateChangeListener { view, year, month, dayOfMonth ->
                findNote(year, month, dayOfMonth)
            }
    }

    private fun findNote(
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        var selectDate = Calendar.getInstance().apply {
            clear()
            set(year, month, dayOfMonth)
        }
        val notes = realm.where<Notepad>()
            .between(
                // 日付または時間で範囲指定
                "date",
                // 00:00:00
                selectDate.time,
                // 23:59:59
                selectDate.apply {
                    add(Calendar.DAY_OF_MONTH, 1)
                    add(Calendar.MILLISECOND, -1)
                }.time
            ).findAll().sort("date")
        val adapter = NotepadAdapter(notes)
        binding.list.adapter = adapter

        adapter.setOnItemClickListener { id ->
            id?.let {
                val action =
                    SecondFragmentDirections.actionToNoteEditFragment(it)
                findNavController().navigate(action)
            }
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