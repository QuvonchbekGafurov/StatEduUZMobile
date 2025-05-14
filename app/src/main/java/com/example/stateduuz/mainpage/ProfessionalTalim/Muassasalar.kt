package com.example.stateduuz.mainpage.ProfessionalTalim

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.FilterScreenManager
import com.example.stateduuz.R
import com.example.stateduuz.mainpage.OliyTalim.OtmlarManager
import com.example.stateduuz.mainpage.OliyTalim.OtmlarRoyxatiCard
import com.example.stateduuz.ui.theme.ColorRed
import com.example.stateduuz.utils.formatNumber
import com.github.mikephil.charting.components.YAxis
import kotlin.math.log

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomHorizontalBarChart(
    allDataList: List<List<Int>> = listOf(listOf(1, 2), listOf(1, 2), listOf(1, 2), listOf(1, 2)),
    xAxisScaleData: List<String> = listOf("Erkaklar", "Ayollar"),
    yAxisScaleData: List<String> = listOf("Sirtqi", "Maxsus Sirtqi", "Kunduzgi", "Dual"),
    title: String = "Salom",
    barheight: Dp = 30.dp,
    type: Boolean = false
) {
    // Data preparation
    val rowChartItems = yAxisScaleData.mapIndexed { index, title ->
        RowChartItem(
            titleItem = title,
            countList = allDataList.getOrElse(index) { emptyList() }
        )
    }
    val rowChartData = RowChartModel(
        title = title,
        type = xAxisScaleData,
        rowChartItem = rowChartItems
    )

    val totalCount = rowChartData.rowChartItem.flatMap { it.countList }.sum()
    val globalMaxCount = rowChartData.rowChartItem.flatMap { it.countList }.maxOrNull() ?: 1
    val maxTotalCount = rowChartData.rowChartItem.maxOfOrNull { it.countList.sum() } ?: 1

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .heightIn(max = 17000.dp)
                    .verticalScroll(scrollState)
            ) {
                // Header
                Text(
                    text = rowChartData.title,
                    fontWeight = FontWeight.W900,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(10.dp))

                // Total count
                Row {
                    Text(
                        text = "Umumiy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = formatNumber(totalCount),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Bars
                var columnHeight by remember { mutableStateOf(0) }

                Box {
                    Column(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .onGloballyPositioned { coordinates ->
                                columnHeight = coordinates.size.height
                            }
                    ) {
                        rowChartData.rowChartItem.forEach { rowItem ->
                            BarRow(
                                title = rowItem.titleItem,
                                counts = rowItem.countList,
                                colors = rowItem.colorListmain,
                                textColors = rowItem.colorListText,
                                barHeight = barheight,
                                globalMaxCount = globalMaxCount,
                                maxTotalCount = maxTotalCount,
                                type = type
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        HorizontalScale()
                    }

                    // Dashed vertical lines
                    Row {
                        Spacer(modifier = Modifier.weight(0.2f))
                        DashedVerticalLines(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(with(LocalDensity.current) { columnHeight.toDp() }) // set height equal to Column
                                .align(Alignment.Top) // Align top if you want to match
                        )
                        Spacer(modifier = Modifier.weight(0.2f))

                    }
                }

            }
        }

        // Legend
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            rowChartData.type.forEachIndexed { index, typeLabel ->
                LegendItem(
                    color = rowChartData.rowChartItem[0].colorListmain.getOrElse(index) { Color.Gray },
                    label = typeLabel
                )
            }
        }
    }
}

@Composable
private fun BarRow(
    title: String,
    counts: List<Int>,
    colors: List<Color>,
    textColors: List<Color>,
    barHeight: Dp,
    globalMaxCount: Int,
    maxTotalCount: Int,
    type: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.2f),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.secondary,
            lineHeight = 12.sp
        )

        if (type) {
            StackedBarRow(
                counts = counts,
                colors = colors,
                textColors = textColors,
                barHeight = barHeight,
                maxCount = globalMaxCount,
                modifier = Modifier.weight(0.8f)
            )
        } else {
            ProportionalBarRow(
                counts = counts,
                colors = colors,
                textColors = textColors,
                barHeight = barHeight,
                maxTotalCount = maxTotalCount,
                modifier = Modifier.weight(0.8f)
            )
        }
    }
}

@Composable
private fun StackedBarRow(
    counts: List<Int>,
    colors: List<Color>,
    textColors: List<Color>,
    barHeight: Dp,
    maxCount: Int,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        counts.forEachIndexed { index, count ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(barHeight)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .weight(0.75f)
                        .fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(count / maxCount.toFloat())
                            .align(Alignment.CenterStart)
                            .fillMaxHeight()
                            .background(colors.getOrElse(index) { Color.Gray })
                    )
                }

                Box(
                    modifier = Modifier.weight(0.25f),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = count.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(6.dp))
                                .background(textColors.getOrElse(index) { Color.Gray }),
                            color = colors.getOrElse(index) { Color.Gray }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
private fun ProportionalBarRow(
    counts: List<Int>,
    colors: List<Color>,
    textColors: List<Color>,
    barHeight: Dp,
    maxTotalCount: Int,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(barHeight)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    counts.forEachIndexed { index, count ->
                        val widthFraction = (count / maxTotalCount.toFloat()).takeIf { it > 0f }
                            ?: return@forEachIndexed
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxHeight()
                                .weight(widthFraction)
                                .background(colors.getOrElse(index) { Color.Gray })
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    val remainingFraction = (maxTotalCount - counts.sum()) / maxTotalCount.toFloat()
                        .coerceAtLeast(0f)
                    Spacer(modifier = Modifier.fillMaxWidth(remainingFraction))
                }
            }

            Box(
                modifier = Modifier.weight(0.25f),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = counts.sum().toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(6.dp))
                            .background(textColors.lastOrNull() ?: Color.Gray),
                        color = colors.lastOrNull() ?: Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun HorizontalScale() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(0.7f)
        )
        val maxValue = 120
        val steps = 3
        val labels = List(steps + 1) { it * (maxValue / steps) }

        Row(
            modifier = Modifier.fillMaxWidth(0.6f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach { label ->
                Text(
                    text = label.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun DashedVerticalLines(modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(4) {
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(bottom = 30.dp)
            ) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, color = MaterialTheme.colorScheme.secondary)
    }
}

data class RowChartModel(
    val title: String = "O'qtuvchilar ilmiy daraj bo'yicha",
    val type: List<String> = listOf("Erkaklar", "Ayollar"),
    val rowChartItem: List<RowChartItem>
)

data class RowChartItem(
    val titleItem: String,
    val countList: List<Int>,
    val colorListmain: List<Color> = listOf(
        Color(0xFF4DA2F1),
        Color(0xFFFF6482),
        Color(0xFFFF7F00),
        Color(0xFF43B1A0),
        Color(0xFF7D7AFF),
        Color(0xFFFFD426)
    ),
    val colorListText: List<Color> = listOf(
        Color(0x7A4DA2F1),
        Color(0x7AFF6482),
        Color(0x7AFF7F00),
        Color(0x7A43B1A0),
        Color(0x7A7D7AFF),
        Color(0x7AFFD426)
    )
)


@Composable
fun Muassasalar(viewModel: MuassasalarViewModel = hiltViewModel(), textSearch: String = "") {
    val university by viewModel.muassasalar.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        // Fetch data from the repository
        viewModel.fetchEducationTypes()
    }
    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {

        FilterScreenManager.selectedTabScreen = "Muassasalar"
        val filteredList = when (MuassasalarManager.selected1) {
            "Davlat" -> university.filter { it.ownership_form == 11 }
            "Nodavlat" -> university.filter { it.ownership_form == 12 }
            "Xorjiy" -> university.filter { it.ownership_form == 13 }
            else -> university
        }
        Log.e("TAG", "OtmlarRoyxatiScreen: $textSearch")
        val newFilter = if (textSearch.isBlank()) {
            filteredList
        } else {
            filteredList.filter { it.name_uz.contains(textSearch, ignoreCase = true) }
        }
        items(newFilter) { UniversityItem ->
            Spacer(modifier = Modifier.height(5.dp))
            MuassasalarCard(
                title = UniversityItem.name_uz,
                type = UniversityItem.ownership_form,
            )
            Spacer(modifier = Modifier.height(5.dp))

        }
    }

}

object MuassasalarManager {
    var selected1 by mutableStateOf("Umumiy")
}

@Composable
fun MuassasalarCard(
    title: String,
    type: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(12.dp)
    ) {
        Row {
            Text(
                text = if (type == 12) "Nodavlat"
                else if (type == 11) "Davlat"
                else "Nodavlat",
                modifier = Modifier
                    .width(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF2778D5), RoundedCornerShape(10.dp))
                    .padding(vertical = 3.dp),
                fontSize = 12.sp,
                color = Color(0xFF2778D5),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                "Texnikum",
                modifier = Modifier
                    .width(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF6DA966), RoundedCornerShape(10.dp))
                    .padding(vertical = 3.dp),
                fontSize = 12.sp,
                color = Color(0xFF6DA966),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Image(
                painter = painterResource(id = R.drawable.otm),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "$title",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                lineHeight = 18.sp // Qatorlar orasidagi masofani kichraytirish
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun showMuassasalari() {
    Muassasalar()
}
