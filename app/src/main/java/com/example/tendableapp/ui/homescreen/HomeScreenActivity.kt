package com.example.tendableapp.ui.homescreen

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tendableapp.R
import com.example.tendableapp.adapter.CategoriesAdapter
import com.example.tendableapp.data.Response.InspectionResponse
import com.example.tendableapp.data.Response.Question
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var homeScreenViewModel: HomeScreenViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var submitButton: Button
    private lateinit var originalInspectionResponse: InspectionResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        // Initialize ViewModel
        homeScreenViewModel = ViewModelProvider(this, HomeViewModelFactory())
            .get(HomeScreenViewModel::class.java)

        // Set RecyclerView adapter
        val recyclerView: RecyclerView = findViewById(R.id.answerChoicesRecyclerView)
        categoriesAdapter = CategoriesAdapter(emptyList(), ::onAnswerSelected  )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = categoriesAdapter
        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Observe LiveData for inspection data
        homeScreenViewModel._dataFetch.observe(this, Observer { inspectionResponse ->
            inspectionResponse?.let {
                // Update adapter data when LiveData changes
                originalInspectionResponse = it
                categoriesAdapter.setData(it.inspection.survey.categories)
            }
        })

        homeScreenViewModel._notifySucess.observe(this, Observer { success ->
            if (success) {
                displayToastMessage()
            }
        })

        // Fetch and save inspection data
        homeScreenViewModel.fetchAndSaveInspectionData()
        // Find the submit button by its ID
        submitButton = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            // Assuming inspectionResponse is the updated model containing user answers
            val inspectionResponse = createUpdatedInspectionResponse(originalInspectionResponse)
            GlobalScope.launch {
                homeScreenViewModel.submitInspectionData(inspectionResponse)
            }
        }
    }

    private fun displayToastMessage() {
        Toast.makeText(this, "SuccessFully Sumbitted", Toast.LENGTH_SHORT).show()
    }

    private fun onAnswerSelected(questionId: Int, answerChoiceId: Int) {
        // Logic to handle answer choice selection
        val question = originalInspectionResponse.inspection.survey.categories
            .flatMap { it.questions }
            .find { it.id.toInt() == questionId }

        question?.selectedAnswerChoiceId = answerChoiceId
    }

    private fun createUpdatedInspectionResponse(originalInspectionResponse: InspectionResponse): InspectionResponse {
        val updatedCategories = originalInspectionResponse.inspection.survey.categories.map { category ->
            val updatedQuestions = category.questions.map { question ->
                val selectedAnswerChoiceId = question.selectedAnswerChoiceId

                if (selectedAnswerChoiceId != null) {
                    val updatedAnswerChoices = question.answerChoices.map { answerChoice ->
                        if (answerChoice.id == selectedAnswerChoiceId) {
                            answerChoice.copy() // Or any specific update if needed
                        } else {
                            answerChoice
                        }
                    }
                    question.copy(answerChoices = updatedAnswerChoices, selectedAnswerChoiceId = selectedAnswerChoiceId)
                } else {
                    question
                }
            }
            category.copy(questions = updatedQuestions)
        }

        val updatedSurvey = originalInspectionResponse.inspection.survey.copy(categories = updatedCategories)
        val updatedInspection = originalInspectionResponse.inspection.copy(survey = updatedSurvey)

        return originalInspectionResponse.copy(inspection = updatedInspection)
    }



}
