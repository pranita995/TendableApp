package com.example.tendableapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tendableapp.R
import com.example.tendableapp.data.Response.Category

class CategoriesAdapter(private var categories: List<Category>,
                        private val onAnswerSelected: (questionId: Int, answerChoiceId: Int) -> Unit) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val questionsRecyclerView: RecyclerView = itemView.findViewById(R.id.questionsRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryTextView.text = category.name

        val questionsAdapter = QuestionsAdapter(category.questions,onAnswerSelected)
        holder.questionsRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.questionsRecyclerView.adapter = questionsAdapter
    }

    override fun getItemCount(): Int = categories.size

    // Function to update data in the adapter
    fun setData(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
