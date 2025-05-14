package com.example.stateduuz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorGreenMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ColorGreenMain, RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 8.dp) // ✅ Faqat tashqi padding
    ) {
        Row(
            modifier
                .fillMaxSize()
                .align(Alignment.CenterStart),
            Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                ,
                value = state.value,
                onValueChange = { state.value = it },
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (state.value.text.isEmpty()) {
                        Text(text = placeHolder, fontSize = 14.sp, color = Color.Gray)
                    }
                    innerTextField()
                }
            )
            Box (modifier.fillMaxWidth(0.2f)){
                Icon(
                    modifier = Modifier.size(25.dp).align(Alignment.CenterEnd),
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = ColorGreen
                )
            }
        }
    }
}
object SearchManager {
    var searchText by mutableStateOf(TextFieldValue(""))
}