package com.jokku.funapp.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import com.jokku.funapp.R

class FunDataView : LinearLayout {
    private lateinit var textView: CorrectTextView
    private lateinit var favoriteBtn: CorrectImageButton
    private lateinit var getBtn: CorrectButton
    private lateinit var checkBox: CheckBox
    private lateinit var progressBar: CorrectProgressBar
    //region
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    //endregion
    private fun init(attrs: AttributeSet) {
        orientation = VERTICAL
        (context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.fun_data_view, this, true)
        val horizontalLinear = getChildAt(0) as LinearLayout
        textView = horizontalLinear.findViewById(R.id.joke_tv)
        favoriteBtn = horizontalLinear.findViewById(R.id.favorite_ib)
        checkBox = this.findViewById(R.id.favourite_joke_cb)
        getBtn = this.findViewById(R.id.joke_btn)
        progressBar = this.findViewById(R.id.progress_bar)

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

    fun linkWith(funViewModel: FunItemViewModel) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            funViewModel.chooseFavorites(isChecked)
        }
        favoriteBtn.setOnClickListener {
            funViewModel.changeItemStatus()
        }
        getBtn.setOnClickListener {
            funViewModel.getItem()
        }
    }

    fun show(state: BaseFunViewModel.State) {
        state.show(progressBar, getBtn, textView, favoriteBtn)
    }
}