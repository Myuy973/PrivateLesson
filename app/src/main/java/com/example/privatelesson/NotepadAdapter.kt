package com.example.privatelesson

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class NotepadAdapter(data: OrderedRealmCollection<Notepad>) :
    RealmRecyclerViewAdapter<Notepad, NotepadAdapter.ViewHolder>(data, true) {

    private var listener: ((Long?) -> Unit)? = null

    fun setOnItemClickListener(listener: (Long?) -> Unit) {
        this.listener = listener
    }

    init {
        // このアダプターでは、データ内の１つの項目を指し示すために固有のID(キー)を使う
        // @Primaryを有効にする
        setHasStableIds(true)
    }

    // セルの値を入れる位置を保存
    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell) {
        // TextViewのID(位置)を代入
        val date: TextView = cell.findViewById(android.R.id.text1)
        val text: TextView = cell.findViewById(android.R.id.text2)
    }

    // セルが必要になるたびに呼び出される
    // セルに必要な値が入ったViewHolderを返す
    // 値の入った箱を渡す処理を担当（表示まではやらない）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotepadAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    // それぞれのセルにセットされているViewHolderの値をTextViewにセット
    // 表示を担当
    override fun onBindViewHolder(holder: NotepadAdapter.ViewHolder, position: Int) {
        val notepad: Notepad? = getItem(position)
        // それぞれ指定された位置に、必要な値を代入
        holder.date.text = DateFormat.format("yyyy/MM/dd HH:mm", notepad?.date)
        holder.text.text = notepad?.text
        holder.itemView.setOnClickListener {
            listener?.invoke(notepad?.id)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: 0
    }

}