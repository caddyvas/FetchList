package com.learn.fetchlist.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.learn.fetchlist.R
import com.learn.fetchlist.data.HiringList

class ListViewAdapter(private val context: Context, private val hiringList: List<HiringList>) : BaseAdapter() {

    private val inflater: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return hiringList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.row_list_view, parent, false)
        val idText: TextView = rowView.findViewById(R.id.row_id)
        val listIdText: TextView = rowView.findViewById(R.id.row_list_id)
        val nameText: TextView = rowView.findViewById(R.id.row_name)

        idText.text = hiringList[position].id.toString()
        listIdText.text = hiringList[position].listId.toString()
        nameText.text = hiringList[position].name
         return rowView
    }
}