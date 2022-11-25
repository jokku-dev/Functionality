package com.jokku.funapp.presentation.content

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.jokku.funapp.R
import com.jokku.funapp.presentation.fragment.FunItemViewModel

class FunDataView : LinearLayout {
    private lateinit var textView: CorrectTextView
    private lateinit var favoriteBtn: CorrectImageButton
    private lateinit var actionBtn: CorrectButton
    private lateinit var checkBox: CheckBox
    private lateinit var progressBar: CorrectProgressBar

    //region
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init()
    }

    //endregion
    private fun init() {
        orientation = VERTICAL
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.fun_data_view, this, true)
        val horizontalLinear = getChildAt(0) as LinearLayout
        textView = horizontalLinear.findViewById(R.id.joke_tv)
        favoriteBtn = horizontalLinear.findViewById(R.id.favorite_ib)
        checkBox = this.findViewById(R.id.favourite_cb)
        actionBtn = this.findViewById(R.id.action_btn)
        progressBar = this.findViewById(R.id.progress_bar)
    }

    fun checkBoxText(@StringRes id: Int) = checkBox.setText(id)
    fun actionButtonText(@StringRes id: Int) = actionBtn.setText(id)
    fun linkWith(funViewModel: FunItemViewModel) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            funViewModel.chooseFavorites(isChecked)
        }
        actionBtn.setOnClickListener {
            funViewModel.getItem()
        }
        favoriteBtn.setOnClickListener {
            funViewModel.changeItemStatus()
        }
    }

    fun show(state: State) {
        state.show(progressBar, actionBtn, textView, favoriteBtn)
    }
}