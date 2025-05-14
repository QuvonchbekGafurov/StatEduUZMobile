package com.example.stateduuz.chart.stackedbar

import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Top
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.stateduuz.chart.BarGraph
import com.example.stateduuz.chart.BarType
import com.example.stateduuz.mainpage.OliyTalim.talabalar.screens.LegendItem
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.utils.AnimatedCounter
import com.example.stateduuz.utils.formatNumber
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import kotlin.math.round

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StackedBarGraph(
    xAxisScaleData: List<String>,
    yAxisScaleData: List<String>,
    allDataLists:List<List<Int>>,
    height: Dp,
    barWidth: Dp,
    type: Boolean = false
) {
    val topValue = List(allDataLists[0].size) { index ->
        allDataLists.sumOf { it[index] }
    }
    val list:List<Color> = listOf(Color(0xFF4DA2F1),Color(0xFFFF6482),Color(0xFF43B1A0),Color(0xFFFF7F00),Color(0xFFFFD426),Color(0xFF7D7AFF),Color(0xFF4DA2F1),Color(0xFFFF6482),Color(0xFF43B1A0),Color(0xFF43B1A0),Color(0xFFFF7F00),Color(0xFFFFD426))
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val totalData = allDataLists.reduce { acc, list -> acc.zip(list) { a, b -> a + b } }
    val maxValue = if (!type) {
        allDataLists.flatten().maxOrNull() ?: 1
    } else {
        allDataLists.reduce { acc, list -> acc.zip(list) { a, b -> a + b } }.maxOrNull() ?: 1
    }
    val scaleFactor =(height.value-70f) / maxValue.toFloat()
    val graphBarData = allDataLists.map { list -> list.map { if(type){ if (3f > it.toFloat() * scaleFactor) 3f else it.toFloat() * scaleFactor } else  if (10f > it.toFloat() * scaleFactor) 10f else it.toFloat() * scaleFactor  } }
    val context= LocalContext.current


    val density = LocalDensity.current
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val textPaint = remember(secondaryColor, density) {
        Paint().apply {
            color = secondaryColor.toArgb()
            textSize = 30f
            textAlign = android.graphics.Paint.Align.LEFT
        }
    }
    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(yAxisScaleData.size) { index ->
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "${yAxisScaleData[index]}",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    if (topValue.isNotEmpty()) {
                        AnimatedCounter(
                            targetNumber = allDataLists[index].sum() ?: 0,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
                .height(height - 20.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .height(height - 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val yAxisScaleText = maxValue / 3f
                val yCoordinates = mutableListOf<Float>()

                val heightInPx = with(density) { (height - 60.dp).toPx() }

                (0..3).forEach { i ->
                    yCoordinates.add(heightInPx - (heightInPx / 3f * i))
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    (0..3).forEach {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                formatNumber(round(yAxisScaleText * it).toInt()),
                                0f,
                                yCoordinates[it],
                                textPaint
                            )
                        }
                        drawLine(
                            start = Offset(0f, yCoordinates[it]),
                            end = Offset(size.width, yCoordinates[it]),
                            color = Color.Gray,
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                graphBarData[0].indices.forEach { index ->
                    Column {

                        val allYtext = allDataLists.map { it[index] }
                        val xAxisScaleDataItem = xAxisScaleData[index]// hammasida bir xil

                        val toastText: String = yAxisScaleData.mapIndexed { index, value ->
                            "$value: ${allYtext[index]}"
                        }.joinToString("\n")

                        var selectedText by remember { mutableStateOf<String?>(null) }
                        var selectedIndex by remember { mutableStateOf<Int?>(0) }

                        if (type == true) {
                            Column(
                                modifier = Modifier
                                    .height(height - 60.dp)
                                    .width(50.dp),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                graphBarData.forEachIndexed { layerIndex, dataset ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(dataset[index].dp)
                                            .padding(1.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(list.getOrElse(layerIndex) { Color.Gray })
                                            .clickable {
                                                selectedText =
                                                    if (selectedText == "${yAxisScaleData[layerIndex]} : ${allYtext[layerIndex]}")
                                                        null
                                                    else
                                                        "${yAxisScaleData[layerIndex]} : ${allYtext[layerIndex]}"
                                            }
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.height(height - 60.dp),
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.Center

                            ) {

                                graphBarData.forEachIndexed { layerIndex, dataset ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .width(20.dp)
                                            .height(dataset[index].dp)
                                            .padding(2.dp)
                                            .background(list.getOrElse(layerIndex) { Color.Gray })
                                            .clickable {
                                                selectedIndex = index
                                                Log.e(
                                                    "TAG",
                                                    "StackedBarGraph: $layerIndex  $index",
                                                )
                                                selectedText =
                                                    if (selectedText == "${yAxisScaleData[layerIndex]} : ${allYtext[layerIndex]}")
                                                        null
                                                    else
                                                        "${yAxisScaleData[layerIndex]} : ${allYtext[layerIndex]}"
                                            }
                                    )
                                }

                            }
                        }

                        selectedText?.let { text ->
                            showToastLikePopup(text, color = list[selectedIndex!!]) {
                                selectedText = null
                            }
                        }

                        Text(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                ) {

                                    selectedText = if (selectedText == xAxisScaleDataItem) {
                                        null // If the selected text is clicked again, set it to null (deselect)
                                    } else {
                                        toastText
                                    }

                                }
                                .width(100.dp),
                            text = xAxisScaleData.getOrElse(index) { "" },
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Start,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        FlowRow(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            yAxisScaleData.forEachIndexed { index, label ->
                LegendItem(
                    color = list[index],
                    label = label
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}


@Composable
fun showToastLikePopup(text: String,color: Color =Color.Yellow, onDismiss: () -> Unit,) {
    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = false)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                .border(
                    1.dp,
                    color = color,
                    RoundedCornerShape(8.dp)
                ) // ✅ 1dp sariq border qo'shildi
                .padding(8.dp)
        ) {
            Text(
                text = text,
                color = Color.Black
            )
            Log.e("TAG", "showToastLikePopup: $text", )
        }
    }
}
