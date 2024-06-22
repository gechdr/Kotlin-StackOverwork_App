package com.example.tugasm7_6958

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AnswerAdapter(
    val data: MutableList<AnswerEntity>
) : RecyclerView.Adapter<AnswerAdapter.ViewHolder>() {
    class ViewHolder(val row: View) : RecyclerView.ViewHolder(row){
        val tvAnswer: TextView = row.findViewById(R.id.AI_tvAnswer)
        val tvOwner: TextView = row.findViewById(R.id.AI_tvOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.answer_item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val a = data[position]

        holder.tvAnswer.text = a.answer
        holder.tvOwner.text = a.owner
    }

    override fun getItemCount(): Int {
        return data.size
    }
}