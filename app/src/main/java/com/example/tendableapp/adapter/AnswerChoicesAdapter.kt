package com.example.tendableapp.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tendableapp.R
import com.example.tendableapp.data.Response.AnswerChoice

class AnswerChoicesAdapter(private val answerChoices: List<AnswerChoice>, private val onAnswerSelected: (Int) -> Unit ) :
    RecyclerView.Adapter<AnswerChoicesAdapter.AnswerChoiceViewHolder>() {

    // ViewHolder class to hold the view references
    class AnswerChoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerChoiceTextView: TextView = itemView.findViewById(R.id.answerChoiceTextView)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
    }

    // Inflating the item layout and creating the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerChoiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.answer_choice_item_layout, parent, false)
        return AnswerChoiceViewHolder(view)
    }

    // Binding data to the view holder
    override fun onBindViewHolder(holder: AnswerChoiceViewHolder, position: Int) {
        val answerChoice = answerChoices[position]
        holder.answerChoiceTextView.text = answerChoice.name
        // You can handle checkbox selection logic here
        holder.checkbox.isChecked = answerChoice.id.toInt() == holder.adapterPosition

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onAnswerSelected(answerChoice.id.toInt())
            }
        }
    }

    // Returning the size of the list
    override fun getItemCount(): Int = answerChoices.size
}
