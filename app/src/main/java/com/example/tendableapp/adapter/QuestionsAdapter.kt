package com.example.tendableapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tendableapp.R
import com.example.tendableapp.data.Response.Question

class QuestionsAdapter(private val questions: List<Question>,private val onAnswerSelected: (questionId: Int, answerChoiceId: Int) -> Unit) :
    RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.questionTextView)
        val answerChoicesRecyclerView: RecyclerView = itemView.findViewById(R.id.answerChoicesRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item_layout, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.questionTextView.text = question.name

        val answerChoicesAdapter = AnswerChoicesAdapter(question.answerChoices) { selectedAnswerChoiceId ->
            // Update the selected answer choice ID in the question
            question.selectedAnswerChoiceId = selectedAnswerChoiceId
            onAnswerSelected(question.id.toInt(), selectedAnswerChoiceId)
        }
        holder.answerChoicesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.answerChoicesRecyclerView.adapter = answerChoicesAdapter
    }

    override fun getItemCount(): Int = questions.size
}
