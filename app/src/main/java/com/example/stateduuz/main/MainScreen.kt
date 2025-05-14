package com.example.stateduuz.main

import android.util.Log
import android.widget.ToggleButton
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stateduuz.R
import com.example.stateduuz.mainpage.Home.HomeScreen
import com.example.stateduuz.mainpage.OliyTalim.OliyTalimScreen
import com.example.stateduuz.mainpage.ProfessionalTalim.ProfessionalTalimScreen
import com.example.stateduuz.mainpage.Qabul.QabulScreen
import com.example.stateduuz.ui.theme.ColorGreen
import com.example.stateduuz.ui.theme.ColorGreenMain
import com.example.stateduuz.ui.theme.ColorWhite
import com.example.stateduuz.ui.theme.StatEduUzTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navControllerfilter: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val navItemList = listOf(
        NavItem("Asosiy", R.drawable.icon, icon2 = R.drawable.iconf1, 0, "home"),
        NavItem("Oliy ta'lim", R.drawable.icon2, icon2 = R.drawable.iconf2, 0, "oliy"),
        NavItem("Kasb ta'lim", R.drawable.icon3, icon2 = R.drawable.iconf3, 0, "kasb"),
        NavItem("Qabul", R.drawable.icon4, icon2 = R.drawable.iconf4, 0, "qabul"),
    )
    val selectedTitle = remember { mutableStateOf("Asosiy") }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colorScheme.primary

    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "home"

    SideEffect {
        systemUiController.setStatusBarColor(color)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(coroutineScope, scaffoldState, title = selectedTitle.value) },
        drawerContent = {
            DrawerContent(
                coroutineScope,
                scaffoldState,
                isDarkTheme,
                onToggleTheme
            )
        },
        drawerElevation = 0.dp,
        drawerBackgroundColor = Color.Transparent,
        bottomBar = {
            BottomNavigationBar(
                navItemList = navItemList,
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    val selectedItem = navItemList.find { it.route == route }
                    selectedItem?.let {
                        selectedTitle.value = it.label // <-- yangi text
                    }
                    navController.navigate(route) {
                        popUpTo("home")
                        { inclusive = false } // faqat "home"ga qadar tozalaydi
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        MainContent(paddingValues, navController, navControllerfilter = navControllerfilter, isDarkMode = isDarkTheme)
    }
}

@Composable
fun BottomNavigationBar(
    navItemList: List<NavItem>,
    currentRoute: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.onPrimary) {
        navItemList.forEach { navItem ->
            val selected = navItem.route == currentRoute
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != navItem.route) onItemSelected(navItem.route)
                },
                icon = {
                    BadgedBox(badge = {
                        if (navItem.badgeCount > 0) Badge { Text(text = navItem.badgeCount.toString()) }
                    }) {
                        Icon(
                            painter = painterResource(id = if (selected) navItem.icon2 else navItem.icon),
                            contentDescription = navItem.label,
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                label = {
                    Text(
                        text = navItem.label,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun BackgroundImage(
    isDarkMode: Boolean
) {
    val imageRes = if (isDarkMode) R.drawable.background_dark else R.drawable.back2
    Crossfade(targetState = imageRes, label = "Background Transition") { targetImage ->
        Log.e("TAG", "BackgroundImage: $targetImage", )
        Image(
            painter = painterResource(id = targetImage),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun MainContent(
    paddingValues: PaddingValues,
    navController: NavController,
    navControllerfilter: NavController,
    isDarkMode: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets(0.dp))
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(ColorGreenMain)
        ) {
            BackgroundImage(
                isDarkMode =isDarkMode
            )
        }
        Box(modifier = Modifier.zIndex(1f)) {
            NavHost(navController = navController as NavHostController, startDestination = "home") {
                composable("home") { HomeScreen(navController = navController) }
                composable("oliy") { OliyTalimScreen(navControllerfilter = navControllerfilter) }
                composable("kasb") { ProfessionalTalimScreen(navController = navControllerfilter) }
                composable("qabul") { QabulScreen() }
            }
        }
    }
}


@Composable
fun TopBar(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState, title: String) {
    TopAppBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(Color.Transparent),
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(title)
                    }
                }
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch { scaffoldState.drawerState.open() }
            }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.nav_icon),
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun DrawerContent(
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .clip(RoundedCornerShape(bottomEnd = 24.dp, topEnd = 22.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            DrawerHeader()
            Spacer(modifier = Modifier.height(20.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorWhite)
            )
            DrawerItems(isDarkMode, onToggleTheme)
            Spacer(modifier = Modifier.weight(1f))
            DrawerFooter()
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Icon(
            painter = painterResource(id = R.drawable.stateduzu),
            contentDescription = "",
            modifier = Modifier.size(80.dp),
            tint = Color.White
        )
        Text(
            text = "stat.edu.uz",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = ColorWhite
        )
    }
}

@Composable
fun CustomThemeToggle(
    isDarkMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (isDarkMode) Color(0xFFE7F3FF) else Color(0xFFE7FFEA))
            .clickable { onToggle() } // <<< to'g'ri ishlayapti
            .width(100.dp)
            .height(30.dp)
            .padding(3.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            Box(modifier = Modifier.weight(0.5f), Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.moon),
                    contentDescription = "Dark Mode",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(if (isDarkMode) Color.White else ColorGreenMain)
                )
            }
            Box(modifier = Modifier.weight(0.5f), Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.sunny),
                    contentDescription = "Light Mode",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(if (isDarkMode) Color(0xFF111839) else Color.White)
                )
            }
        }

        // Moving Circle
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(46.dp)
                .align(if (isDarkMode) Alignment.CenterStart else Alignment.CenterEnd)
                .background(
                    if (isDarkMode) Color(0xFF111839) else ColorGreenMain,
                    shape = RoundedCornerShape(50)
                )
        )
    }
}


@Composable
fun DrawerFooter() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.drawer),
            contentDescription = "",
            modifier = Modifier.size(50.dp),
            tint = ColorWhite
        )
        Text(
            text = "Raqamli ta’lim texnologiyalarni rivojlantirish markazi",
            fontSize = 12.sp,
            lineHeight = 14.sp,
            color = ColorWhite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        )
    }
}

@Composable
fun DrawerItems(
    isDarkMode: Boolean, onToggleTheme: () -> Unit
) {
    val items = listOf(
        Pair(R.drawable.nav_1, "Ilova ko'rinishi"),
        Pair(R.drawable.nav_2, "Ilova tili"),
        Pair(R.drawable.nav_3, "Bog'lanish"),
        Pair(R.drawable.nav_4, "Rasmiy Sahifalar")
    )

    Spacer(modifier = Modifier.height(10.dp))
    var isToggled by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clickable { /* Bosilganda nima bo'lishini yozishing mumkin */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = items[0].first),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(ColorWhite)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = items[0].second, color = ColorWhite)
        Spacer(modifier = Modifier.weight(1f))

        CustomThemeToggle(
            isDarkMode = isDarkMode,
            onToggle = onToggleTheme // <<< BU YERDA
        )

    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable { /* Bosilganda nima bo'lishini yozishing mumkin */ },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        Image(
            painter = painterResource(id = items[1].first),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(ColorWhite)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = items[1].second, color = ColorWhite)
    }
    Spacer(modifier = Modifier.height(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable { /* Bosilganda nima bo'lishini yozishing mumkin */ },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        Image(
            painter = painterResource(id = items[2].first),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(ColorWhite)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = items[2].second, color = ColorWhite)
    }
    Spacer(modifier = Modifier.height(5.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()

            .padding(horizontal = 10.dp)
            .clickable { /* Bosilganda nima bo'lishini yozishing mumkin */ },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = items[3].first),
            contentDescription = "",
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(ColorWhite)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = items[3].second, color = ColorWhite)
    }
}