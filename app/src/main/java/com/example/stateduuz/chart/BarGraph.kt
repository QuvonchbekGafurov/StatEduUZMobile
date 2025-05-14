package com.example.stateduuz.chart

import android.graphics.Paint
import android.icu.text.CaseMap.Title
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stateduuz.chart.stackedbar.showToastLikePopup
import com.example.stateduuz.utils.formatNumber
import kotlin.math.round

@Composable
fun BarGraph(
    graphBarData: List<Float>,
    xAxisScaleData: List<String>, // Stringni qo'yish uchun List'ni o'zgartirdim
    barData_: List<Int>,
    height: Dp,
    roundType: BarType,
    barWidth: Dp,
    barColor: Color,
    barArrangement: Arrangement.Horizontal
) {

    val barData by remember {
        mutableStateOf(barData_) // Barlar sonini to'liq olish uchun 0 qo'shdim
    }
    var selectedValue by remember { mutableStateOf<Int?>(null) } // ✅ Tanlangan qiymatni saqlash

    // for getting screen width and height you can use LocalConfiguration
    val configuration = LocalConfiguration.current
    // getting screen width
    val width = configuration.screenWidthDp.dp

    // bottom height of the X-Axis Scale
    val xAxisScaleHeight = 40.dp

    val yAxisScaleSpacing by remember {
        mutableStateOf(100f)
    }
    val yAxisTextWidth by remember {
        mutableStateOf(100.dp)
    }
    val context = LocalContext.current

    // bar shape
    val barShape =
        when (roundType) {
            BarType.CIRCULAR_TYPE -> CircleShape
            BarType.TOP_CURVED -> RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
        }

    val density = LocalDensity.current
    // y-axis scale text paint
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val textPaint = remember(density,secondaryColor) {
        Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
            color = secondaryColor.toArgb() // ✅ Rangni o‘rnatish
        }
    }


    // for y coordinates of y-axis scale to create horizontal dotted line indicating y-axis scale
    val yCoordinates = mutableListOf<Float>()
    // for dotted line effect
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    // height of vertical line over x-axis scale connecting x-axis horizontal line
    val lineHeightXAxis = 10.dp
    // height of horizontal line over x-axis scale
    val horizontalLineHeight = 2.dp
    var selectedIndex by remember { mutableStateOf<Int?>(null) } // ✅ Faqat tanlangan qiymatni saqlaymiz

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp),
        contentAlignment = Alignment.TopStart
    ) {

        // y-axis scale and horizontal dotted lines on graph indicating y-axis scale
        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            Canvas(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxSize()
            ) {

                // Y-Axis Scale Text
                // Y-Axis Scale Text
                val yAxisScaleText = barData.max() / 6f // 6 ta bo‘lishi uchun 6 ga bo‘lamiz
                (0..6).forEach { i -> // ✅ Endi 6 ta qiymat chiqadi (0 dan boshlab)
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            formatNumber(round(yAxisScaleText * i).toInt()), // ✅ Raqamlarni format qilish
                            30f, // X koordinatasi (chap tomonda chiqishi uchun)
                            size.height - yAxisScaleSpacing - i * size.height / 6f, // Y koordinatasi (6 ta bo‘lishi uchun)
                            textPaint // Matn bo‘yoq parametrlari
                        )
                    }
                    yCoordinates.add(size.height - yAxisScaleSpacing - i * size.height / 6f)
                }


                // horizontal dotted lines on graph indicating y-axis scale
                (1..5).forEach {
                    drawLine(
                        start = Offset(x = yAxisScaleSpacing + 50f, y = yCoordinates[it]),
                        end = Offset(x = size.width, y = yCoordinates[it]),
                        color = Color.Gray,
                        strokeWidth = 1f,
                        pathEffect = pathEffect
                    )
                }

            }

        }
        var selectedText by remember { mutableStateOf<String?>(null) }

        // Graph with Bar Graph and X-Axis Scale
        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ) {


            Row(
                modifier = Modifier
                    .width(width - yAxisTextWidth)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {

                // Graph
                graphBarData.forEachIndexed { index, value ->

                    var animationTriggered by remember {
                        mutableStateOf(false)
                    }
                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) value else 0f,
                        animationSpec = tween(
                            durationMillis = 2000,
                            delayMillis = 0
                        )
                    )
                    LaunchedEffect(key1 = true) {
                        animationTriggered = true
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Top,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        // Each Graph
                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clip(barShape)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent)
                                .clickable {
                                    selectedIndex = index
                                    selectedText =
                                        if (selectedText == "${xAxisScaleData[index]}: ${barData[index]}")
                                            null
                                        else
                                            "${xAxisScaleData[index]}: ${barData[index]}"
                                    Log.e("TAG", "BarGraph: ${barData[index]}")
                                },
                            contentAlignment = BottomCenter
                        ) {
                            if (index == selectedIndex) {
                                selectedText?.let { text ->
                                    showToastLikePopup(text, color = barColor) {
                                        selectedText = null
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(barShape)
                                    .fillMaxWidth()
                                    .fillMaxHeight(graphBarHeight)
                                    .background(barColor)
                            )
                        }


//
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .height(xAxisScaleHeight + 10.dp),
                            verticalArrangement = Top,
                            horizontalAlignment = CenterHorizontally
                        ) {

                            // small vertical line joining the horizontal x-axis line
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 2.dp,
                                            bottomEnd = 2.dp,
                                        )
                                    )
                                    .padding(top = 2.dp)
                                    .width(horizontalLineHeight)
                                    .height(5.dp)
                                    .background(color = Color.DarkGray)
                            )
                            // scale x-axis (Now we are using String data for x axis)
                            Text(
                                modifier = Modifier
                                    .width(70.dp), // Text uzunligini 100 dp bilan cheklash
                                text = xAxisScaleData.getOrElse(index) { "" }, // Indeks xatoligidan saqlanish
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.secondary,
                                lineHeight = 15.sp,
                                maxLines = 2, // 2 qator bilan cheklash
                            )


                        }

                    }

                }


            }

            // horizontal line on x-axis below the graph
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .padding(bottom = xAxisScaleHeight + 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .fillMaxWidth()
                        .height(horizontalLineHeight)
                        .background(Color.DarkGray)
                )
            }
        }
    }
}

@Composable
fun BarGraphCard(
    title: String = "",
    graphBarData: List<Float>,
    xAxisScaleData: List<String>, // Stringni qo'yish uchun List'ni o'zgartirdim
    barData_: List<Int>,
    height: Dp,
    roundType: BarType,
    barWidth: Dp,
    barColor: Color,
    barArrangement: Arrangement.Horizontal
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(vertical = 10.dp)
    )
    {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = title,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
        BarGraph(
            graphBarData = graphBarData,
            xAxisScaleData = xAxisScaleData,
            barData_ = barData_,
            height = height,
            roundType = roundType,
            barWidth = barWidth,
            barColor = barColor,
            barArrangement = barArrangement
        )
    }
}
