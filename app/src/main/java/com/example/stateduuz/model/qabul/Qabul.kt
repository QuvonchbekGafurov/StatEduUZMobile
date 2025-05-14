package com.example.stateduuz.model.qabul

data class StatisticsQabul(
    val title: String,
    val categories: List<YearCategory>
)

data class YearCategory(
    val name: String, // Yil (Masalan: "2021-2022")
    val data: List<TypeData>
)

data class TypeData(
    val type: String, // O‘quv turi (Masalan: "Bakalavr")
    val applications: Int
)

