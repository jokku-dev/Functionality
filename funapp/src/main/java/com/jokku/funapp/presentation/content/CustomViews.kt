package com.jokku.funapp.presentation.content

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible

class CorrectButton : AppCompatButton, BtnEnabler {
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

class CorrectImageButton : AppCompatImageButton, ImageSetter {
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

class CorrectTextView : AppCompatTextView, TextSetter {
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

class CorrectProgressBar : ProgressBar, BarShow {
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