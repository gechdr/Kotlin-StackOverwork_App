package com.example.tugasm7_6958

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(
    val data: MutableList<QuestionEntity>,
    var onDetailClickListener:((QuestionEntity)->Unit)? = null,
): RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    class ViewHolder(val row: View) : RecyclerView.ViewHolder(row){
        val tvTitle: TextView = row.findViewById(R.id.HQ_tvTitle)
        val tvAnswer: TextView = row.findViewById(R.id.HQ_tvAnswer)
        val tvTag1: TextView = row.findViewById(R.id.HQ_tvTag1)
        val tvTag2: TextView = row.findViewById(R.id.HQ_tvTag2)
        val tvOwner: TextView = row.findViewById(R.id.HQ_tvOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.home_question_item, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val q = data[position]

        holder.tvTitle.text = q.title
        holder.tvAnswer.text = "${q.answers} answers"

        val tags = q.tags.split(",")
        holder.tvTag1.text = tags[0]
        holder.tvTag2.text = tags[1]

        holder.tvOwner.text = q.owner

        holder.tvTitle.setOnClickListener {
            onDetailClickListener?.invoke(q)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}