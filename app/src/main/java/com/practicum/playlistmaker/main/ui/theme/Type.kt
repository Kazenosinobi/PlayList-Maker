package com.practicum.playlistmaker.main.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R

val PlayMakerFont = FontFamily(
    Font(R.font.ys_display_bold, FontWeight.Bold),
    Font(R.font.ys_display_heavy, FontWeight.Black),
    Font(R.font.ys_display_light, FontWeight.Light),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_thin, FontWeight.Thin),
    Font(R.font.ys_text_bold, FontWeight.Bold),
    Font(R.font.ys_text_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.ys_text_light, FontWeight.Light),
    Font(R.font.ys_text_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.ys_text_medium, FontWeight.Medium),
    Font(R.font.ys_text_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.ys_text_regular, FontWeight.Normal),
    Font(R.font.ys_text_regular_italic, FontWeight.Normal, FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = PlayMakerFont,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = PlayMakerFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PlayMakerFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 19.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PlayMakerFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 13.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PlayMakerFont,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        lineHeight = 23.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

