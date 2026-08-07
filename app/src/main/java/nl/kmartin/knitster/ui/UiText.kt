package nl.kmartin.knitster.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Represents text that can be resolved to a String only within a Composable context.
 * Use [toDisplayString] to get the display value.
 */
sealed interface UiText {
    data class Literal(val value: String) : UiText
    data class Resource(@param:StringRes val resId: Int, val args: List<Any> = emptyList()) : UiText
}

/**
 * Resolves this [UiText] to its display String.
 */
@Composable
fun UiText.toDisplayString(): String = when (this) {
    is UiText.Literal -> value
    is UiText.Resource -> stringResource(resId, *args.toTypedArray())
}

/**
 * Resolves this [UiText] to its display String, or null if this is null.
 */
@Composable
fun UiText?.toDisplayStringOrNull(): String? = this?.toDisplayString()
