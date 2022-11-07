package com.jokku.jokeapp.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import com.jokku.jokeapp.R

class FavoriteDataView : LinearLayout {
    private val textView: CorrectTextView
    private val favoriteBtn: CorrectImageButton
    private val getBtn: CorrectButton
    private val checkBox: CheckBox
    private val progressBar: CorrectProgressBar

    //region
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    //endregion
    private fun init(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.FavoriteDataView, 0, 0).apply {
            try {
                val getBtnText = getString(R.styleable.FavoriteDataView_actionButtonText)
                val checkBoxText = getString(R.styleable.FavoriteDataView_checkBoxText)
                checkBox.text = checkBoxText
                getBtn.text = getBtnText
            } finally {
                recycle()
            }
        }
    }

    init {
        orientation = VERTICAL
        (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.favorite_data_view, this, true)
        val linear = getChildAt(0) as LinearLayout
        textView = linear.findViewById(R.id.joke_tv)
        favoriteBtn = linear.findViewById(R.id.favorite_ib)
        checkBox = getChildAt(1) as CheckBox
        getBtn = getChildAt(2) as CorrectButton
        progressBar = getChildAt(3) as CorrectProgressBar
    }

    fun listenFavoriteCheckBox(block: (checked: Boolean) -> Unit) =
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            block.invoke(isChecked)
        }

    fun handleFavoriteButton(block: () -> Unit) = favoriteBtn.setOnClickListener {
        block.invoke()
    }

    fun handleGetButton(block: () -> Unit) = getBtn.setOnClickListener {
        block.invoke()
    }

    fun show(state: MainViewModel.State) = state.show(progressBar, getBtn, textView, favoriteBtn)
}