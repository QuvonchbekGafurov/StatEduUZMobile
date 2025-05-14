package com.example.stateduuz.model.Professional.profAdmissionType

data class ProfAdmissionType(
    val by_age: List<ByAge>,
    val by_citizenship: List<ByCitizenship>,
    val by_course: List<ByCourse>,
    val by_current_live_place: List<ByCurrentLivePlace>,
    val by_gender: List<ByGender>
)