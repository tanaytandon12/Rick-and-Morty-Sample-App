package com.weather.willy.willyweathersample.util

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes

fun Any.showPlaceholderIfTextIsEmpty(
    context: Context,
    textView: TextView,
    @StringRes textResId: Int,
    text: String?,
    @StringRes placeHolderResId: Int
) {
    textView.text = context.resources.getString(
        textResId,
        if (text != null && text.isNotEmpty())
            text
        else context.resources.getString(placeHolderResId)
    )
}