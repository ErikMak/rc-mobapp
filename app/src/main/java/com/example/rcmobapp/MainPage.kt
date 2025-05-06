package com.example.rcmobapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rcmobapp.api.NetworkResponse
import com.example.rcmobapp.api.WeatherModel
import com.example.rcmobapp.ui.theme.Pink40
import com.example.rcmobapp.ui.theme.Purple40
import com.example.rcmobapp.ui.theme.Yellow40
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(viewModel: AppViewModel) {
    val tabs = listOf(
        TabItem("Главная", Icons.Filled.Home),
        TabItem("Конфигурация", Icons.Filled.Settings),
    )
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("reCar - App") },
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = { Icon(tab.icon, contentDescription = null) },
                        text = { Text(tab.title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> TabContent1(viewModel)
                1 -> TabContent2()
            }
        }
    }
}

fun getWeatherCondition(code: Int): String {
    return when (code) {
        0 -> "Ясное небо"
        1 -> "Преим. ясно"
        2 -> "Перем. облачность"
        3 -> "Пасмурно"
        45, 48 -> "Туман и оседающий изморозь"
        51 -> "Морось: слабая"
        53 -> "Морось: умеренная"
        55 -> "Морось: интенсивная"
        56 -> "Замерзающая морось: слабая"
        57 -> "Замерзающая морось: плотная интенсивность"
        61 -> "Дождь: слабый"
        63 -> "Дождь: умеренный"
        65 -> "Дождь: сильный"
        66 -> "Замерзающий дождь: слабой интенсивности"
        67 -> "Замерзающий дождь: сильной интенсивности"
        71 -> "Снегопад: слабый"
        73 -> "Снегопад: умеренный"
        75 -> "Снегопад: сильный"
        77 -> "Снежные зерна"
        80 -> "Ливневые дожди: слабые"
        81 -> "Ливневые дожди: умеренные"
        82 -> "Ливневые дожди: сильные"
        85 -> "Снежные ливни: слабые"
        86 -> "Снежные ливни: сильные"
        else -> "Неизвестный код погоды"
    }
}

data class TabItem(val title: String, val icon: ImageVector)

@Composable
fun WeatherDetails(data: WeatherModel) {
    // Логика
    var weatherCond : String = ""
    weatherCond = getWeatherCondition(data.current.weather_code)

    var wind : String = data.current.wind_speed_10m.toString() + " км/ч"
    var prec : String = data.current.precipitation.toString() + " мм"
    var temp : String = data.current.temperature_2m.toString() + " C°"

    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
    val date: Date? = dateFormat.parse(data.current.time)

    val locale = Locale("ru", "RU")
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"), locale)
    calendar.time = date ?: return

    // День недели (0 - понедельник, 6 - воскресенье)
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
    // Месяц (1 - январь, 12 - декабрь)
    val monthOfYear = calendar.get(Calendar.MONTH) + 1
    // Час дня (0 - 23)
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    println(hourOfDay)

    // Время суток (0 - 8, каждое число)
    val timeOfDay = (hourOfDay / 3).coerceIn(0, 8)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Text("Погодные данные")
    }
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    )
    {
        Card (colors = CardDefaults.cardColors(
            containerColor = Pink40,
            contentColor = Purple40
        )){
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    WeatherDataBox("Погода", weatherCond)
                    WeatherDataBox("Ветер", wind)
                }

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    WeatherDataBox("Температура", temp)
                    WeatherDataBox("Осадки", prec)
                }

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    WeatherDataBox("Время суток", timeOfDay.toString())
                    WeatherDataBox("День недели", dayOfWeek.toString())
                }

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    WeatherDataBox("Неделя", weekOfYear.toString())
                    WeatherDataBox("Месяц", monthOfYear.toString())
                }
            }
        }
    }
}

@Composable
fun GetDataButton(onClick: () -> Unit) {
    Button(onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = Yellow40,
            contentColor = Color.Black)) {
        Text("Загрузить данные")
    }
}

@Composable
fun WeatherDataRow(key: String, value: String) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp, vertical = 6.dp
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = key,
            )
            Text(text = value)
        }
    }
}

@Composable
fun WeatherDataBox(key: String, value: String) {
    Column (
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = value, fontWeight = FontWeight.SemiBold)
        Text(text = key, fontSize = 12.sp, fontWeight = FontWeight.Normal, color = Color.Gray)
    }
}

@Composable
fun TabContent1(viewModel: AppViewModel) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    val weatherResult = viewModel.weatherResult.observeAsState()
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Pink40,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .width(240.dp)
                .padding(vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                WeatherDataRow("Широта: ", state.latitude.toString())
                WeatherDataRow("Долгота: ", state.longitude.toString())
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = state.address)
        }

        Spacer(modifier = Modifier.height(16.dp))



        GetDataButton(onClick = {
            viewModel.getWeatherData()
        })


        Spacer(modifier = Modifier.height(16.dp))

        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }

            null -> {}
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabContent2() {
    val scrollState = rememberScrollState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
        ) {
            val violation = arrayOf("Несоблюдение дистанции", "Обгон",
                "Превышение скорости", "Несоблюдение очередность проезда", "Проезд на красный свет")
            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember { mutableStateOf(violation[0]) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    label = { Text(text = "Нарушения водителя") },
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    violation.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

