package pl.mrodkiewicz.imageeditor.ui

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import pl.mrodkiewicz.imageeditor.R


//work around the problem of not respecting font weights
 val latoThin = fontFamily(fonts = listOf(ResourceFont(R.font.lato_thin, FontWeight.W100, style = FontStyle.Normal)))
 val latoLight = fontFamily(fonts = listOf(ResourceFont(R.font.lato_light, FontWeight.W300, style = FontStyle.Normal)))
 val latoRegular = fontFamily(fonts = listOf(ResourceFont(R.font.lato_regular, FontWeight.W400, style = FontStyle.Normal)))
 val latoBold = fontFamily(ResourceFont(R.font.lato_bold, FontWeight.W700, style = FontStyle.Normal))
 val latoBlack = fontFamily(ResourceFont(R.font.lato_black, FontWeight.W900, style = FontStyle.Normal))
 val roboto = fontFamily( ResourceFont(R.font.roboto_mono_light))

val actionBarTextStyle = TextStyle(
    fontFamily = latoLight,
    color = yellowPrimary,
    fontSize = 32.sp
)

// Set of Material typography styles to start with
public val typography = Typography(
    body1 = TextStyle(
        fontFamily = latoBold,
        fontSize = 18.sp
    ),
    caption = TextStyle(
        color = Color.White,
        fontFamily = latoRegular,
        fontSize = 22.sp
    ),
    subtitle1 =  TextStyle(
        color = Color.White,
        fontFamily = latoLight,
        fontSize = 20.sp
    )
    /* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/
)