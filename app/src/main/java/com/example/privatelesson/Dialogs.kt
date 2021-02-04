package com.example.privatelesson

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.sql.Time
import java.util.*

class ConfirmDialog(private val message: String,
                    private val okLabel: String,
                    private val okSelected: () -> Unit,
                    private val cancelLabel: String,
                    private val cancelSelected: () -> Unit)
    : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        // メッセージを設定
        builder.setMessage(message)
        // OK
        builder.setPositiveButton(okLabel) { dialog, which ->
            okSelected()
        }
        // CANCEL
        builder.setNegativeButton(cancelLabel) { dialog, which ->
            cancelSelected()
        }
        return builder.create()
    }
}

class DateDialog(private val onSelected: (String) -> Unit)
    : DialogFragment(), DatePickerDialog.OnDateSetListener {

    // ダイアログ表示
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        // 現在の日付を初期設定として表示
        return DatePickerDialog(requireActivity(), this, year, month, date)
    }

    // ダイアログの値を設定
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // monthはカウントが0からのスタートのため+1することで補正している
        onSelected("$year/${month + 1}/$dayOfMonth")
    }

}

class TimeDialog(private val onSelected: (String) -> Unit)
    : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    // ダイアログ表示
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        // 現在の時間を初期設定として表示
        return TimePickerDialog(context, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        onSelected("%1$02d:%2$02d".format(hourOfDay, minute))
    }

}
