package com.example.stateduuz.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import android.graphics.Typeface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import kotlin.math.round

@Composable
fun DonutChartWithRoundedCorners(
    total: Int,
    segments: List<Pair<String, Float>>, // List ko'rinishida nomi va foizi
    colors: List<Color> = listOf(Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784), Color(0xFFE57373), Color(0xFF64B5F6), Color(0xFF81C784)) // Standart ranglar
) {
    val strokeWidth = 150f // Qalinligi
    val gapAngle = 40f // Har bir segment orasidagi bo'shliq

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val radius = size.minDimension / 2 - strokeWidth / 2
            var startAngle = -90f

            segments.forEachIndexed { index, (label, percentage) ->
                val sweepAngle = 360 * (percentage / 100) - gapAngle
                val middleAngle = startAngle + sweepAngle / 2 // Matnni joylashtirish uchun

                // Har bir segmentni chizamiz (Path yordamida RoundCorner yaratamiz)
                val path = Path().apply {
                    arcTo(
                        rect = androidx.compose.ui.geometry.Rect(
                            offset = Offset(strokeWidth / 2, strokeWidth / 2),
                            size = Size(size.width - strokeWidth, size.height - strokeWidth)
                        ),
                        startAngleDegrees = startAngle + gapAngle / 2,
                        sweepAngleDegrees = sweepAngle,
                        forceMoveTo = true
                    )
                }

                drawPath(
                    path = path,
                    color = colors.getOrElse(index) { Color.Gray },
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                    )
                )

                // Foizni segment ichida chiqarish
                val angleRad = Math.toRadians(middleAngle.toDouble()).toFloat()
                val textX = (size.width / 2 + cos(angleRad) * radius * 0.7).toFloat()
                val textY = (size.height / 2 + sin(angleRad) * radius * 0.7).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${percentage}%",
                        textX,
                        textY,
                        Paint().apply {
                            textSize = 36f
                            color = android.graphics.Color.BLACK
                            textAlign = Paint.Align.CENTER
                            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        }
                    )
                }

                startAngle += sweepAngle + gapAngle // Keyingi segmentga o'tamiz
            }
        }

        // O'rtadagi asosiy raqam
        Text(
            text = total.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewDonutChart() {
    DonutChartWithRoundedCorners(
        total = 44778,
        segments = listOf(
            "Erkaklar" to 20f,
            "Ayollar" to 20f,
            "Salomlar" to 20f,
            "Erkaklar" to 20f,
            "Ayollar" to 20f,

        )
    )
}
