package com.example.stateduuz.mainpage.OliyTalim.umumiy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.stateduuz.chart.BarChartScreen
import com.example.stateduuz.chart.PieChartPreview
import com.example.stateduuz.chart.stackedbar.StackedBarChartScreen

@Composable
fun UmumiyScreen() {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .padding(20.dp)
        .verticalScroll(scrollState)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(10.dp)
        )
        {

            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            PieChartPreview()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(10.dp)
        )
        {
            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            StackedBarChartScreen()
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(10.dp)
        )
        {

            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            BarChartScreen()
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(10.dp)
        )
        {

            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            BarChartScreen()
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(10.dp)
        )
        {

            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            PieChartPreview()
        }
        Spacer(modifier = Modifier.height(20.dp))

       Column (modifier = Modifier
           .fillMaxWidth()
           .clip(RoundedCornerShape(14.dp))
           .background(Color.White)
           .padding(10.dp))
       {

           Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
           PieChartPreview()
       }
        Column (modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(10.dp))
        {

            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            PieChartPreview()
        }
        Column (modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(10.dp))
        {

            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            PieChartPreview()
        }
        Column (modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(10.dp))
        {

            Text(text = "OTMlar soni mulkchilik shakli bo'yicha", fontWeight = FontWeight.Bold)
            PieChartPreview()
        }

   }
}

