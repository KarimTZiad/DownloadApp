package com.example.downloadapp

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ObjectAnimator()

    private var rect = Rect()
    private var radius = 0
    private var paddingWidth = 0
    private var angle = 0.0F
    private var buttonText = context.getString(R.string.button_idle)
    private var rectWidth = 0.0F
    private var customBackgroundColor = context.getColor(R.color.blue)

    private val rectPaint = Paint().apply{
        color = context.getColor(R.color.blue_transparent)
        isDither = true
    }

    private val textPaint = Paint().apply{
        color = context.getColor(R.color.white)
        isDither = true
        textSize = 44f
        textAlign = Paint.Align.CENTER
    }

    private val arcPaint = Paint().apply{
        color = context.getColor(R.color.yellow)
        isDither = true
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Clicked -> {
                this.isEnabled = false
                buttonState = ButtonState.Loading
            }

            ButtonState.Loading -> {
                buttonText = context.getString(R.string.button_loading)
                valueAnimator.addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationCancel(animation: Animator?) {
                        super.onAnimationCancel(animation)
                        valueAnimator.removeListener(this)
                        angle = 0.0F
                        alpha = 1.0F
                        rectWidth = 0.0F
                        setBackgroundColor(customBackgroundColor)
                        buttonState = ButtonState.Completed
                    }
                })
                valueAnimator.start()
            }

            ButtonState.Completed -> {
                this.isEnabled = true
                valueAnimator.cancel()
                buttonText = context.getString(R.string.button_idle)
            }
        }
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            radius = getDimension(R.styleable.LoadingButton_circleRadius, 0F).toInt()
            paddingWidth = getDimension(R.styleable.LoadingButton_circlePaddingWidth, 0F).toInt()
            arcPaint.color = getColor(R.styleable.LoadingButton_circleColor, arcPaint.color)
            customBackgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, customBackgroundColor)
            rectPaint.color = getColor(R.styleable.LoadingButton_overlayColor, rectPaint.color)
            textPaint.color = getColor(R.styleable.LoadingButton_textColor, textPaint.color)
        }

        valueAnimator.target = this
        val alphaAnim = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.5f)

        valueAnimator.repeatCount = ValueAnimator.DURATION_INFINITE.toInt()
        //valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.duration = 3000
        valueAnimator.setValues(alphaAnim)
        valueAnimator.addUpdateListener {
            angle = 360F * it.animatedFraction
            rectWidth = width * it.animatedFraction
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = Rect(
            0,
            0,
            widthSize,
            heightSize
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setBackgroundColor(customBackgroundColor)

        canvas.drawRect(
            0F,
            0F,
            rectWidth,
            heightSize.toFloat(),
            rectPaint
        )

        canvas.drawArc(
            (widthSize - paddingWidth - 2*radius).toFloat(),
            (heightSize/2 - radius).toFloat(),
            (widthSize - paddingWidth).toFloat(),
            (heightSize/2 + radius).toFloat(),
            0.0F,
            angle,
            true,
            arcPaint
        )

        canvas.drawText(buttonText, widthSize/2f,heightSize/2f, textPaint)

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    fun startAnimation() {
        buttonState = ButtonState.Clicked
    }

    fun stopAnimation() {
        buttonState = ButtonState.Completed
    }

}