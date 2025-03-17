package com.example.stateduuz.model.universitetall


data class StatisticsQabul(
    val title: String,
    val categories: List<Category>
)

data class Category(
    val name: String,
    val data: List<ApplicationData>
)

data class ApplicationData(
    val year: String,
    val applications: Int
)
