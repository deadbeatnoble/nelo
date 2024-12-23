package com.example.nelo.presentation.screen.browse

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.nelo.MainViewModel
import com.example.nelo.R
import com.example.nelo.domain.model.ChapterModel
import com.example.nelo.domain.model.MangaModel
import com.example.nelo.presentation.navigation.DetailNavScreens
import com.example.nelo.presentation.navigation.RootNavGraphs
import com.example.nelo.util.Genres
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState", "UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun BrowseScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    val isEndReached = remember {
        derivedStateOf {
            (gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1 >
                    gridState.layoutInfo.totalItemsCount - 2
        }
    }

    val selectedColor = Color.Red

    val tabs = listOf(
        "Popular",
        "Latest",
        "Filter"
    )

    val isLoading = mainViewModel.isLoading.value
    val feedResponse = mainViewModel.feedResponse.value
    val feedResponseError = mainViewModel.feedResponseError.value
    val onUpdate = mainViewModel.onUpdate.value
    val toggleTheme = mainViewModel.toggleTheme.value
    val theme = if (toggleTheme) R.drawable.light else R.drawable.dark
    val selectedTab = mainViewModel.selectedTab.value
    val keyword = mainViewModel.keyword
    val selectedKeyword = mainViewModel.selectedKeyword

    val themeBackgroundColor = if (toggleTheme) Color.White else Color.Black
    val themeTextColor = if (toggleTheme) Color.Black else Color.White

    val showGenres = remember {
        mutableStateOf(false)
    }

    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val keyTypesExpanded = remember {
        mutableStateOf(false)
    }
    val keyTypes = listOf(
        "Everything",
        "Name title",
        "Alternative name",
        "Author"
    )
    //val selectedKeyword = remember { mutableStateOf(keyTypes[0]) }

    val orderByExpanded = remember {
        mutableStateOf(false)
    }
    val orderBy = listOf(
        "Latest Updates",
        "Top view",
        "New Manga",
        "A-Z"
    )
    val selectedOrderBy = remember {
        mutableStateOf(orderBy[0])
    }

    val statusExpanded = remember {
        mutableStateOf(false)
    }
    val status = listOf(
        "Ongoing and Complete",
        "Ongoing",
        "Completed"
    )
    val selectedStatus = remember {
        mutableStateOf(status[0])
    }

    val excluded = remember {
        mutableStateListOf<Int>()
    }
    val include = remember {
        mutableStateListOf<Int>()
    }

    //val keyword = remember { mutableStateOf("") }
    val searchExpanded = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        if (keyword.value.isNotEmpty()) {
            searchExpanded.value = true
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 50.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Red)
                                .padding(start = 20.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Reset",
                                color = themeTextColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clickable {
                                        if (
                                            selectedKeyword.value == keyTypes[0]
                                            && selectedOrderBy.value == orderBy[0]
                                            && selectedStatus.value == status[0]
                                            && include.isEmpty()
                                            && excluded.isEmpty()
                                        ) {
                                            scope.launch {
                                                bottomSheetState.collapse()
                                            }
                                        } else {
                                            selectedKeyword.value = keyTypes[0]
                                            selectedOrderBy.value = orderBy[0]
                                            selectedStatus.value = status[0]
                                            include.clear()
                                            excluded.clear()
                                        }
                                    }
                            )
                            Button(
                                onClick = {
                                    mainViewModel._currentPage.value = 1
                                    val url = buildString {
                                        append("https://manganato.com/advanced_search?s=all")

                                        if(include.isNotEmpty()) {
                                            append("&g_i=_${include.joinToString("_")}_")
                                        }

                                        if(excluded.isNotEmpty()) {
                                            append("&g_e=_${excluded.joinToString("_")}_")
                                        }

                                        when(selectedStatus.value) {
                                            status[0] -> append("")
                                            status[1] -> append("&sts=ongoing")
                                            status[2] -> append("&sts=completed")
                                        }

                                        when(selectedOrderBy.value) {
                                            orderBy[0] -> append("")
                                            orderBy[1] -> append("&orby=topview")
                                            orderBy[2] -> append("&orby=newest")
                                            orderBy[3] -> append("&orby=az")
                                        }

                                        append("&page=${mainViewModel._currentPage.value}")

                                        if (keyword.value.isNotEmpty()) {
                                            when(selectedKeyword.value) {
                                                keyTypes[0] -> append("")
                                                keyTypes[1] -> append("&keyt=title")
                                                keyTypes[2] -> append("&keyt=alternative")
                                                keyTypes[3] -> append("&keyt=author")
                                            }

                                            append("&keyw=${keyword.value}")
                                        }
                                    }
                                    mainViewModel.advancedSearchUrl.value = url
                                    mainViewModel._selectedTab.value = tabs[2]
                                    mainViewModel.feedChange.value = true
                                    mainViewModel._hasNextPage.value = true
                                    mainViewModel._feedResponse.value.clear()
                                    mainViewModel.getAdvancedSearchFeed()
                                    scope.launch {
                                        bottomSheetState.collapse()
                                    }
                                }
                            ) {
                                Text(
                                    text = "Filter",
                                    color = themeTextColor
                                )
                            }
                        }
                        Divider()
                    }
                    item {
                        val menuIcon = if (keyTypesExpanded.value) R.drawable.drop_up else R.drawable.drop_down
                        val textFieldSize = remember {
                            mutableStateOf(Size.Zero)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {
                            OutlinedTextField(
                                value = selectedKeyword.value,
                                onValueChange = {
                                    selectedKeyword.value = it
                                },
                                enabled = false,
                                textStyle = TextStyle(
                                    fontSize = 15.sp
                                ),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    disabledBorderColor = themeTextColor,
                                    disabledTrailingIconColor = themeTextColor,
                                    disabledTextColor = themeTextColor
                                ),
                                label = {
                                        Text(
                                            text = "Keyword",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Red
                                        )
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = menuIcon),
                                        contentDescription = "more/less icon",
                                        modifier = Modifier
                                            .clickable {
                                                keyTypesExpanded.value = !keyTypesExpanded.value
                                            }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(themeBackgroundColor)
                                    .onGloballyPositioned { coordinate ->
                                        textFieldSize.value = coordinate.size.toSize()
                                    }
                            )

                            DropdownMenu(
                                expanded = keyTypesExpanded.value,
                                onDismissRequest = {
                                    keyTypesExpanded.value = false
                                },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { textFieldSize.value.width.toDp() })
                                    .background(themeBackgroundColor)
                            ) {
                                keyTypes.forEach {
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedKeyword.value = it
                                            keyTypesExpanded.value = false
                                        }
                                    ) {
                                        Text(
                                            text = it,
                                            color = themeTextColor,
                                            fontSize = 15.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp))
                    }
                    item {
                        val menuIcon = if (orderByExpanded.value) R.drawable.drop_up else R.drawable.drop_down
                        val textFieldSize = remember {
                            mutableStateOf(Size.Zero)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {
                            OutlinedTextField(
                                value = selectedOrderBy.value,
                                onValueChange = {
                                    selectedOrderBy.value = it
                                },
                                enabled = false,
                                textStyle = TextStyle(
                                    fontSize = 15.sp
                                ),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    disabledBorderColor = themeTextColor,
                                    disabledTrailingIconColor = themeTextColor,
                                    disabledTextColor = themeTextColor
                                ),
                                label = {
                                    Text(
                                        text = "Order By",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Red
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = menuIcon),
                                        contentDescription = "more/less icon",
                                        modifier = Modifier
                                            .clickable {
                                                orderByExpanded.value = !orderByExpanded.value
                                            }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(themeBackgroundColor)
                                    .onGloballyPositioned { coordinate ->
                                        textFieldSize.value = coordinate.size.toSize()
                                    }
                            )

                            DropdownMenu(
                                expanded = orderByExpanded.value,
                                onDismissRequest = {
                                    orderByExpanded.value = false
                                },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { textFieldSize.value.width.toDp() })
                                    .background(themeBackgroundColor)
                            ) {
                                orderBy.forEach {
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedOrderBy.value = it
                                            orderByExpanded.value = false
                                        }
                                    ) {
                                        Text(
                                            text = it,
                                            color = themeTextColor,
                                            fontSize = 15.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp))
                    }
                    item {
                        val menuIcon = if (statusExpanded.value) R.drawable.drop_up else R.drawable.drop_down
                        val textFieldSize = remember {
                            mutableStateOf(Size.Zero)
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {
                            OutlinedTextField(
                                value = selectedStatus.value,
                                onValueChange = {
                                    selectedStatus.value = it
                                },
                                enabled = false,
                                textStyle = TextStyle(
                                    fontSize = 15.sp
                                ),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    disabledBorderColor = themeTextColor,
                                    disabledTrailingIconColor = themeTextColor,
                                    disabledTextColor = themeTextColor
                                ),
                                label = {
                                    Text(
                                        text = "Status",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Red
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = menuIcon),
                                        contentDescription = "more/less icon",
                                        modifier = Modifier
                                            .clickable {
                                                statusExpanded.value = !statusExpanded.value
                                            }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(themeBackgroundColor)
                                    .onGloballyPositioned { coordinate ->
                                        textFieldSize.value = coordinate.size.toSize()
                                    }
                            )

                            DropdownMenu(
                                expanded = statusExpanded.value,
                                onDismissRequest = {
                                    statusExpanded.value = false
                                },
                                modifier = Modifier
                                    .width(with(LocalDensity.current) { textFieldSize.value.width.toDp() })
                                    .background(themeBackgroundColor)
                            ) {
                                status.forEach {
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedStatus.value = it
                                            statusExpanded.value = false
                                        }
                                    ) {
                                        Text(
                                            text = it,
                                            color = themeTextColor,
                                            fontSize = 15.sp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp))
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 20.dp)
                                .clickable {
                                    showGenres.value = !showGenres.value
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = "Genres",
                                color = Color.Red,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            )
                            Icon(
                                painter = if(showGenres.value) painterResource(id = R.drawable.drop_up) else painterResource(id = R.drawable.drop_down),
                                contentDescription = "More/Less icon",
                                tint = themeTextColor
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    if(showGenres.value) {
                        items(Genres.items.keys.toList()) { genre ->
                            val options = listOf(
                                "neutral",
                                "include",
                                "exclude"
                            )
                            var selectedOption = if (include.contains(Genres.items[genre])) {
                                options[1]
                            } else if (excluded.contains(Genres.items[genre])) {
                                options[2]
                            } else {
                                options[0]
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp)
                                    .clickable {
                                        when (selectedOption) {
                                            options[0] -> {
                                                selectedOption = options[1]
                                                include.add(Genres.items[genre]!!)
                                            }

                                            options[1] -> {
                                                selectedOption = options[2]
                                                include.remove(Genres.items[genre]!!)
                                                excluded.add(Genres.items[genre]!!)
                                            }

                                            options[2] -> {
                                                selectedOption = options[0]
                                                excluded.remove(Genres.items[genre]!!)
                                            }
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TripleCheckBox(
                                    selectedOption = selectedOption,
                                    themeTextColor = themeTextColor
                                )
                                Spacer(modifier = Modifier.width(32.dp))
                                Text(
                                    text = genre,
                                    color = themeTextColor,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = themeBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(themeBackgroundColor)
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(themeTextColor)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(66.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(!searchExpanded.value){
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "NaNaNa",
                                color = themeBackgroundColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        if(!searchExpanded.value){
                            IconButton(
                                onClick = {
                                    searchExpanded.value = true
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.search),
                                    contentDescription = "Search icon",
                                    tint = themeBackgroundColor,
                                    modifier = Modifier
                                        .size(25.dp)
                                )
                            }
                        } else {
                            val keyboardController = LocalSoftwareKeyboardController.current

                            OutlinedTextField(
                                value = keyword.value,
                                onValueChange = {
                                    keyword.value = it
                                },
                                textStyle = TextStyle(
                                    fontSize = 15.sp
                                ),
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.search),
                                        contentDescription = "Search icon",
                                        tint = themeTextColor.copy(alpha = 0.6f)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "close",
                                        tint = themeTextColor.copy(alpha = 0.6f),
                                        modifier = Modifier
                                            .clickable {
                                                if (keyword.value.isEmpty()) {
                                                    searchExpanded.value = false
                                                }
                                                keyword.value = ""
                                            }
                                    )
                                },
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()

                                        mainViewModel._currentPage.value = 1
                                        val url = buildString {
                                            append("https://manganato.com/advanced_search?s=all")

                                            if(include.isNotEmpty()) {
                                                append("&g_i=_${include.joinToString("_")}_")
                                            }

                                            if(excluded.isNotEmpty()) {
                                                append("&g_e=_${excluded.joinToString("_")}_")
                                            }

                                            when(selectedStatus.value) {
                                                status[0] -> append("")
                                                status[1] -> append("&sts=ongoing")
                                                status[2] -> append("&sts=completed")
                                            }

                                            when(selectedOrderBy.value) {
                                                orderBy[0] -> append("")
                                                orderBy[1] -> append("&orby=topview")
                                                orderBy[2] -> append("&orby=newest")
                                                orderBy[3] -> append("&orby=az")
                                            }

                                            append("&page=${mainViewModel._currentPage.value}")

                                            if (keyword.value.isNotEmpty()) {
                                                when(selectedKeyword.value) {
                                                    keyTypes[0] -> append("")
                                                    keyTypes[1] -> append("&keyt=title")
                                                    keyTypes[2] -> append("&keyt=alternative")
                                                    keyTypes[3] -> append("&keyt=author")
                                                }

                                                append("&keyw=${keyword.value}")
                                            }
                                        }
                                        mainViewModel.advancedSearchUrl.value = url
                                        mainViewModel._selectedTab.value = tabs[2]
                                        mainViewModel.feedChange.value = true
                                        mainViewModel._hasNextPage.value = true
                                        mainViewModel._feedResponse.value.clear()
                                        mainViewModel.getAdvancedSearchFeed()
                                    }
                                ),
                                singleLine = true,
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = themeTextColor,
                                    containerColor = themeBackgroundColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        IconButton(onClick = {
                            mainViewModel._toggleTheme.value = !toggleTheme
                        }) {
                            Icon(
                                painter = painterResource(id = theme),
                                contentDescription = "Toggle Night/Dark mode",
                                tint = colorResource(R.color.orange),
                                modifier = if (toggleTheme) {
                                    Modifier
                                        .size(25.dp)
                                } else {
                                    Modifier
                                        .size(25.dp)
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(color = colorResource(R.color.dark_blue))
                                        .padding(2.dp)
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                mainViewModel.updateSelectedTab(tabs[0])
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.hot),
                            contentDescription = "Icon",
                            tint = if (selectedTab == "Popular") selectedColor else themeBackgroundColor,
                            modifier = Modifier
                                .size(25.dp)
                        )
                        Text(
                            text = "Popular",
                            color = if (selectedTab == "Popular") selectedColor else themeBackgroundColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                mainViewModel.updateSelectedTab(tabs[1])
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.latest),
                            contentDescription = "Icon",
                            tint = if (selectedTab == "Latest") selectedColor else themeBackgroundColor,
                            modifier = Modifier
                                .size(25.dp)
                        )

                        Text(
                            text = "Latest ",
                            color = if (selectedTab == "Latest") selectedColor else themeBackgroundColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    if (bottomSheetState.isExpanded) {
                                        bottomSheetState.collapse()
                                    } else {
                                        bottomSheetState.expand()
                                    }
                                }
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "Icon",
                            tint = if (selectedTab == "Filter") selectedColor else themeBackgroundColor,
                            modifier = Modifier
                                .size(25.dp)
                        )

                        Text(
                            text = "Filter ",
                            color = if (selectedTab == "Filter") selectedColor else themeBackgroundColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (isLoading && feedResponse.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (feedResponseError.isNotEmpty() && feedResponse.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(themeBackgroundColor)
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = feedResponseError,
                                fontSize = 18.sp,
                                color = themeTextColor
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    mainViewModel.getFeed()
                                }
                            ) {
                                Text(
                                    text = "Retry"
                                )
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        state = gridState,
                        contentPadding = PaddingValues(bottom = 50.dp)
                    ) {
                        items(feedResponse) {
                            onUpdate
                            Box(
                                modifier = Modifier
                                    .aspectRatio(225f / 337f)
                                    .padding(6.dp)
                                    .clickable {
                                        if (navController.currentBackStackEntry?.destination?.route != DetailNavScreens.MangaScreen.route) {
                                            mainViewModel._mangaDetail.value =
                                                MangaModel(
                                                    title = it.title,
                                                    thumbnail = it.thumbnail,
                                                    authors = emptyList(),
                                                    genres = emptyList(),
                                                    status = null,
                                                    updatedAt = null,
                                                    description = null,
                                                    view = null,
                                                    rating = it.rating,
                                                    mangaUrl = it.mangaUrl,
                                                    chapterList = emptyList()
                                                )
                                            mainViewModel._chapterDetail.value = ChapterModel(
                                                title = null,
                                                view = null,
                                                uploadedAt = null,
                                                chapterUrl = null,
                                                pages = emptyList()
                                            )
                                            //mainViewModel.getManga()
                                            navController.navigate(RootNavGraphs.DetailGraph.route)
                                        }
                                    }
                            ) {
                                AsyncImage(
                                    model = it.thumbnail,
                                    contentDescription = "Manga Cover",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .drawWithCache {
                                            val gradient = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.6f)
                                                ),
                                                startY = (size.height / 4) + (size.height / 2),
                                                endY = size.height
                                            )
                                            onDrawWithContent {
                                                drawContent()
                                                drawRect(
                                                    gradient,
                                                    blendMode = BlendMode.Multiply
                                                )
                                            }
                                        }
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.35f)
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .align(Alignment.TopEnd)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(horizontal = 3.dp)
                                    ) {
                                        Text(
                                            text = it.rating ?: "?",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "rate",
                                            tint = Color.Yellow
                                        )
                                    }
                                }

                                Text(
                                    text = it.title ?: "Unknown",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 13.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(6.dp),
                                )
                            }
                        }

                        item {
                            if (isEndReached.value) {
                                LaunchedEffect(true) {
                                    if (mainViewModel.selectedTab.value != tabs[2]) {
                                        mainViewModel.getFeed()
                                    } else {

                                        var url = mainViewModel.advancedSearchUrl.value
                                        var spliturl = url.split("&").toMutableList()
                                        for (miniurl in spliturl) {
                                            if (miniurl.startsWith("page=")) {
                                                spliturl[spliturl.indexOf(miniurl)] = "page=${mainViewModel._currentPage.value}"
                                            }
                                        }
                                        url = spliturl.joinToString("&")

                                        mainViewModel.advancedSearchUrl.value = url
                                        mainViewModel.getAdvancedSearchFeed()
                                    }
                                }
                            }
                        }

                        item {
                            if (isLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripleCheckBox(
    selectedOption: String,
    themeTextColor: Color
) {
    when(selectedOption) {
        "neutral" -> {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = 1.dp,
                        color = themeTextColor,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
        "include" -> {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "include",
                    tint = themeTextColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                )
            }
        }
        "exclude" -> {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "exclude",
                    tint = themeTextColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                )
            }
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Preview
@Composable
fun BrowseScreenPreview() {
    BrowseScreen(
        navController = rememberNavController(),
        mainViewModel = MainViewModel()
    )
}