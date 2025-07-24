package com.example.average_value

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.average_value.ui.theme.Average_valueTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Average_valueTheme {

                MainScreen(

                    )
                }
            }
        }
    }


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val MyFontFamily = FontFamily(
        Font(R.font.sfmedium, FontWeight.Normal),
        Font(R.font.sfbold, FontWeight.Bold),
        Font(R.font.sflight, FontWeight.Light)
    )

    var input by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val digits =input
        .replace(",", ".")                  // заменяем запятые на точки
        .trim()                             // убираем пробелы в начале/конце
        .split(Regex("\\s+"))               // разделяем по пробелам
        .mapNotNull { it.toDoubleOrNull() } // пытаемся преобразовать в числа

    val average = if (digits.isNotEmpty()) {
        digits.average()
    } else {
        0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Среднее \nзначение",
            style = TextStyle(
                fontSize = 40.sp,
                lineHeight = 40.sp,
                fontFamily = MyFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Считайте ср. значение чисел, ставьте пробел после каждого числа для коррекного счета, например:\n(12  3.5 = 7.75)",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 20.sp,
                fontFamily = MyFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
            )
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            TextField(
                value = input,
                onValueChange = { newValue ->
                    // Фильтруем ввод, оставляя только цифры
                    input = newValue.filter {
                        it.isDigit() || it == ' ' || it == ',' || it == '.' || it == ';'
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done),
                placeholder = {
                    Text(
                        text = "Введите числа",
                        fontFamily = MyFontFamily, // Применяем ваш шрифт
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                },
                // Главное место для настройки цветов и фона TextField
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEAEAEA), // Фон поля при фокусе
                    unfocusedContainerColor = Color(0xFFEAEAEA), // Фон поля без фокуса
                    disabledContainerColor = Color(0xFFEAEAEA), // Фон поля в отключенном состоянии

                    focusedTextColor = Color.Black, // Цвет текста при фокусе
                    unfocusedTextColor = Color.Black, // Цвет текста без фокуса
                    disabledTextColor = Color.Black, // Цвет текста в отключенном состоянии

                    cursorColor = Color.Black, // Цвет курсора

                    focusedIndicatorColor = Color.Transparent, // Убираем нижнюю линию при фокусе
                    unfocusedIndicatorColor = Color.Transparent, // Убираем нижнюю линию без фокуса
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                ),
                // Применение скругления напрямую к TextField
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = MyFontFamily, // Применяем ваш шрифт к введенному тексту
                    fontSize = 18.sp,
                    color = Color.Black // Убедитесь, что цвет текста установлен здесь или через colors
                ),
            )
            // Кнопки справа
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),

                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Вставить из буфера
                IconButton(
                    onClick = {
                        val clipboardText = clipboardManager.getText()?.text ?: ""
                        input = clipboardText.filter {
                            it.isDigit() || it == ' ' || it == ',' || it == '.' || it == ';'
                        }

                        Toast
                            .makeText(context, "Цифры из буфера обмена вставлены", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier
                        .size(56.dp) // совпадает с высотой TextField
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.paste),
                        contentDescription = "Вставить",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Среднее значение: ${"%.2f".format(average)}",
            style = TextStyle(
                fontSize = 29.sp,
                lineHeight = 29.sp,
                fontFamily = MyFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
            )
        )
    }
}
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val MyFontFamily = FontFamily(
        Font(R.font.sfmedium, FontWeight.Normal),
        Font(R.font.sfbold, FontWeight.Bold),
        Font(R.font.sflight, FontWeight.Light)
    )

    var input by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val digits = input.filter { it.isDigit() }
        .map { it.digitToInt() }

    val average = if (digits.isNotEmpty()) {
        digits.average()
    } else {
        0.0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Среднее \nзначение",
            style = TextStyle(
                fontSize = 40.sp,
                lineHeight = 40.sp,
                fontFamily = MyFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Считайте ср. значение однозначных чисел (двух и трехзначные скоро!)",
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 20.sp,
                fontFamily = MyFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
            )
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            TextField(
                value = input,
                onValueChange = { newValue ->
                    // Фильтруем ввод, оставляя только цифры
                    input = newValue.filter { it.isDigit() }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done),
                placeholder = {
                    Text(
                        text = "Введите числа",
                        fontFamily = MyFontFamily, // Применяем ваш шрифт
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                },
                // Главное место для настройки цветов и фона TextField
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEAEAEA), // Фон поля при фокусе
                    unfocusedContainerColor = Color(0xFFEAEAEA), // Фон поля без фокуса
                    disabledContainerColor = Color(0xFFEAEAEA), // Фон поля в отключенном состоянии

                    focusedTextColor = Color.Black, // Цвет текста при фокусе
                    unfocusedTextColor = Color.Black, // Цвет текста без фокуса
                    disabledTextColor = Color.Black, // Цвет текста в отключенном состоянии

                    cursorColor = Color.Black, // Цвет курсора

                    focusedIndicatorColor = Color.Transparent, // Убираем нижнюю линию при фокусе
                    unfocusedIndicatorColor = Color.Transparent, // Убираем нижнюю линию без фокуса
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,

                    // Цвет плейсхолдера, если вы хотите его настроить отдельно через colors
                    // placeholderColor = Color.Gray,
                    // focusedPlaceholderColor = Color.Gray,
                    // unfocusedPlaceholderColor = Color.Gray,
                ),
                // Применение скругления напрямую к TextField
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = MyFontFamily, // Применяем ваш шрифт к введенному тексту
                    fontSize = 18.sp,
                    color = Color.Black // Убедитесь, что цвет текста установлен здесь или через colors
                ),
            )
            // Кнопки справа
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),

                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Вставить из буфера
                IconButton(
                    onClick = {
                        val clipboardText = clipboardManager.getText()?.text ?: ""
                        input = clipboardText.filter { it.isDigit() }

                        Toast
                            .makeText(context, "Цифры из буфера обмена вставлены", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier
                        .size(56.dp) // совпадает с высотой TextField
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.paste),
                        contentDescription = "Вставить",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Среднее значение: ${"%.2f".format(average)}",
            style = TextStyle(
                fontSize = 29.sp,
                lineHeight = 29.sp,
                fontFamily = MyFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
            )
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    Average_valueTheme {
        MainScreen()
    }
}
