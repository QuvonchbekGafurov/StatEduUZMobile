package com.example.stateduuz.data.repository

import android.util.Log
import com.example.stateduuz.data.retrofit.StudentApiService
import com.example.stateduuz.model.Doctorantura
import com.example.stateduuz.model.Professional.profAdmissionType.ProfAdmissionType
import com.example.stateduuz.model.Professional.profAge.ProfAge
import com.example.stateduuz.model.Professional.profEduForm.ProfEduForm
import com.example.stateduuz.model.Professional.profEduType.ProfEduType
import com.example.stateduuz.model.Professional.profcitizenship.ProfCitizenship
import com.example.stateduuz.model.Professional.profcourse.ProfCourse
import com.example.stateduuz.model.Professional.profcurrentLive.ProfCurrentLive
import com.example.stateduuz.model.Professional.region.ProfRegion
import com.example.stateduuz.model.UniversityOwnership.universityOwnership
import com.example.stateduuz.model.accommodationAndGender.accommodationAndGender
import com.example.stateduuz.model.ageAndAccomodation.ageAndAccomodation
import com.example.stateduuz.model.ageAndGender.ageAndGender
import com.example.stateduuz.model.citizenshipAndAge.citizenshipAndAge
import com.example.stateduuz.model.citizenshipAndCourse.citizenshipAndCourse
import com.example.stateduuz.model.citizenshipAndGender.citizenshipAndGender
import com.example.stateduuz.model.courseAndAccomodation.courseAndAccomodation
import com.example.stateduuz.model.courseAndAge.courseAndAge
import com.example.stateduuz.model.courseAndGender.courseAndGender
import com.example.stateduuz.model.eduTypeAndAccommodation.eduTypeAndAccommodation
import com.example.stateduuz.model.eduTypeAndAge.eduTypeAndAge
import com.example.stateduuz.model.eduTypeAndCitizenship.eduTypeAndCitizenship
import com.example.stateduuz.model.eduTypeAndCourse.eduTypeAndCourse
import com.example.stateduuz.model.eduTypeAndEduForm.eduTypeAndEduForm
import com.example.stateduuz.model.universityAll.UniversitetAll
import com.example.stateduuz.model.eduTypeAndGander.eduTypeAndGender
import com.example.stateduuz.model.eduTypeAndPaymentType.eduTypeAndPaymentType
import com.example.stateduuz.model.educationFormAndAccommodation.educationFormAndAccommodation
import com.example.stateduuz.model.educationFormAndAge.educationFormAndAge
import com.example.stateduuz.model.educationFormAndCitizenship.educationFormAndCitizenship
import com.example.stateduuz.model.educationFormAndCourse.educationFormAndCourse
import com.example.stateduuz.model.educationFormAndGender.educationFormAndGender
import com.example.stateduuz.model.educationFormAndPaymentForm.educationFormAndPaymentForm
import com.example.stateduuz.model.muassasalar.muassasalar
import com.example.stateduuz.model.ownership.ownership
import com.example.stateduuz.model.ownershipAndAcademicDegree.ownershipAndAcademicDegree
import com.example.stateduuz.model.ownershipAndAcademicRank.ownershipAndAcademicRank
import com.example.stateduuz.model.ownershipAndEduForm.ownershipAndEduForm
import com.example.stateduuz.model.ownershipAndEduType.ownershipAndEduType
import com.example.stateduuz.model.ownershipAndGender.ownershipAndGender
import com.example.stateduuz.model.ownershipAndPaymentType.ownershipAndPaymentType
import com.example.stateduuz.model.ownershipCourse.ownershipAndCourse
import com.example.stateduuz.model.paymentTypeAndAccommodation.paymentTypeAndAccommodation
import com.example.stateduuz.model.paymentTypeAndAge.paymentTypeAndAge
import com.example.stateduuz.model.paymentTypeAndCitizenship.paymentTypeAndCitizenship
import com.example.stateduuz.model.paymentTypeAndCourse.paymentTypeAndCourse
import com.example.stateduuz.model.paymentTypeAndGender.paymentTypeAndGender
import com.example.stateduuz.model.regionAndEduForm.regionAndEduForm
import com.example.stateduuz.model.regionAndEduTpe.regionAndEduType
import com.example.stateduuz.model.regionAndGender.regionAndGender
import com.example.stateduuz.model.studentAddress.studentAddress
import com.example.stateduuz.model.techerStatisticAcademicDegree.techerStatisticAcademicDegree
import com.example.stateduuz.model.techerStatisticAcademicRank.techerStatisticAcademicRank
import com.example.stateduuz.model.techerStatisticAge.techerStatisticAge
import com.example.stateduuz.model.techerStatisticChiefPosition.techerStatisticChiefPosition
import com.example.stateduuz.model.techerStatisticCitizenship.techerStatisticCitizenship
import com.example.stateduuz.model.techerStatisticEmployeeForm.techerStatisticEmployeeForm
import com.example.stateduuz.model.techerStatisticGender.techerStatisticGender
import com.example.stateduuz.model.techerStatisticPosition.techerStatisticPosition
import com.example.stateduuz.model.topFiveUniversity.topFiveUniversity
import com.example.stateduuz.model.universitety.University
import com.example.stateduuz.model.universityAddress.universityAddress
import com.example.stateduuz.model.universityType.UniversityType
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import kotlin.math.log


@ActivityRetainedScoped
class UniversitetRepository @Inject constructor(
    private val apiService: StudentApiService
) {
    suspend fun getUniversitetAll(): UniversitetAll {
        return apiService.getStudents()
    }
    suspend fun getMuassasalar(): muassasalar {
        return apiService.getMuassasalar()
    }
    suspend fun getUniversityOwnership(): universityOwnership {
        return apiService.getUniversityOwnership()
    }

    suspend fun getEduTypeAndGender(): eduTypeAndGender {
        return apiService.getEduTypeAndGender()
    }
    suspend fun getEducationFormAndCitizenship(): educationFormAndCitizenship {
        return apiService.getEducationFormAndCitizenship()
    }
    suspend fun getUniversityType(): UniversityType {
        return apiService.getUniversityType()
    }
    suspend fun getOwnershipAndGender(): ownershipAndGender {
        return apiService.getOwnershipAndGender()
    }
    suspend fun getOwnershipAndEduType(): ownershipAndEduType {
        return apiService.getOwnershipAndEduType()
    }
    suspend fun getOwnershipAndCourse(): ownershipAndCourse {
        return apiService.getOwnershipAndCourse()
    }
    suspend fun getOwnershipAndPaymentType(): ownershipAndPaymentType {
        return apiService.getOwnershipAndPaymentType()
    }
    suspend fun getOwnershipAndEduForm(): ownershipAndEduForm {
        return apiService.getOwnershipAndEduForm()
    }
    suspend fun getUniversityAddress(): universityAddress {
        return apiService.getUniversityAddress()
    }
    suspend fun getStudentAddress(): studentAddress {
        return apiService.getStudentAddress()
    }
    suspend fun getTopFiveUniversity(): topFiveUniversity {
        return apiService.getTopFiveUniversity()
    }
    suspend fun getOwnership(): ownership {
        return apiService.getOwnership()
    }
    suspend fun getCourseAndAccommodation(): courseAndAccomodation {
        return apiService.getcourseAndAccomodation()
    }
    suspend fun getAgeAndAccommodation(): ageAndAccomodation {
        return apiService.getAgeAndAccomodation()
    }

    suspend fun getOwnershipAndGenderTeacher(): ownershipAndGender {
        return apiService.getOwnershipAndGenderTeacher()
    }
    suspend fun getOwnershipAndAcademicRank(): ownershipAndAcademicRank {
        return apiService.ownershipAndAcademicRank()
    }
    suspend fun getOwnershipAndAcademicDegree(): ownershipAndAcademicDegree {
        return apiService.getOwnershipAndAcademicDegree()
    }
    suspend fun getEduTypeAndAge(): eduTypeAndAge{
        return apiService.getEduTypeAndAge()
    }
    suspend fun getEduTypeAndPaymentType(): eduTypeAndPaymentType {
        return apiService.getEduTypeAndPaymentType()
    }
    suspend fun getEduTypeAndCourse(): eduTypeAndCourse {
        return apiService.getEduTypeAndCourse()
    }
    suspend fun getEduTypeAndCitizenship(): eduTypeAndCitizenship {
        return apiService.getEduTypeAndCitizenship()
    }
    suspend fun getEduTypeAndAccommodation(): eduTypeAndAccommodation {
        return apiService.getEduTypeAndAccommodation()
    }
    suspend fun getEduTypeAndEduForm(): eduTypeAndEduForm{
        return apiService.getEduTypeAndEduForm()
    }
    suspend fun getEducationFormAndGender(): educationFormAndGender {
        return apiService.getEducationFormAndGender()
    }
    suspend fun getEducationFormAndAge(): educationFormAndAge {
        return apiService.getEducationFormAndAge()
    }
    suspend fun getEducationFormAndPaymentForm(): educationFormAndPaymentForm{
        return apiService.getEducationFormAndPaymentForm()
    }
    suspend fun getEducationFormAndCourse(): educationFormAndCourse {
        return apiService.getEducationFormAndCourse()
    }
    suspend fun getEducationFormAndAccommodation(): educationFormAndAccommodation {
        return apiService.getEducationFormAndAccommodation()
    }
    suspend fun getPaymentTypeAndGender(): paymentTypeAndGender {
        return apiService.getPaymentTypeAndGender()
    }
    suspend fun getPaymentTypeAndAge(): paymentTypeAndAge{
        return apiService.getPaymentTypeAndAge()
    }
    suspend fun getPaymentTypeAndCitizenship(): paymentTypeAndCitizenship {
        return apiService.getPaymentTypeAndCitizenship()
    }
    suspend fun getPaymentTypeAndCourse(): paymentTypeAndCourse {
        return apiService.getPaymentTypeAndCourse()
    }
    suspend fun getPaymentTypeAndAccommodation(): paymentTypeAndAccommodation {
        return apiService.getPaymentTypeAndAccommodation()
    }
    suspend fun getCitizenshipAndGender(): citizenshipAndGender{
        return apiService.getCitizenshipAndGender()
    }
    suspend fun getCitizenshipAndAge(): citizenshipAndAge {
        return apiService.getCitizenshipAndAge()
    }
    suspend fun getCitizenshipAndCourse(): citizenshipAndCourse{
        return apiService.getCitizenshipAndCourse()
    }
    suspend fun getCourseAndGender(): courseAndGender {
        return apiService.getCourseAndGender()
    }
    suspend fun getCourseAndAge(): courseAndAge {
        return apiService.getCourseAndAge()
    }
    suspend fun getAgeAndGender(): ageAndGender {
        return apiService.getAgeAndGender()
    }
    suspend fun getAccommodationAndGender(): accommodationAndGender {
        return apiService.getAccommodationAndGender()
    }
    suspend fun getRegionAndGender(): regionAndGender {
        return apiService.getRegionAndGender()
    }
    suspend fun getRegionAndEduType(): regionAndEduType {
        return apiService.getRegionAndEduType()
    }
    suspend fun getRegionAndEduForm(): regionAndEduForm {
        return apiService.getregionAndEduForm()
    }
    suspend fun getRegionAndCourse(): regionAndGender{
        return apiService.getRegionAndCourse()
    }
    suspend fun getRegionAndAge(): regionAndGender {
        return apiService.getRegionAndAge()
    }
    suspend fun getRegionAndPaymentType(): regionAndGender {
        return apiService.getRegionAndPaymentType()
    }
    suspend fun getRegionAndCitizenship(): regionAndGender {
        return apiService.getRegionAndCitizenship()
    }
    suspend fun getRegionAndAccommodation(): regionAndGender {
        return apiService.getRegionAndAccommodation()
    }





    suspend fun getTeacherStatisticGender(): techerStatisticGender {
        return apiService.getTeacherStatisticGender()
    }
    suspend fun getTeacherStatisticChiefPosition(): techerStatisticChiefPosition{
        return apiService.getTeacherStatisticChiefPosition()
    }
    suspend fun getTeacherStatisticAcademicDegree(): techerStatisticAcademicDegree {
        return apiService.getTeacherStatisticAcademicDegree()
    }
    suspend fun getTeacherStatisticAcademicRank(): techerStatisticAcademicRank{
        return apiService.getTeacherStatisticAcademicRank()
    }
    suspend fun getTeacherStatisticPosition(): techerStatisticPosition {
        return apiService.getTeacherStatisticPosition()
    }
    suspend fun getTeacherStatisticEmployeeForm(): techerStatisticEmployeeForm {
        return apiService.getTeacherStatisticEmployeeForm()
    }
    suspend fun getTeacherStatisticCitizenship(): techerStatisticCitizenship {
        return apiService.getTeacherStatisticCitizenship()
    }
    suspend fun getTeacherStatisticAge(): techerStatisticAge {
        return apiService.getTeacherStatisticAge()
    }
    suspend fun getUniversity():University{
        return  apiService.getUniversity()
    }

    suspend fun getEducationTypes(): ProfEduType {
        val result = apiService.getEducationTypes()
        Log.e("TAG", "getEducationTypes: $result")
        return result
    }

    suspend fun getEducationForms(): ProfEduForm {
        return apiService.getEducationForms()
    }

    suspend fun getAdmissionTypes(): ProfAdmissionType {
        return apiService.getAdmissionTypes()
    }

    suspend fun getCitizenship(): ProfCitizenship {
        return apiService.getCitizenship()
    }

    suspend fun getCourses(): ProfCourse {
        return apiService.getCourses()
    }

    suspend fun getAges(): ProfAge {
        return apiService.getAges()
    }

    suspend fun getCurrentLivePlaces(): ProfCurrentLive {
        return apiService.getCurrentLivePlaces()
    }

    suspend fun getRegions(): ProfRegion {
        return apiService.getRegions()
    }

    suspend fun getDoctorantura():Doctorantura{
        return apiService.getDoctorantura()
        Log.e("TAG", "getDoctorantura: ${apiService.getDoctorantura()}", )
    }

}
