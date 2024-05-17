package com.example.tendableapp.data.Response

data class InspectionResponse(
    val inspection: Inspection,
)

data class Inspection(
    val area: Area,
    val id: Long,
    val inspectionType: InspectionType,
    val survey: Survey,
)

data class Area(
    val id: Long,
    val name: String,
)

data class InspectionType(
    val access: String,
    val id: Long,
    val name: String,
)

data class Survey(
    val categories: List<Category>,
    val id: Long,
)

data class Category(
    val id: Long,
    val name: String,
    val questions: List<Question>,
)

data class Question(
    val answerChoices: List<AnswerChoice>,
    val id: Long,
    val name: String,
    var selectedAnswerChoiceId: Any?,
)

data class AnswerChoice(
    val id: Long,
    val name: String,
    val score: Double
)
