package com.example.stateduuz.model.Professional.profEduType

data class ProfEduType(
    val by_admission_type: List<ByAdmissionType>,
    val by_age: List<ByAge>,
    val by_citizenship: List<ByCitizenship>,
    val by_course: List<ByCourse>,
    val by_current_live_place: List<ByCurrentLivePlace>,
    val by_education_form: List<ByEducationForm>,
    val by_gender: List<ByGender>
)