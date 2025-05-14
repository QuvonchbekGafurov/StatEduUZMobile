package com.example.stateduuz.mainpage.ProfessionalTalim

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stateduuz.data.repository.UniversitetRepository
import com.example.stateduuz.model.Professional.profAdmissionType.ProfAdmissionType
import com.example.stateduuz.model.Professional.profAge.ProfAge
import com.example.stateduuz.model.Professional.profEduForm.ProfEduForm
import com.example.stateduuz.model.Professional.profEduType.ProfEduType
import com.example.stateduuz.model.Professional.profcitizenship.ProfCitizenship
import com.example.stateduuz.model.Professional.profcourse.ProfCourse
import com.example.stateduuz.model.Professional.profcurrentLive.ProfCurrentLive
import com.example.stateduuz.model.Professional.region.ProfRegion
import com.example.stateduuz.model.universitety.University
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OquvchilarViewModel @Inject constructor(
    private val repository: UniversitetRepository
) : ViewModel() {

    // Core data
    private val _admissionTypes = MutableLiveData<ProfAdmissionType>()
    val admissionTypes: LiveData<ProfAdmissionType> get() = _admissionTypes

    private val _ages = MutableLiveData<ProfAge>()
    val ages: LiveData<ProfAge> get() = _ages

    private val _citizenship = MutableLiveData<ProfCitizenship>()
    val citizenship: LiveData<ProfCitizenship> get() = _citizenship

    private val _courses = MutableLiveData<ProfCourse>()
    val courses: LiveData<ProfCourse> get() = _courses

    private val _currentLivePlaces = MutableLiveData<ProfCurrentLive>()
    val currentLivePlaces: LiveData<ProfCurrentLive> get() = _currentLivePlaces

    private val _educationForms = MutableLiveData<ProfEduForm>()
    val educationForms: LiveData<ProfEduForm> get() = _educationForms

    private val _educationTypes = MutableLiveData<ProfEduType>()
    val educationTypes: LiveData<ProfEduType> get() = _educationTypes

    // UI data structure
    data class ChartData(
        val xAxisScale: List<String>,
        val yAxisScale: List<String>,
        val allDataList: List<List<Int>>,
        val topValues: List<Int>,
        val type: Boolean = true
    )

    // AdmissionType screen charts
    private val _admGenderChartData = MutableStateFlow<ChartData?>(null)
    val admGenderChartData: StateFlow<ChartData?> = _admGenderChartData.asStateFlow()

    private val _admAgeChartData = MutableStateFlow<ChartData?>(null)
    val admAgeChartData: StateFlow<ChartData?> = _admAgeChartData.asStateFlow()

    private val _admLivePlaceChartData = MutableStateFlow<ChartData?>(null)
    val admLivePlaceChartData: StateFlow<ChartData?> = _admLivePlaceChartData.asStateFlow()

    private val _admCourseChartData = MutableStateFlow<ChartData?>(null)
    val admCourseChartData: StateFlow<ChartData?> = _admCourseChartData.asStateFlow()

    private val _admCitizenshipChartData = MutableStateFlow<ChartData?>(null)
    val admCitizenshipChartData: StateFlow<ChartData?> = _admCitizenshipChartData.asStateFlow()

    // Age screen charts
    private val _ageGenderChartData = MutableStateFlow<ChartData?>(null)
    val ageGenderChartData: StateFlow<ChartData?> = _ageGenderChartData.asStateFlow()

    private val _ageLivePlaceChartData = MutableStateFlow<ChartData?>(null)
    val ageLivePlaceChartData: StateFlow<ChartData?> = _ageLivePlaceChartData.asStateFlow()

    // Citizenship screen charts
    private val _citGenderChartData = MutableStateFlow<ChartData?>(null)
    val citGenderChartData: StateFlow<ChartData?> = _citGenderChartData.asStateFlow()

    private val _citAgeChartData = MutableStateFlow<ChartData?>(null)
    val citAgeChartData: StateFlow<ChartData?> = _citAgeChartData.asStateFlow()

    private val _citLivePlaceChartData = MutableStateFlow<ChartData?>(null)
    val citLivePlaceChartData: StateFlow<ChartData?> = _citLivePlaceChartData.asStateFlow()

    private val _citCourseChartData = MutableStateFlow<ChartData?>(null)
    val citCourseChartData: StateFlow<ChartData?> = _citCourseChartData.asStateFlow()

    // Course screen charts
    private val _courseGenderChartData = MutableStateFlow<ChartData?>(null)
    val courseGenderChartData: StateFlow<ChartData?> = _courseGenderChartData.asStateFlow()

    private val _courseAgeChartData = MutableStateFlow<ChartData?>(null)
    val courseAgeChartData: StateFlow<ChartData?> = _courseAgeChartData.asStateFlow()

    private val _courseLivePlaceChartData = MutableStateFlow<ChartData?>(null)
    val courseLivePlaceChartData: StateFlow<ChartData?> = _courseLivePlaceChartData.asStateFlow()

    // CurrentLive screen charts
    private val _liveGenderChartData = MutableStateFlow<ChartData?>(null)
    val liveGenderChartData: StateFlow<ChartData?> = _liveGenderChartData.asStateFlow()

    // EduForm screen charts
    private val _eduFormGenderChartData = MutableStateFlow<ChartData?>(null)
    val eduFormGenderChartData: StateFlow<ChartData?> = _eduFormGenderChartData.asStateFlow()

    private val _eduFormAgeChartData = MutableStateFlow<ChartData?>(null)
    val eduFormAgeChartData: StateFlow<ChartData?> = _eduFormAgeChartData.asStateFlow()

    private val _eduFormLivePlaceChartData = MutableStateFlow<ChartData?>(null)
    val eduFormLivePlaceChartData: StateFlow<ChartData?> = _eduFormLivePlaceChartData.asStateFlow()

    private val _eduFormPTChartData = MutableStateFlow<ChartData?>(null)
    val eduFormPTChartData: StateFlow<ChartData?> = _eduFormPTChartData.asStateFlow()

    private val _eduFormCourseChartData = MutableStateFlow<ChartData?>(null)
    val eduFormCourseChartData: StateFlow<ChartData?> = _eduFormCourseChartData.asStateFlow()

    private val _eduFormCitizenshipChartData = MutableStateFlow<ChartData?>(null)
    val eduFormCitizenshipChartData: StateFlow<ChartData?> = _eduFormCitizenshipChartData.asStateFlow()

    // EduType screen charts
    private val _eduTypeAgeChartData = MutableStateFlow<ChartData?>(null)
    val eduTypeAgeChartData: StateFlow<ChartData?> = _eduTypeAgeChartData.asStateFlow()

    private val _eduTypePTChartData = MutableStateFlow<ChartData?>(null)
    val eduTypePTChartData: StateFlow<ChartData?> = _eduTypePTChartData.asStateFlow()

    private val _eduTypeCourseChartData = MutableStateFlow<ChartData?>(null)
    val eduTypeCourseChartData: StateFlow<ChartData?> = _eduTypeCourseChartData.asStateFlow()

    private val _eduTypeCitizenshipChartData = MutableStateFlow<ChartData?>(null)
    val eduTypeCitizenshipChartData: StateFlow<ChartData?> = _eduTypeCitizenshipChartData.asStateFlow()

    private val _eduTypeETChartData = MutableStateFlow<ChartData?>(null)
    val eduTypeETChartData: StateFlow<ChartData?> = _eduTypeETChartData.asStateFlow()

    private val _eduTypeLivePlaceChartData = MutableStateFlow<ChartData?>(null)
    val eduTypeLivePlaceChartData: StateFlow<ChartData?> = _eduTypeLivePlaceChartData.asStateFlow()

    // EduType gender cards
    data class GenderCardData(
        val college: List<Int>,
        val technicum: List<Int>
    )

    private val _eduTypeGenderCardData = MutableStateFlow<GenderCardData?>(null)
    val eduTypeGenderCardData: StateFlow<GenderCardData?> = _eduTypeGenderCardData.asStateFlow()

    // Constants
    private val yAxisScaleAdm = listOf("To'lov Shartnoma", "Davlat Granti")
    private val yAxisScaleAge = listOf("20 yoshdan oshganlar", "20 yoshdan kichiklar")
    private val yAxisScaleCit = listOf("Fuqaroligi yo'q shaxs", "O'zbekiston fuqarosi", "Voyaga yetmagan Shaxs", "Xorijiy davlat fuqarosi")
    private val yAxisScaleCourse = listOf("1-kurs", "2-kurs", "3-kurs")
    private val yAxisScaleLive = listOf("O‘z uyida", "Qarindoshining uyida", "Tanishining uyida", "Ijaradagi uyda", "Yotoqxonada", "Boshqa")
    private val yAxisScaleEduForm = listOf("Dual", "Kechki", "Kunduzgi", "Sirtqi", "Maxsus sirtqi", "Eksternat tartibidagi ta’lim")
    private val yAxisScaleEduType = listOf("Kunduzgi", "Sirtqi", "Kechki", "Dual")
    private val xAxisScaleGender = listOf("Erkak", "Ayol")
    private val xAxisScaleAge = listOf("20 yoshdan oshganlar", "20 yoshdan kichiklar")
    private val xAxisScaleLive = listOf("O'z uyida", "Talabalar turar joyida", "Ijaradagi uyida", "Qarindoshining uyida", "Tanishing uyida", "Boshqa")
    private val xAxisScaleEduType = listOf("Kasb-hunar maktablar", "Kollejlar", "Texnikumlar")
    private val listPt = listOf("Davlat granti", "To'lov-kontrakt")
    private val listCitizen = listOf("O'zbekiston fuqarosi", "Xorijiy davlat fuqarosi", "Fuqaroligi yo'q shaxs", "Voyaga yetmagan shaxs")

    init {
        admissionTypes.observeForever { it?.let { processAdmissionTypeData(it) } }
        ages.observeForever { it?.let { processAgeData(it) } }
        citizenship.observeForever { it?.let { processCitizenshipData(it) } }
        courses.observeForever { it?.let { processCourseData(it) } }
        currentLivePlaces.observeForever { it?.let { processCurrentLiveData(it) } }
        educationForms.observeForever { it?.let { processEduFormData(it) } }
        educationTypes.observeForever { it?.let { processEduTypeData(it) } }
    }

    // Data fetching
    fun fetchAdmissionTypes() = fetchData(repository::getAdmissionTypes, _admissionTypes)
    fun fetchAges() = fetchData(repository::getAges, _ages)
    fun fetchCitizenship() = fetchData(repository::getCitizenship, _citizenship)
    fun fetchCourses() = fetchData(repository::getCourses, _courses)
    fun fetchCurrentLivePlaces() = fetchData(repository::getCurrentLivePlaces, _currentLivePlaces)
    fun fetchEducationForms() = fetchData(repository::getEducationForms, _educationForms)
    fun fetchEducationTypes() = fetchData(repository::getEducationTypes, _educationTypes)

    private fun <T> fetchData(fetchFunction: suspend () -> T, liveData: MutableLiveData<T>) {
        viewModelScope.launch {
            try {
                val data = fetchFunction()
                liveData.postValue(data)
                Log.d("OquvchilarViewModel", "Fetched data: $data")
            } catch (e: Exception) {
                Log.e("OquvchilarViewModel", "Error fetching data: ${e.message}")
            }
        }
    }

    // Data processing
    private fun processAdmissionTypeData(data: ProfAdmissionType) {
        _admGenderChartData.value = ChartData(
            xAxisScale = xAxisScaleGender,
            yAxisScale = yAxisScaleAdm,
            allDataList = data.by_gender.map { listOf(it.male, it.female) },
            topValues = listOf(data.by_gender.sumOf { it.male }, data.by_gender.sumOf { it.female }),
            type = true
        )
        _admAgeChartData.value = ChartData(
            xAxisScale = xAxisScaleAge,
            yAxisScale = yAxisScaleAdm,
            allDataList = data.by_age.map { listOf(it.gt_20, it.lte_20) },
            topValues = listOf(data.by_age.sumOf { it.gt_20 }, data.by_age.sumOf { it.lte_20 }),
            type = true
        )
        _admLivePlaceChartData.value = ChartData(
            xAxisScale = xAxisScaleLive,
            yAxisScale = yAxisScaleAdm,
            allDataList = data.by_current_live_place.map { listOf(it.`9`, it.`10`, it.`11`, it.`12`, it.`13`, it.`14`) },
            topValues = listOf(
                data.by_current_live_place.sumOf { it.`9` },
                data.by_current_live_place.sumOf { it.`10` },
                data.by_current_live_place.sumOf { it.`11` },
                data.by_current_live_place.sumOf { it.`12` },
                data.by_current_live_place.sumOf { it.`13` },
                data.by_current_live_place.sumOf { it.`14` }
            ),
            type = false
        )
        _admCourseChartData.value = ChartData(
            xAxisScale = yAxisScaleAdm,
            yAxisScale = yAxisScaleCourse,
            allDataList = listOf(
                data.by_course.map { it.course1 },
                data.by_course.map { it.course2 },
                data.by_course.map { it.course3 }
            ),
            topValues = listOf(
                data.by_course.sumOf { it.course1 },
                data.by_course.sumOf { it.course2 },
                data.by_course.sumOf { it.course3 }
            )
        )
        _admCitizenshipChartData.value = ChartData(
            xAxisScale = yAxisScaleAdm,
            yAxisScale = listCitizen,
            allDataList = listOf(
                data.by_citizenship.map { it.`11` },
                data.by_citizenship.map { it.`12` },
                data.by_citizenship.map { it.`13` },
                data.by_citizenship.map { it.`14` }
            ),
            topValues = listOf(
                data.by_citizenship.sumOf { it.`11` },
                data.by_citizenship.sumOf { it.`12` },
                data.by_citizenship.sumOf { it.`13` },
                data.by_citizenship.sumOf { it.`14` }
            )
        )
    }

    private fun processAgeData(data: ProfAge) {
        _ageGenderChartData.value = ChartData(
            xAxisScale = xAxisScaleGender,
            yAxisScale = yAxisScaleAge,
            allDataList = listOf(
                data.by_gender.map { it.gt_20 },
                data.by_gender.map { it.lte_20 }
            ),
            topValues = listOf(
                data.by_gender.sumOf { it.gt_20 },
                data.by_gender.sumOf { it.lte_20 }
            ),
            type = true
        )
        _ageLivePlaceChartData.value = ChartData(
            xAxisScale = xAxisScaleLive,
            yAxisScale = yAxisScaleAge,
            allDataList = listOf(
                data.by_current_live_place.map { it.gt_20 },
                data.by_current_live_place.map { it.lte_20 }
            ),
            topValues = listOf(
                data.by_current_live_place.sumOf { it.gt_20 },
                data.by_current_live_place.sumOf { it.lte_20 }
            ),
            type = false
        )
    }

    private fun processCitizenshipData(data: ProfCitizenship) {
        _citGenderChartData.value = ChartData(
            xAxisScale = xAxisScaleGender,
            yAxisScale = yAxisScaleCit,
            allDataList = data.by_gender.map { listOf(it.male, it.female) },
            topValues = listOf(data.by_gender.sumOf { it.male }, data.by_gender.sumOf { it.female }),
            type = true
        )
        _citAgeChartData.value = ChartData(
            xAxisScale = xAxisScaleAge,
            yAxisScale = yAxisScaleCit,
            allDataList = data.by_age.map { listOf(it.gt_20, it.lte_20) },
            topValues = listOf(data.by_age.sumOf { it.gt_20 }, data.by_age.sumOf { it.lte_20 }),
            type = true
        )
        _citLivePlaceChartData.value = ChartData(
            xAxisScale = xAxisScaleLive,
            yAxisScale = yAxisScaleCit,
            allDataList = data.by_current_live_place.map { listOf(it.`9`, it.`10`, it.`11`, it.`12`, it.`13`, it.`14`) },
            topValues = listOf(
                data.by_current_live_place.sumOf { it.`9` },
                data.by_current_live_place.sumOf { it.`10` },
                data.by_current_live_place.sumOf { it.`11` },
                data.by_current_live_place.sumOf { it.`12` },
                data.by_current_live_place.sumOf { it.`13` },
                data.by_current_live_place.sumOf { it.`14` }
            ),
            type = false
        )
        _citCourseChartData.value = ChartData(
            xAxisScale = yAxisScaleCit,
            yAxisScale = yAxisScaleCourse,
            allDataList = listOf(
                data.by_course.map { it.course1 },
                data.by_course.map { it.course2 },
                data.by_course.map { it.course3 }
            ),
            topValues = listOf(
                data.by_course.sumOf { it.course1 },
                data.by_course.sumOf { it.course2 },
                data.by_course.sumOf { it.course3 }
            )
        )
    }

    private fun processCourseData(data: ProfCourse) {
        _courseGenderChartData.value = ChartData(
            xAxisScale = xAxisScaleGender,
            yAxisScale = yAxisScaleCourse,
            allDataList = data.by_gender.map { listOf(it.male, it.female) },
            topValues = listOf(data.by_gender.sumOf { it.male }, data.by_gender.sumOf { it.female }),
            type = true
        )
        _courseAgeChartData.value = ChartData(
            xAxisScale = xAxisScaleAge,
            yAxisScale = yAxisScaleCourse,
            allDataList = data.by_age.map { listOf(it.gt_20, it.lte_20) },
            topValues = listOf(data.by_age.sumOf { it.gt_20 }, data.by_age.sumOf { it.lte_20 }),
            type = true
        )
        _courseLivePlaceChartData.value = ChartData(
            xAxisScale = xAxisScaleLive,
            yAxisScale = yAxisScaleCourse,
            allDataList = data.by_current_live_place.map { listOf(it.`9`, it.`10`, it.`11`, it.`12`, it.`13`, it.`14`) },
            topValues = listOf(
                data.by_current_live_place.sumOf { it.`9` },
                data.by_current_live_place.sumOf { it.`10` },
                data.by_current_live_place.sumOf { it.`11` },
                data.by_current_live_place.sumOf { it.`12` },
                data.by_current_live_place.sumOf { it.`13` },
                data.by_current_live_place.sumOf { it.`14` }
            ),
            type = false
        )
    }

    private fun processCurrentLiveData(data: ProfCurrentLive) {
        _liveGenderChartData.value = ChartData(
            xAxisScale = xAxisScaleGender,
            yAxisScale = yAxisScaleLive,
            allDataList = data.by_gender.map { listOf(it.male, it.female) },
            topValues = listOf(data.by_gender.sumOf { it.male }, data.by_gender.sumOf { it.female }),
            type = false
        )
    }

    private fun processEduFormData(data: ProfEduForm) {
        _eduFormGenderChartData.value = ChartData(
            xAxisScale = xAxisScaleGender,
            yAxisScale = yAxisScaleEduForm,
            allDataList = data.by_gender.map { listOf(it.male, it.female) },
            topValues = listOf(data.by_gender.sumOf { it.male }, data.by_gender.sumOf { it.female }),
            type = true
        )
        _eduFormAgeChartData.value = ChartData(
            xAxisScale = xAxisScaleAge,
            yAxisScale = yAxisScaleEduForm,
            allDataList = data.by_age.map { listOf(it.gt_20, it.lte_20) },
            topValues = listOf(data.by_age.sumOf { it.gt_20 }, data.by_age.sumOf { it.lte_20 }),
            type = true
        )
        _eduFormLivePlaceChartData.value = ChartData(
            xAxisScale = xAxisScaleLive,
            yAxisScale = yAxisScaleEduForm,
            allDataList = data.by_current_live_place.map { listOf(it.`9`, it.`10`, it.`11`, it.`12`, it.`13`, it.`14`) },
            topValues = listOf(
                data.by_current_live_place.sumOf { it.`9` },
                data.by_current_live_place.sumOf { it.`10` },
                data.by_current_live_place.sumOf { it.`11` },
                data.by_current_live_place.sumOf { it.`12` },
                data.by_current_live_place.sumOf { it.`13` },
                data.by_current_live_place.sumOf { it.`14` }
            ),
            type = false
        )
        _eduFormPTChartData.value = ChartData(
            xAxisScale = yAxisScaleEduForm,
            yAxisScale = listPt,
            allDataList = listOf(
                data.by_admission_type.map { it.grand },
                data.by_admission_type.map { it.contract }
            ),
            topValues = listOf(
                data.by_admission_type.sumOf { it.grand },
                data.by_admission_type.sumOf { it.contract }
            ),
            type = false
        )
        _eduFormCourseChartData.value = ChartData(
            xAxisScale = yAxisScaleEduForm,
            yAxisScale = yAxisScaleCourse,
            allDataList = listOf(
                data.by_course.map { it.course1 },
                data.by_course.map { it.course2 },
                data.by_course.map { it.course3 }
            ),
            topValues = listOf(
                data.by_course.sumOf { it.course1 },
                data.by_course.sumOf { it.course2 },
                data.by_course.sumOf { it.course3 }
            )
        )
        _eduFormCitizenshipChartData.value = ChartData(
            xAxisScale = yAxisScaleEduForm,
            yAxisScale = listCitizen,
            allDataList = listOf(
                data.by_citizenship.map { it.`11` },
                data.by_citizenship.map { it.`12` },
                data.by_citizenship.map { it.`13` },
                data.by_citizenship.map { it.`14` }
            ),
            topValues = listOf(
                data.by_citizenship.sumOf { it.`11` },
                data.by_citizenship.sumOf { it.`12` },
                data.by_citizenship.sumOf { it.`13` },
                data.by_citizenship.sumOf { it.`14` }
            )
        )
    }

    private fun processEduTypeData(data: ProfEduType) {
        _eduTypeGenderCardData.value = GenderCardData(
            college = listOf(data.by_gender[0].male, data.by_gender[0].female),
            technicum = listOf(data.by_gender[1].male, data.by_gender[1].female)
        )
        _eduTypeAgeChartData.value = ChartData(
            xAxisScale = xAxisScaleEduType,
            yAxisScale = xAxisScaleAge,
            allDataList = listOf(
                data.by_age.map { it.gt_20 },
                data.by_age.map { it.lte_20 }
            ),
            topValues = listOf(data.by_age.sumOf { it.gt_20 }, data.by_age.sumOf { it.lte_20 })
        )
        _eduTypePTChartData.value = ChartData(
            xAxisScale = xAxisScaleEduType,
            yAxisScale = listPt,
            allDataList = listOf(
                data.by_admission_type.map { it.grand },
                data.by_admission_type.map { it.contract }
            ),
            topValues = listOf(
                data.by_admission_type.sumOf { it.grand },
                data.by_admission_type.sumOf { it.contract }
            )
        )
        _eduTypeCourseChartData.value = ChartData(
            xAxisScale = xAxisScaleEduType,
            yAxisScale = yAxisScaleCourse,
            allDataList = listOf(
                data.by_course.map { it.course1 },
                data.by_course.map { it.course2 },
                data.by_course.map { it.course3 }
            ),
            topValues = listOf(
                data.by_course.sumOf { it.course1 },
                data.by_course.sumOf { it.course2 },
                data.by_course.sumOf { it.course3 }
            )
        )
        _eduTypeCitizenshipChartData.value = ChartData(
            xAxisScale = xAxisScaleEduType,
            yAxisScale = listCitizen,
            allDataList = listOf(
                data.by_citizenship.map { it.`14` },
                data.by_citizenship.map { it.`12` },
                data.by_citizenship.map { it.`13` },
                data.by_citizenship.map { it.`11` }
            ),
            topValues = listOf(
                data.by_citizenship.sumOf { it.`11` },
                data.by_citizenship.sumOf { it.`12` },
                data.by_citizenship.sumOf { it.`13` },
                data.by_citizenship.sumOf { it.`14` }
            )
        )
        _eduTypeETChartData.value = ChartData(
            xAxisScale = xAxisScaleEduType,
            yAxisScale = yAxisScaleEduType,
            allDataList = listOf(
                data.by_education_form.map { it.day_time },
                data.by_education_form.map { it.external },
                data.by_education_form.map { it.evening },
                data.by_education_form.map { it.dual }
            ),
            topValues = listOf(
                data.by_education_form.sumOf { it.day_time },
                data.by_education_form.sumOf { it.external },
                data.by_education_form.sumOf { it.evening },
                data.by_education_form.sumOf { it.dual }
            )
        )
        _eduTypeLivePlaceChartData.value = ChartData(
            xAxisScale = xAxisScaleLive,
            yAxisScale = xAxisScaleEduType,
            allDataList = data.by_current_live_place.map { listOf(it.`10`, it.`14`, it.`13`, it.`11`, it.`12`, it.`9`) },
            topValues = listOf(
                data.by_current_live_place.sumOf { it.`10` },
                data.by_current_live_place.sumOf { it.`14` },
                data.by_current_live_place.sumOf { it.`13` },
                data.by_current_live_place.sumOf { it.`11` },
                data.by_current_live_place.sumOf { it.`12` },
                data.by_current_live_place.sumOf { it.`9` }
            ),
            type = false
        )
    }

    override fun onCleared() {
        super.onCleared()
    }
}