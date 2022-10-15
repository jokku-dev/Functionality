package com.jokku.images

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jokku.functionality.R

class MainActivity : AppCompatActivity() {

    private lateinit var story: TextView
    private lateinit var answer1: TextView
    private lateinit var answer2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        story = findViewById(R.id.story_tv)
        answer1 = findViewById(R.id.answer1_tv)
        answer2 = findViewById(R.id.answer2_tv)

        val secondStoryAfterFirstAnswer = getString(R.string.story_answer1_text)
        val secondStoryAfterSecondAnswer = getString(R.string.story_answer2_text)
        val firstAnswer = getString(R.string.answer1_text)
        val secondAnswer = getString(R.string.answer2_text)
        val firstAnswerSpannable = SpannableString(firstAnswer)
        val secondAnswerSpannable = SpannableString(secondAnswer)

        val firstAnswerClickable = MyClickableSpan {
            story.text = secondStoryAfterFirstAnswer
        }

        val secondAnswerClickable = MyClickableSpan {
            story.text = secondStoryAfterSecondAnswer
        }

        firstAnswerSpannable.setSpan(
            firstAnswerClickable,
            firstAnswer
        )
        secondAnswerSpannable.setSpan(
            secondAnswerClickable,
            secondAnswer
        )

        answer1.apply(firstAnswerSpannable)
        answer2.apply(secondAnswerSpannable)
    }

    inner class MyClickableSpan (private val lambda: () -> Unit) : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
            ds.color = Color.RED
        }

        override fun onClick(widget: View) {
            answer1.visibility = View.INVISIBLE
            answer2.visibility = View.INVISIBLE
            lambda.invoke()
        }
    }

    private fun TextView.apply(string: SpannableString) {
        text = string
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    private fun SpannableString.setSpan(clickable: MyClickableSpan, secondAnswer: String) {
        setSpan(
            clickable,
            0,
            secondAnswer.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}