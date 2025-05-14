package com.example.stateduuz.mainpage.OliyTalim

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stateduuz.FilterScreenManager
import com.example.stateduuz.R
@Composable
fun OtmlarRoyxatiScreen(viewModel: OtmViewModel = hiltViewModel(),textSearch:String="") {
    val university by viewModel.universitet.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchUniversity()
    }


    FilterScreenManager.selectedTabScreen = "Otmlar"
    val filteredList = when (OtmlarManager.selected1) {
        "Davlat" -> university.filter { it.ownership_form == 11 }
        "Nodavlat" -> university.filter { it.ownership_form == 12 }
        "Xorjiy" -> university.filter { it.ownership_form == 13 }
        else -> university
    }
    Log.e("TAG", "OtmlarRoyxatiScreen: $textSearch", )
    val newFilter = if (textSearch.isBlank()) {
        filteredList
    } else {
        filteredList.filter { it.name_uz.contains(textSearch, ignoreCase = true) }
    }

    Log.e("TAG", "OtmlarRoyxatiScreen: $newFilter", )
    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
        items(newFilter) {UniversityItem->
            Spacer(modifier = Modifier.height(10.dp))
            OtmlarRoyxatiCard(
                universitetname =UniversityItem.name_uz,
                locationname =UniversityItem.address,
                location =UniversityItem.location,
                instagram =UniversityItem.instagram,
                facebook =UniversityItem.facebook,
                telegram =UniversityItem.telegram,
                youtube =UniversityItem.youtube,
                statlink = UniversityItem.stat_link,
                ownershipform = UniversityItem.ownership_form,
                website = UniversityItem.website
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
object OtmlarManager {
    var selected1 by mutableStateOf("Umumiy")
}
@Composable
fun OtmlarRoyxatiCard(
    universitetname: String = "",
    locationname: String = "",
    location: String = "",
    instagram: String = "",
    facebook: String = "",
    telegram: String = "",
    youtube: String = "",
    statlink:String="",
    phonenumber:String="",
    ownershipform:Int=11,
    website:String=""
) {
    val context = LocalContext.current

    var isFirstBoxVisible by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Litsenziyasini yuklab olish",
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF2E6B30))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                fontSize = 12.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (ownershipform==12)"Nodavlat"
                else if(ownershipform==11)"Davlat"
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
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(150.dp)
        ) {
            // 1-Box (Dastlab ko‘rinadi)
            this@Column.AnimatedVisibility(
                visible = isFirstBoxVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .shadow(
                            4.dp,
                            RoundedCornerShape(16.dp),
                            ambientColor = Color.Gray,
                            spotColor = Color.Black
                        ) // Soya qo'shildi
                        .offset(y = (10).dp) // Boxni Column ustiga biroz surib qo'yish
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(10.dp)

                ) {
                    Row(modifier = Modifier
                        .clickable(
                        ) {
                            isFirstBoxVisible = false
                        }
                        .shadow(
                            4.dp,
                            RoundedCornerShape(8.dp),
                            ambientColor = Color.Gray,
                            spotColor = Color.Black
                        ) // Soya qo'shildi
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White)
                        .padding(vertical = 3.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Batafsil",
                            modifier = Modifier,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = Color(0xFF6DA966),
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Image(
                            painter = painterResource(id = R.drawable.arrowbottom),
                            contentDescription = "",
                            modifier = Modifier.size(10.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.otm),
                            contentDescription = "",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(5.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "$universitetname",
                            modifier = Modifier.width(170.dp),
                            maxLines = 3,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            lineHeight = 14.sp // Qatorlar orasidagi masofani kichraytirish
                        )
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 15.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Statistika",
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF2778D5))
                                .padding(vertical = 7.dp)
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable {
                                    if (statlink != "") {
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(statlink))
                                        context.startActivity(intent)
                                    }
                                }
                            ,
                            fontSize = 12.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(
                            modifier = Modifier
                                .width(30.dp)
                        )
                        Text(
                            text = "Rasmiy veb sahifa",
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF044085))
                                .padding(vertical = 7.dp)
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable {
                                    if (website != "") {
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(website))
                                        context.startActivity(intent)
                                    }
                                }
                            ,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // 2-Box (Dastlab ko‘rinmaydi, lekin 1-Box bosilganda chiqadi)
            this@Column.AnimatedVisibility(
                visible = !isFirstBoxVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .clickable {
                            isFirstBoxVisible = true
                        }
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(horizontal = 15.dp)
                        .padding(top = 20.dp)
                    ,
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(text = "Manzil: ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    lineHeight = 15.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = "$locationname",
                                    modifier = Modifier.clickable {
                                        isFirstBoxVisible = true
                                    },
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 2,
                                    fontSize = 12.sp,
                                    lineHeight = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = "Rasmiy ijtimoiy tarmoqlari", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.height(5.dp))
                            Divider(color = Color.Green, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.instagram),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .fillMaxWidth(1f)
                                        .clickable {
                                            if (instagram != "") {
                                                val intent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(instagram))
                                                context.startActivity(intent)
                                            }
                                        }
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Image(
                                    painter = painterResource(id = R.drawable.facebook),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .fillMaxWidth(1f)
                                        .clickable {
                                            if (facebook != "") {
                                                val intent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(facebook))
                                                context.startActivity(intent)
                                            }
                                        }
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Image(
                                    painter = painterResource(id = R.drawable.telegram),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .fillMaxWidth(1f)
                                        .clickable {
                                            if (telegram != "") {
                                                val intent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(telegram))
                                                context.startActivity(intent)
                                            }
                                        }
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Image(
                                    painter = painterResource(id = R.drawable.youtube),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .fillMaxWidth(1f)
                                        .clickable {
                                            if (youtube != "") {
                                                val intent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(youtube))
                                                context.startActivity(intent)
                                            }
                                        }
                                )
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clickable {
                                if (instagram != "") {
                                    val intent =
                                        Intent(Intent.ACTION_VIEW, Uri.parse(location))
                                    context.startActivity(intent)
                                }
                            }

                            ,
                        ){
                            Row (
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .align(Alignment.TopEnd)
                                    .background(Color(0xFF2778D5))
                                    .padding(vertical = 5.dp, horizontal = 10.dp)
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.marshrut),
                                    contentDescription = "",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Marshrut",
                                    modifier = Modifier,
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


