package com.example.stateduuz.mainpage.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.stateduuz.R
import com.example.stateduuz.mainpage.ProfessionalTalim.OquvchilarViewModel
import com.example.stateduuz.mainpage.Qabul.QabulMiniItem1
import com.example.stateduuz.mainpage.Qabul.loadStatistics
import com.example.stateduuz.mainpage.Qabul.normalizeApplications
import com.example.stateduuz.model.qabul.StatisticsQabul
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorOrange
import com.example.stateduuz.ui.theme.ColorRed
import com.example.stateduuz.ui.theme.ColorWhite
import com.example.stateduuz.ui.theme.StatEduUzTheme
import com.example.stateduuz.utils.formatNumber
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val educationTypes by viewModel.educationType.observeAsState()
    val position by viewModel.position.observeAsState()
    val otm by viewModel.otm.observeAsState()
    val ownership by viewModel.ownership.observeAsState()

    // State
    var statistics by remember { mutableStateOf<StatisticsQabul?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch data and JSON
    LaunchedEffect(Unit) {
        statistics = loadStatistics(context, "qabul")
        viewModel.apply {
            fetchEducationType()
            fetchOtm()
            fetchPosition()
            fetchOwnership()
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // Oliy ta'lim section
            item {
                SectionHeader(
                    title = "Oliy ta'lim",
                    onDetailsClick = { navController.navigate("oliy") }
                )
                HorizontalScrollRow {
                    HomeScreenOliyTalimItem(
                        backgroundImage = R.drawable.card_main1,
                        title = "Oliy ta'lim muassasalari soni",
                        icon = R.drawable.cardicon_1,
                        number = otm?.size ?: 0
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HomeScreenOliyTalimItem(
                        backgroundImage = R.drawable.card_main2,
                        title = "Professor o'qituvchilar soni",
                        icon = R.drawable.umumiy_prof,
                        number = position?.sumOf { it.count } ?: 0
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HomeScreenOliyTalimItem(
                        backgroundImage = R.drawable.card_main3,
                        title = "Talabalar soni",
                        icon = R.drawable.umumiy_3,
                        number = ownership?.sumOf { it.count } ?: 0
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Kasb ta'lim section
            item {
                SectionHeader(
                    title = "Kasb ta'lim",
                    onDetailsClick = { navController.navigate("kasb") }
                )
                HorizontalScrollRow {
                    val kasbHunar = educationTypes?.by_gender?.getOrNull(0)?.let { it.male + it.female } ?: 0
                    val kollejlar = educationTypes?.by_gender?.getOrNull(1)?.let { it.male + it.female } ?: 0
                    val texnikumlar = educationTypes?.by_gender?.getOrNull(2)?.let { it.male + it.female } ?: 0

                    HomeScreenProfessionalTalimItem(
                        backgroundImage = R.drawable.card_main4,
                        title = "Kasb-hunar maktablar",
                        icon = R.drawable.student_back,
                        number = kasbHunar,
                        iconColor = listOf(Color(0xFFD9D9D9), Color.White, Color(0xFFD9D9D9))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HomeScreenProfessionalTalimItem(
                        backgroundImage = R.drawable.card_main5,
                        title = "Kollejlar",
                        icon = R.drawable.stu_magist,
                        number = kollejlar,
                        iconColor = listOf(Color(0xFFD9D9D9), Color.White, Color(0xFFD9D9D9))
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Qabul section
            item {
                SectionHeader(
                    title = "Qabul",
                    onDetailsClick = { navController.navigate("qabul") }
                )
                statistics?.let { data ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(16.dp)
                    ) {
                        Column {
                            data.categories.getOrNull(2)?.data?.forEach { item ->
                                Spacer(modifier = Modifier.height(12.dp))
                                QabulMiniItem1(
                                    data = item,
                                    foiz = normalizeApplications(data.categories[2].data, item.applications)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SectionHeader(title: String, onDetailsClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Batafsil",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.clickable { onDetailsClick() }
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun HorizontalScrollRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        content()
    }
}
@Composable
fun HomeScreenOliyTalimItem(
    backgroundImage: Int,
    icon: Int,
    title: String,
    number: Int,
    iconColor: List<Color> = listOf(Color.Red, Color.Red, Color.White)
) {
    Box(
        modifier = Modifier
            .width(230.dp)
            .height(140.dp)
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.padding(15.dp)) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxSize()
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatNumber(number),
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp),
                        tint = iconColor[2]
                    )
                }
            }
            Text(
                text = "Batafsil",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { /* Add navigation or action */ },
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun HomeScreenProfessionalTalimItem(
    backgroundImage: Int,
    icon: Int,
    title: String,
    number: Int,
    iconColor: List<Color>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(230.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Text(
                text = formatNumber(number),
                modifier = Modifier.align(Alignment.CenterStart),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(50.dp)
                    .background(iconColor[0], shape = CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(iconColor[1])
                        .padding(6.dp),
                    tint = iconColor[2]
                )
            }
            Text(
                text = title,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .width(100.dp)
                    .padding(end = 10.dp, bottom = 10.dp),
                fontSize = 12.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
            Text(
                text = "Batafsil",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp)
                    .clickable { /* Add navigation or action */ },
                fontSize = 12.sp,
                color = Color.LightGray
            )
        }
    }
}