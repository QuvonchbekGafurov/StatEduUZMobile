package com.example.stateduuz.utils

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.stateduuz.R
import com.example.stateduuz.chart.PieChart
import com.example.stateduuz.mainpage.OliyTalim.umumiy.PieChartData
import com.example.stateduuz.ui.theme.BackgroundBlur
import com.example.stateduuz.ui.theme.ColorAzureishWhite
import com.example.stateduuz.ui.theme.ColorBgSecondary
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorLightRed
import com.example.stateduuz.ui.theme.QabulColor
import com.example.stateduuz.ui.theme.QabulColor1
import com.example.stateduuz.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun AnimatedCounter(
    targetNumber: Int,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: Int = 20
) {
    val animatedNumber by animateIntAsState(
        targetValue = targetNumber,
        animationSpec = tween(
            durationMillis = 1000, // animatsiya davomiyligi
            easing = FastOutSlowInEasing // silliq boshlanib tugaydi
        )
    )

    Text(
        text = formatNumber(animatedNumber),
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary
    )
}


// Raqamni formatlash
fun formatNumber(number: Int): String {
    return "%,d".format(number).replace(",", " ")
}

fun calculateMaleCount(total: Int): Int {
    return (total * 0.5).toInt() // Masalan, 50% erkaklar
}

fun calculateFemaleCount(total: Int): Int {
    return (total * 0.5).toInt() // Masalan, 50% ayollar
}

fun calculatePercentage(value: Int, total: Int): String {
    return if (total > 0) {
        String.format("%.2f%%", (value.toDouble() / total) * 100)
    } else {
        "0.00%"
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedSelection(
    list: List<String> =listOf("Jami", "Davlat", "Nodavlat", "Xorijiy"),
    onValueChanged: (String) -> Unit // Tanlangan elementni uzatish uchun callback
) {
    var selectedYear by remember { mutableStateOf(0) }
    var isNext by remember { mutableStateOf(true) }

    LaunchedEffect(selectedYear) {
        onValueChanged(list[selectedYear])
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chap tugma
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(QabulColor1)
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (selectedYear > 0) {
                        isNext = false
                        selectedYear -= 1
                    }
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.polygon5),
                tint = QabulColor,
                modifier = Modifier.size(15.dp),
                contentDescription = ""
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Gradient chiziq
        Box(
            modifier = Modifier
                .height(32.dp)
                .width(3.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.LightGray, Color.Transparent),
                        start = Offset(0f, 0f),
                        end = Offset(8f, 0f)
                    )
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Animatsiyalangan matn
        AnimatedContent(
            targetState = selectedYear,
            transitionSpec = {
                if (isNext) {
                    slideInHorizontally { it } with slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } with slideOutHorizontally { it }
                }
            },
            label = "",
            modifier = Modifier.weight(1f)
        ) { year ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = list[year],
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Ikkinchi gradient chiziq
        Box(
            modifier = Modifier
                .height(32.dp)
                .width(3.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.LightGray, Color.Transparent),
                        start = Offset(8f, 0f),
                        end = Offset(0f, 0f)
                    )
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // O‘ng tugma
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(QabulColor1)
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (selectedYear < list.size - 1) {
                        isNext = true
                        selectedYear += 1
                    }
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.polygon6),
                tint = QabulColor,
                modifier = Modifier.size(15.dp),
                contentDescription = ""
            )
        }
    }
}


@Composable
fun UniversityTabs(
    onTabSelected: (String) -> Unit,
    tabs: List<String> = listOf("Jami", "Davlat", "Nodavlat", "Xorijiy")
) {
    var selectedTab by remember { mutableStateOf(tabs[0]) }

    LazyRow(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ColorBgSecondary),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(tabs) { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) ColorAzureishWhite else ColorBgSecondary)
                    .clickable {
                        selectedTab = tab
                        onTabSelected(tab)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Eni shunchaki text bo‘yicha
            ) {
                Text(
                    text = tab,
                    color = Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun CardUtils(
    modifier: Modifier,
    maintext: String = "",
    lists: List<Int>, listsname: List<String>,
    iconSize: Dp = 50.dp,
    icon: Int = R.drawable.stu_magist
) {
    val otmCount = lists.sum()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(15.dp),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            )
            {

                Text(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    text = "$maintext",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 13.sp,
                    color =MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(iconSize)
                        .background(
                            MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        ) // Doira shaklida fond
                        .clip(CircleShape) // Box ni kesish
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(7.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(6.dp)
                        ,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "$otmCount",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(), // Ekranni to‘liq egallaydi
                horizontalArrangement = Arrangement.SpaceEvenly, // Elementlar orasidagi masofani teng qiladi
            ) {
                items(lists.size) { index ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f) // Har bir element teng joy egallaydi
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "${lists[index]}",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color =MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = listsname[index],
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 10.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                }
            }
        }
    }
}

