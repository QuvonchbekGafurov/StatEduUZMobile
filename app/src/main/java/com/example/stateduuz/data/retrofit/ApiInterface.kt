package com.example.stateduuz.data.retrofit

import com.example.stateduuz.model.Doctorantura
import com.example.stateduuz.model.Professional.profAdmissionType.ProfAdmissionType
import com.example.stateduuz.model.Professional.profAge.ProfAge
import com.example.stateduuz.model.Professional.profEduForm.ProfEduForm
import com.example.stateduuz.model.Professional.profEduType.ProfEduType
import com.example.stateduuz.model.Professional.profRegionSection.ProfRegionSection
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
import com.example.stateduuz.model.ownershipCourse.ownershipAndCourse
import com.example.stateduuz.model.ownershipAndEduForm.ownershipAndEduForm
import com.example.stateduuz.model.ownershipAndEduType.ownershipAndEduType
import com.example.stateduuz.model.ownershipAndGender.ownershipAndGender
import com.example.stateduuz.model.ownershipAndPaymentType.ownershipAndPaymentType
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
import retrofit2.http.GET

interface StudentApiService {

    //Umumiy

    //Muassasalar
    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/tc?&limit=10&offset=0")
    suspend fun getMuassasalar():muassasalar
    //OTMlar soni mulkchilik shakli bo'yicha
    @GET("statistic/table/student/region?paymentType=all&eduType=all")
    suspend fun getStudents(): UniversitetAll

    @GET("https://stat.edu.uz/api/statistic/common/university/ownership")
    suspend fun getUniversityOwnership(): universityOwnership

    // Otmlar soni tashkilot bo'yicha
    @GET("https://stat.edu.uz/api/statistic/common/university/universityType")
    suspend fun getUniversityType(): UniversityType

    // Talabalar soni jins kesimida
    @GET("https://stat.edu.uz/api/statistic/common/student/ownershipAndGender")
    suspend fun getOwnershipAndGender(): ownershipAndGender

    //Talabalar soni ta'lim turi kesimida
    @GET("https://stat.edu.uz/api/statistic/common/student/ownershipAndEduType")
    suspend fun getOwnershipAndEduType(): ownershipAndEduType

    //Talabalar soni kurslar kesimida
    @GET("https://stat.edu.uz/api/statistic/common/student/ownershipAndCourse")
    suspend fun getOwnershipAndCourse(): ownershipAndCourse

    //Talabalar soni to'lov shakli kesimida
    @GET("https://stat.edu.uz/api/statistic/common/student/ownershipAndPaymentType")
    suspend fun getOwnershipAndPaymentType(): ownershipAndPaymentType

    //Talabalar soni ta'lim shakli kesimida
    @GET("https://stat.edu.uz/api/statistic/common/student/ownershipAndEduForm")
    suspend fun getOwnershipAndEduForm(): ownershipAndEduForm


    //Respublikada ta'lim olayotgan talabalar soni OTM joylashgan hudud kesimida
    //Talabalar eng zich joylashgan top 5 ta hudud ,Sort qilish kerak
    @GET("https://stat.edu.uz/api/statistic/common/student/universityAddress")
    suspend fun getUniversityAddress(): universityAddress

    //Respublikada ta'lim olayotgan talabalar soni doimiy yashash viloyati kesimida
    //Eng ko'p oliy ma'lumotli kadrlar yetishib chiqayotgan top 5 ta hudud  //Sort qilib olinadi
    @GET("https://stat.edu.uz/api/statistic/common/student/studentAddress")
    suspend fun getStudentAddress(): studentAddress

    //Eng ko'p talabaga ega top 5 ta OTM
    @GET("https://stat.edu.uz/api/statistic/common/student/topFiveUniversity")
    suspend fun getTopFiveUniversity(): topFiveUniversity

    //Talabalar soni OTM mulkchilik shakli kesimida
    @GET("https://stat.edu.uz/api/statistic/common/student/ownership")
    suspend fun getOwnership(): ownership

    //Professor-o'qituvchilar jins kesimida
    @GET("https://stat.edu.uz/api/statistic/common/teacher/ownershipAndGender")
    suspend fun getOwnershipAndGenderTeacher(): ownershipAndGender

    //Professor-o'qituvchilar ilmiy unvon kesimida
    @GET("https://stat.edu.uz/api/statistic/common/teacher/ownershipAndAcademicRank")
    suspend fun ownershipAndAcademicRank(): ownershipAndAcademicRank

    //Professor-o'qituvchilar ilmiy daraja kesimida
    //buni tepada qo'llab ket
    @GET("https://stat.edu.uz/api/statistic/common/teacher/ownershipAndAcademicDegree")
    suspend fun getOwnershipAndAcademicDegree(): ownershipAndAcademicDegree


    //Talabalar ekrani asosiy
    @GET("https://stat.edu.uz/api/student/statistic/eduType/eduTypeAndGender")
    suspend fun getEduTypeAndGender(): eduTypeAndGender

    //Yoshi
    @GET("https://stat.edu.uz/api/student/statistic/eduType/eduTypeAndAge")
    suspend fun getEduTypeAndAge(): eduTypeAndAge

    //To'lov shakli
    @GET("https://stat.edu.uz/api/student/statistic/eduType/eduTypeAndPaymentType")
    suspend fun getEduTypeAndPaymentType(): eduTypeAndPaymentType

    //Kurslar
    //Kurslar (Bakalavr)
    // Kurslar (Magistratura) 38 064
    @GET("https://stat.edu.uz/api/student/statistic/eduType/eduTypeAndCourse")
    suspend fun getEduTypeAndCourse(): eduTypeAndCourse


    //Fuqaroligi
    @GET("https://stat.edu.uz/api/student/statistic/eduType/eduTypeAndCitizenship")
    suspend fun getEduTypeAndCitizenship(): eduTypeAndCitizenship


    //Yashash joyi
    @GET("https://stat.edu.uz/api/student/statistic/eduType/eduTypeAndAccommodation")
    suspend fun getEduTypeAndAccommodation(): eduTypeAndAccommodation

    //Ta'lim shakli
    //Ta'lim shakli (Bakalavr)
    //Ta'lim shakli (Magistratura)
    @GET("https://stat.edu.uz/api/student/statistic/eduType/eduTypeAndEduForm")
    suspend fun getEduTypeAndEduForm(): eduTypeAndEduForm


    //TALIM SHAKLI

    //Jinsi bo'yicha
    @GET("https://stat.edu.uz/api/student/statistic/eduForm/educationFormAndGender")
    suspend fun getEducationFormAndGender(): educationFormAndGender

    //Yoshi
    @GET("https://stat.edu.uz/api/student/statistic/eduForm/educationFormAndAge")
    suspend fun getEducationFormAndAge(): educationFormAndAge

    //To'lov shakli
    @GET("https://stat.edu.uz/api/student/statistic/eduForm/educationFormAndPaymentForm")
    suspend fun getEducationFormAndPaymentForm(): educationFormAndPaymentForm

    @GET("https://stat.edu.uz/api/student/statistic/eduForm/educationFormAndCitizenship")
    suspend fun getEducationFormAndCitizenship(): educationFormAndCitizenship

    //Kurslar
    @GET("https://stat.edu.uz/api/student/statistic/eduForm/educationFormAndCourse")
    suspend fun getEducationFormAndCourse(): educationFormAndCourse

    //Yashash joyi
    @GET("https://stat.edu.uz/api/student/statistic/eduForm/educationFormAndAccommodation")
    suspend fun getEducationFormAndAccommodation(): educationFormAndAccommodation


    //TO'LOV SHAKLI
    //Jinsi bo'yicha
    @GET("https://stat.edu.uz/api/student/statistic/paymentType/paymentTypeAndGender")
    suspend fun getPaymentTypeAndGender(): paymentTypeAndGender

    //Yoshi
    @GET("https://stat.edu.uz/api/student/statistic/paymentType/paymentTypeAndAge")
    suspend fun getPaymentTypeAndAge(): paymentTypeAndAge

    //Fuqaroligi
    @GET("https://stat.edu.uz/api/student/statistic/paymentType/paymentTypeAndCitizenship")
    suspend fun getPaymentTypeAndCitizenship(): paymentTypeAndCitizenship

    //Kurslar
    @GET("https://stat.edu.uz/api/student/statistic/paymentType/paymentTypeAndCourse")
    suspend fun getPaymentTypeAndCourse(): paymentTypeAndCourse

    //Yashash joyi
    @GET("https://stat.edu.uz/api/student/statistic/paymentType/paymentTypeAndAccommodation")
    suspend fun getPaymentTypeAndAccommodation(): paymentTypeAndAccommodation


    //Fuqaroligi
    //Jinsi
    @GET("https://stat.edu.uz/api/student/statistic/citizenship/citizenshipAndGender")
    suspend fun getCitizenshipAndGender(): citizenshipAndGender

    //Yoshi
    @GET("https://stat.edu.uz/api/student/statistic/citizenship/citizenshipAndAge")
    suspend fun getCitizenshipAndAge(): citizenshipAndAge

    //Kurslar
    @GET("https://stat.edu.uz/api/student/statistic/citizenship/citizenshipAndCourse")
    suspend fun getCitizenshipAndCourse(): citizenshipAndCourse


    //KURSLAR
    //Jinsi
    @GET("https://stat.edu.uz/api/student/statistic/course/courseAndGender")
    suspend fun getCourseAndGender(): courseAndGender

    //Yoshi Bo'yicha
    @GET("https://stat.edu.uz/api/student/statistic/course/courseAndAge")
    suspend fun getCourseAndAge(): courseAndAge

    //Yashash joyi
    @GET("https://stat.edu.uz/api/student/statistic/course/courseAndAccomodation")
    suspend fun getcourseAndAccomodation(): courseAndAccomodation

    @GET("https://stat.edu.uz/api/student/statistic/age/ageAndGender")
    suspend fun getAgeAndGender(): ageAndGender

    @GET("https://stat.edu.uz/api/student/statistic/age/ageAndAccomodation")
    suspend fun getAgeAndAccomodation(): ageAndAccomodation

    @GET("https://stat.edu.uz/api/student/statistic/accommodation/accommodationAndGender")
    suspend fun getAccommodationAndGender(): accommodationAndGender

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndGender")
    suspend fun getRegionAndGender(): regionAndGender

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndEduType")
    suspend fun getRegionAndEduType(): regionAndEduType

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndEduForm")
    suspend fun getregionAndEduForm(): regionAndEduForm

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndPaymentType")
    suspend fun getRegionAndPaymentType(): regionAndGender

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndCourse")
    suspend fun getRegionAndCourse(): regionAndGender

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndCitizenship")
    suspend fun getRegionAndCitizenship(): regionAndGender

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndAge")
    suspend fun getRegionAndAge(): regionAndGender

    @GET("https://stat.edu.uz/api/student/statistic/region/regionAndAccommodation")
    suspend fun getRegionAndAccommodation(): regionAndGender


    //O'qituvchilar (Jins bo'yicha)
    @GET("https://stat.edu.uz/api/teacher/statistic/gender")
    suspend fun getTeacherStatisticGender(): techerStatisticGender


    //Rahbar xodimlar
    @GET("https://stat.edu.uz//api/teacher/statistic/chiefPosition")
    suspend fun getTeacherStatisticChiefPosition(): techerStatisticChiefPosition

    //O'qituvchilar ilmiy salohiyati bo'yicha
    // O'qituvchilar ilmiy daraja bo'yicha
    @GET("https://stat.edu.uz/api/teacher/statistic/academicDegree")
    suspend fun getTeacherStatisticAcademicDegree(): techerStatisticAcademicDegree


    // O'qituvchilar ilmiy unvon bo'yicha
    @GET("https://stat.edu.uz//api/teacher/statistic/academicRank")
    suspend fun getTeacherStatisticAcademicRank(): techerStatisticAcademicRank

    //Lavozimi bo'yicha
    @GET("https://stat.edu.uz/api/teacher/statistic/position")
    suspend fun getTeacherStatisticPosition(): techerStatisticPosition


    //Mehnat shakli bo'yicha
    @GET("https://stat.edu.uz/api/teacher/statistic/employeeForm")
    suspend fun getTeacherStatisticEmployeeForm(): techerStatisticEmployeeForm

    //O'qituvchilar fuqarolik bo'yicha
    @GET(" https://stat.edu.uz/api/teacher/statistic/citizenship")
    suspend fun getTeacherStatisticCitizenship(): techerStatisticCitizenship

    //O'qituvchilar yoshi bo'yicha
    @GET("https://stat.edu.uz//api/teacher/statistic/ageGreaterOrLessThan30")
    suspend fun getTeacherStatisticAge(): techerStatisticAge

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/university?limit=10000")
    suspend fun getUniversity(): University

    //Professional Ta'lim
    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/education-type")
    suspend fun getEducationTypes(): ProfEduType

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/education-form")
    suspend fun getEducationForms(): ProfEduForm

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/admission-type")
    suspend fun getAdmissionTypes(): ProfAdmissionType

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/citizenship")
    suspend fun getCitizenship(): ProfCitizenship

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/course")
    suspend fun getCourses(): ProfCourse

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/age")
    suspend fun getAges(): ProfAge

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/current-live-place")
    suspend fun getCurrentLivePlaces(): ProfCurrentLive

    @GET("https://prof-emis.edu.uz/api/v2/classifier/basic/region/")
    suspend fun getRegions(): ProfRegion

    @GET("https://prof-emis.edu.uz/api/v2/integration/stat/public/region-section")
    suspend fun getRegionSection(): ProfRegionSection

    @GET("https://api-phd.mininnovation.uz/api/statistics/count_apply_doc_by_direction")
    suspend fun getDoctorantura():Doctorantura

}
