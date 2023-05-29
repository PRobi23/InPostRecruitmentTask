package pl.inpost.recruitmenttask.core.util

import android.content.Context

/***
 * Helper class, which needs to be used to render text to UI.
 */
sealed class UiText {
    data class DynamicString(val text: String) : UiText()

    data class StringResource(val resId: Int) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}