package com.example.stateduuz.chart

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stateduuz.utils.formatNumber
import com.example.stateduuz.ui.theme.Purple40
import com.example.stateduuz.utils.AnimatedSelection
import kotlin.math.PI
import kotlin.math.atan2


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PieChart(
    modifier: Modifier,
    radius: Dp = 100.dp,  // radiusni dpda berish
    innerRadius: Dp = 50.dp,  // innerRadiusni dpda berish
    transparentWidth: Dp = 0.dp,  // transparentWidthni dpda berish
    input: List<PieChartInput>,
    centerText: String = "",
    textsize: Int = 13
) {
    val rectColor = MaterialTheme.colorScheme.onSecondary
    val textColor = MaterialTheme.colorScheme.secondary
    val centerColor = MaterialTheme.colorScheme.onSecondary
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var inputList by remember {
        mutableStateOf(input)
    }
    var isCenterTapped by remember {
        mutableStateOf(false)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Log.e("TAG", "PieCharttt: $input")
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .pointerInput(true) {
                        detectTapGestures(
                            onTap = { offset ->
                                val tapAngleInDegrees = (-atan2(
                                    x = circleCenter.y - offset.y,
                                    y = circleCenter.x - offset.x
                                ) * (180f / PI).toFloat() - 90f).mod(360f)

                                val centerClicked = if (tapAngleInDegrees < 90) {
                                    offset.x < circleCenter.x + innerRadius.toPx() && offset.y < circleCenter.y + innerRadius.toPx()
                                } else if (tapAngleInDegrees < 180) {
                                    offset.x > circleCenter.x - innerRadius.toPx() && offset.y < circleCenter.y + innerRadius.toPx()
                                } else if (tapAngleInDegrees < 270) {
                                    offset.x > circleCenter.x - innerRadius.toPx() && offset.y > circleCenter.y - innerRadius.toPx()
                                } else {
                                    offset.x < circleCenter.x + innerRadius.toPx() && offset.y > circleCenter.y - innerRadius.toPx()
                                }

                                if (centerClicked) {
                                    inputList = inputList.map {
                                        it.copy(isTapped = !isCenterTapped)
                                    }
                                    isCenterTapped = !isCenterTapped
                                } else {
                                    val anglePerValue = 360f / input.sumOf {
                                        it.value
                                    }
                                    var currAngle = 0f
                                    inputList.forEach { pieChartInput ->

                                        currAngle += pieChartInput.value * anglePerValue
                                        if (tapAngleInDegrees < currAngle) {
                                            val description = pieChartInput.description
                                            inputList = inputList.map {
                                                if (description == it.description) {
                                                    it.copy(isTapped = !it.isTapped)
                                                } else {
                                                    it.copy(isTapped = false)
                                                }
                                            }
                                            return@detectTapGestures
                                        }
                                    }
                                }
                            }
                        )
                    }
            ) {
                val width = size.width
                val height = size.height
                circleCenter = Offset(x = width / 2f, y = height / 2f)

                val totalValue = input.sumOf {
                    it.value
                }
                val anglePerValue = 360f / totalValue
                var currentStartAngle = 0f

                inputList.forEach { pieChartInput ->
                    val scale = if (pieChartInput.isTapped) 1.1f else 1.0f
                    val angleToDraw = pieChartInput.value * anglePerValue

                    scale(scale) {
                        drawArc(
                            color = pieChartInput.color,
                            startAngle = currentStartAngle,
                            sweepAngle = angleToDraw,
                            useCenter = true,
                            size = Size(
                                width = radius.toPx() * 2f,
                                height = radius.toPx() * 2f
                            ),
                            topLeft = Offset(
                                (width - radius.toPx() * 2f) / 2f,
                                (height - radius.toPx() * 2f) / 2f
                            ),

                            )
                        currentStartAngle += angleToDraw
                    }
                    var rotateAngle = currentStartAngle - angleToDraw / 2f - 90f
                    var factor = 1f
                    if (rotateAngle > 90f) {
                        rotateAngle = (rotateAngle + 180).mod(360f)
                        factor = -0.92f
                    }

                    val percentage = (pieChartInput.value / totalValue.toFloat() * 100).toInt()

                    drawContext.canvas.nativeCanvas.apply {
                        if (percentage > 3) {
                            rotate(rotateAngle) {
                                drawText(
                                    "$percentage %",
                                    circleCenter.x,
                                    circleCenter.y + (radius.toPx() - (radius.toPx() - innerRadius.toPx()) / 2f) * factor,
                                    Paint().apply {
                                        textSize = textsize.sp.toPx()
                                        textAlign = Paint.Align.CENTER
                                        color =Color.White.toArgb()
                                        isFakeBoldText = true // <-- Matnni qalin qilish
                                    },


                                    )
                            }
                        }
                    }
                    if (pieChartInput.isTapped) {
                        val tabRotation = currentStartAngle - angleToDraw - 90f

                        // Boshlanish chizig'i
                        rotate(tabRotation) {
                            drawRoundRect(
                                topLeft = circleCenter,
                                size = Size(12f, radius.toPx() * 1.2f),
                                color = rectColor, // oq chiziq
                                cornerRadius = CornerRadius(15f, 15f)
                            )
                        }

                        // Tugash chizig'i (avval Color.Transparent edi, endi uni ham oq qilamiz)
                        rotate(tabRotation + angleToDraw) {
                            drawRoundRect(
                                topLeft = circleCenter,
                                size = Size(12f, radius.toPx() * 1.2f),
                                color = rectColor, // oq chiziq
                                cornerRadius = CornerRadius(15f, 15f)
                            )
                        }

                    }
                }

                if (inputList.first().isTapped) {
                    rotate(-90f) {
                        drawRoundRect(
                            topLeft = circleCenter,
                            size = Size(12f, radius.toPx() * 1.2f),
                            color = rectColor,
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                    }
                }
                drawContext.canvas.nativeCanvas.apply {
                    drawCircle(
                        circleCenter.x,
                        circleCenter.y,
                        innerRadius.toPx(),
                        Paint().apply {
                            color = Color.White.toArgb()
                            setShadowLayer(0f, 0f, 0f, Color.Transparent.toArgb())
                        }
                    )
                }

                drawCircle(
                    color = centerColor,
                    radius = innerRadius.toPx() + transparentWidth.toPx() / 2f
                )
            }

            Text(
                formatNumber(centerText.toInt()),
                modifier = Modifier
                    .width(innerRadius)
                    .padding(),
                fontWeight = FontWeight.Bold,
                fontSize = textsize.sp,  // Dynamically changing font size
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
        // **Legend (Sharh) qo'shildi**
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            inputList.forEach { pieChartInput ->
                val percentage =
                    (pieChartInput.value / input.sumOf { it.value }.toFloat() * 100).toInt()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(textsize.dp)
                            .background(pieChartInput.color, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${(pieChartInput.description)}: ${formatNumber(pieChartInput.value)}",
                        fontSize = textsize.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}


@Composable
fun PieChartCard(
    title: String,
    modifier: Modifier,
    radius: Dp = 100.dp,  // radiusni dpda berish
    innerRadius: Dp = 50.dp,  // innerRadiusni dpda berish
    transparentWidth: Dp = 0.dp,  // transparentWidthni dpda berish
    input: List<PieChartInput>,
    centerText: String = "",
    textsize: Int = 13
) {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(MaterialTheme.colorScheme.onSecondary)
        .padding(10.dp)
    ) {
        Text(
            text = "$title",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        PieChart(
            modifier = modifier,
            input = input,
            radius = radius,
            innerRadius = innerRadius,
            transparentWidth = transparentWidth,
            centerText = centerText,
            textsize = textsize
        )
    }
}

data class PieChartInput(
    val color: Color,
    val value: Int,
    val description: String,
    val isTapped: Boolean = false
)
