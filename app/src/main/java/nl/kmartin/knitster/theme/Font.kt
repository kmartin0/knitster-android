package nl.kmartin.knitster.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import nl.kmartin.knitster.R

@OptIn(ExperimentalTextApi::class)
val baloo2Variable: FontFamily = FontFamily(
    Font(
        R.font.baloo_2_variable,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400)),
    ),
    Font(
        R.font.baloo_2_variable,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500)),
    ),
    Font(
        R.font.baloo_2_variable,
        weight = FontWeight(525),
        variationSettings = FontVariation.Settings(FontVariation.weight(525)),
    ),
    Font(
        R.font.baloo_2_variable,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700)),
    ),
)