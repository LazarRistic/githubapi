package com.overswayit.githubapi.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.overswayit.githubapi.R

class AccInfoItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private lateinit var labelTextView: TextView
    private lateinit var textTextView: TextView
    private lateinit var separatorView: View

    private var label: String = ""
        set(value) {
            labelTextView.text = value
            field = value
        }

    var text: String = ""
        set(value) {
            textTextView.text = value
            field = value
        }

    private var separatorVisibility: Boolean = true
        set(value) {
            separatorView.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

    @ColorInt
    var labelColor: Int = 0

    @ColorInt
    var textColor: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AccInfoItemView, 0, 0
        )

        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_acc_info_item, this, true)

        findViews()
        getValues(typedArray)

        typedArray.recycle()
    }

    private fun getValues(typedArray: TypedArray) {
        label = typedArray.getString(R.styleable.AccInfoItemView_label).toString()
        text = typedArray.getString(R.styleable.AccInfoItemView_text).toString()
        separatorVisibility = typedArray.getBoolean(R.styleable.AccInfoItemView_separatorVisibility, true)

        labelColor = typedArray.getColor(
            R.styleable.AccInfoItemView_labelColor,
            Color.parseColor("#9B9B9B")
        )
        textColor = typedArray.getColor(
            R.styleable.AccInfoItemView_textColor,
            Color.parseColor("#4A4A4A")
        )
    }

    private fun findViews() {
        labelTextView = findViewById(R.id.label)
        textTextView = findViewById(R.id.text_holder)
        separatorView = findViewById(R.id.separator)
    }
}