package com.jokku.jokeapp.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible

class CorrectButton : AppCompatButton, EnableBtn {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun enable(enabled: Boolean) {
        isEnabled = enabled
    }
}

class CorrectImageButton : AppCompatImageButton, SetImage {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun set(arg: Int) {
        setImageResource(arg)
    }

    override fun show(visible: Boolean) {
        isVisible = visible
    }
}

class CorrectTextView : AppCompatTextView, SetText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun set(arg: String) {
        text = arg
    }
}

class CorrectProgressBar : ProgressBar, ShowBar {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun show(visible: Boolean) {
        isVisible = visible
    }
}