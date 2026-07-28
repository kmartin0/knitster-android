package nl.kmartin.knitster.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import nl.kmartin.knitster.R

@OptIn(ExperimentalTextApi::class)
fun quicksandVariable(weight: FontWeight): FontFamily = FontFamily(
    Font(
        R.font.quicksand_variable,
        weight = weight,
        variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
    )
)