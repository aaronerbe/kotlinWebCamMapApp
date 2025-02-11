package com.example.kotlinwebcammapapp.utils
//
//import android.text.Html
//import android.text.Spanned
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.text.style.TextDecoration
//
//@Composable
//fun renderHtmlText(html: String) {
//    val parsedText: Spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
//    val annotatedString = buildAnnotatedString {
//        append(parsedText.toString())
//    }
//
//    ClickableText(
//        text = annotatedString,
//        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.None),
//        onClick = { /* Handle clicks if needed */ }
//    )
//}

import android.text.Html

/**
 * Render HTML text - strips out html tags to make it look nice. needed on some trail descriptions
 * @param html HTML text to render
 * @return String - plain text
 */
fun renderHtmlText(html: String): String {
    return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
}