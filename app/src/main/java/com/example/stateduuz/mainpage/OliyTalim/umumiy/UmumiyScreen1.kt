package com.example.stateduuz.mainpage.OliyTalim.umumiy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.R
import com.example.stateduuz.ui.theme.BackgroundBlur
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorLightRed
import com.example.stateduuz.utils.CardUtils

@Composable
fun UmumiyScreen1(
    viewModel: UmumiyViewModel1 = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.error ?: "Xatolik", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Qayta urinib ko‘rish")
                    }
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Birinchi item: Oliy talim muassasalar soni
                    uiState.universitetAll?.let { universitetAll ->
                        item {
                            Column(modifier = Modifier.padding(top = 20.dp)) {
                                Row {
                                    CardUtils(
                                        maintext = "Oliy talim muassasalar soni",
                                        modifier = Modifier.weight(1f),
                                        icon = R.drawable.student_back,
                                        iconSize = 40.dp,
                                        lists = listOf(
                                            universitetAll.count { it.ownership_form == 11 },
                                            universitetAll.count { it.ownership_form == 12 },
                                            universitetAll.count { it.ownership_form == 13 }
                                        ),
                                        listsname = listOf("Davlat", "Nodavlat", "Xorijiy"),
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    CardUtils(
                                        maintext = "Professor-o'qituvchilar soni",
                                        modifier = Modifier.weight(1f),
                                        iconSize = 40.dp,
                                        icon = R.drawable.umumiy_prof,
                                        lists = listOf(
                                            uiState.ownershipAndGenderTeacher?.sumOf { it.maleCount } ?: 0,
                                            uiState.ownershipAndGenderTeacher?.sumOf { it.femaleCount } ?: 0
                                        ),
                                        listsname = listOf("Erkak", "Ayollar"),
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                CardUtils(
                                    maintext = "Talabalar Soni",
                                    modifier = Modifier.fillMaxWidth(),
                                    iconSize = 50.dp,
                                    icon = R.drawable.umumiy_3,
                                    lists = listOf(
                                        uiState.ownershipAndEduType?.sumOf { it.bachelorCount } ?: 0,
                                        uiState.ownershipAndEduType?.sumOf { it.masterCount } ?: 0,
                                        uiState.ownershipAndEduType?.sumOf { it.ordinaturaCount } ?: 0
                                    ),
                                    listsname = listOf("Bakalavr", "Magistratura", "Ordinatura"),
                                )
                            }
                        }
                    }

                    // Boshqa itemlar uchun kerakli to'ldirish
                    uiState.universityType?.let { type ->
                        item {
                            // Agar universityType uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "University Type: $type")
                        }
                    }

                    uiState.universityOwnership?.let { ownership ->
                        item {
                            // Agar universityOwnership uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "University Ownership: $ownership")
                        }
                    }

                    uiState.ownershipAndGender?.let { gender ->
                        item {
                            // Agar ownershipAndGender uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Gender: $gender")
                        }
                    }

                    uiState.ownershipAndEduType?.let { eduType ->
                        item {
                            // Agar ownershipAndEduType uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Edu Type: $eduType")
                        }
                    }

                    uiState.ownershipAndCourse?.let { course ->
                        item {
                            // Agar ownershipAndCourse uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Course: $course")
                        }
                    }

                    uiState.ownershipAndPaymentType?.let { paymentType ->
                        item {
                            // Agar ownershipAndPaymentType uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Payment Type: $paymentType")
                        }
                    }

                    uiState.ownershipAndEduForm?.let { eduForm ->
                        item {
                            // Agar ownershipAndEduForm uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Edu Form: $eduForm")
                        }
                    }

                    uiState.universityAddress?.let { address ->
                        item {
                            // Agar universityAddress uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "University Address: $address")
                        }
                    }

                    uiState.studentAddress?.let { studentAddress ->
                        item {
                            // Agar studentAddress uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Student Address: $studentAddress")
                        }
                    }

                    uiState.topFiveUniversity?.let { topFive ->
                        item {
                            // Agar topFiveUniversity uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Top Five Universities: $topFive")
                        }
                    }

                    uiState.ownership?.let { ownership ->
                        item {
                            // Agar ownership uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership: $ownership")
                        }
                    }

                    uiState.ownershipAndGenderTeacher?.let { teacherGender ->
                        item {
                            // Agar ownershipAndGenderTeacher uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Gender Teacher: $teacherGender")
                        }
                    }

                    uiState.ownershipAndAcademicRank?.let { academicRank ->
                        item {
                            // Agar ownershipAndAcademicRank uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Academic Rank: $academicRank")
                        }
                    }

                    uiState.ownershipAndAcademicDegree?.let { academicDegree ->
                        item {
                            // Agar ownershipAndAcademicDegree uchun biror UI kerak bo'lsa, shu yerga joylashtiring
                            Text(text = "Ownership And Academic Degree: $academicDegree")
                        }
                    }
                }
            }
        }
    }
}

