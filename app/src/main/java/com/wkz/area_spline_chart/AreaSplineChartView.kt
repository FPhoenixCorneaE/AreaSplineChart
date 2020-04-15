package com.wkz.area_spline_chart

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import java.util.*
import kotlin.math.roundToInt

/**
 * @author wkz
 * @desc 温度曲线面积图
 * @date 2020-04-11 11:15
 */
class AreaSplineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    /**
     * 贝塞尔曲线控制点系数
     */
    private var mBezierSplineSmoothness = 0.2f

    /**
     * 第一个点：距离左边的距离
     */
    private var mFirstPointMarginLeft = dp2px(30f)

    /**
     * 最后一个点：距离右边的距离、Y轴线颜色、文字画笔、文字大小、文字距离最后一个点的距离
     */
    private val mLastPointMarginRight = dp2px(35f)
    private val mLastPointYAxisLineColor = Color.parseColor("#999999")
    private val mLastPointTextPaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val mLastPointTextSize = sp2px(17f).toFloat()
    private val mLastPointTextMarginLeft = sp2px(8f).toFloat()

    /**
     * XY轴线：线画笔、线宽度、线颜色
     */
    private val mXyAxisLinePaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private var mXyAxisLineWidth = dp2px(0.5f)
    private var mXyAxisLineColor = Color.parseColor("#eeeeee")

    /**
     * X轴:Y坐标
     */
    private var mXAxisY = 0f

    /**
     * Y轴:文字画笔、文字大小、文字颜色、文字边界、文字宽度、文字高度、距离顶部的距离、刻度列表、刻度间隔
     */
    private val mYAxisTextPaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private var mYAxisTextSize = sp2px(13f).toFloat()
    private var mYAxisTextColor = Color.parseColor("#999999")
    private val mYAxisTextBounds = Rect()
    private var mYAxisTextWidth = 0
    private var mYAxisTextHeight = 0
    private val mYAxisMarginTop = dp2px(18f)
    private var mYAxisScaleList: MutableList<String> =
        ArrayList()
    private var mYAxisScaleInterval = 50f

    /**
     * 曲线:点数据、点坐标、画笔、颜色、宽度、面积颜色、路径、面积路径、辅助路径
     */
    private var tempList: List<TempBean> = ArrayList()
    private var mSecondTempList: List<TempBean> = ArrayList()
    private val mFirstSplinePointList: MutableList<Point> =
        ArrayList()
    private val mSecondSplinePointList: MutableList<Point> =
        ArrayList()
    private val mFirstSplinePaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val mSecondSplinePaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val mFirstAreaSplinePaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val mSecondAreaSplinePaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private var mFirstSplineStartColor = Color.parseColor("#10FCAB00")
    private var mFirstSplineCenterColor = Color.parseColor("#50FCAB00")
    private var mFirstSplineEndColor = Color.parseColor("#ffFCAB00")
    private var mSecondSplineStartColor = Color.parseColor("#10FF3F00")
    private var mSecondSplineCenterColor = Color.parseColor("#50FF3F00")
    private var mSecondSplineEndColor = Color.parseColor("#ffFF3F00")
    private var mSplineWidth = 2.5f
    private val mFirstAreaSplineStartColor = Color.TRANSPARENT
    private val mFirstAreaSplineEndColor = Color.parseColor("#40FCAB00")
    private val mSecondAreaSplineStartColor = Color.TRANSPARENT
    private val mSecondAreaSplineEndColor = Color.parseColor("#40FF3F00")
    private val mSplinePath = Path()
    private val mAreaSplinePath = Path()
    private val mAssistPath = Path()
    private var mPathMeasure: PathMeasure? = null

    /**
     * 曲线终点：画笔、雷达涟漪画笔、半径、雷达涟漪最大半径、雷达涟漪半径
     */
    private val mSplineDestinationPaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val mSplineDestinationRadarRipplePaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val mSplineDestinationRadius = dp2px(3.5f)
    private val mSplineDestinationRadarRippleMaxRadius = dp2px(14.5f)
    private var mSplineDestinationRadarRippleRadius = 0f
    private var mAnimatedValue = 0f

    /**
     * 画布原点：X坐标、Y坐标
     */
    private var mCanvasOriginX = 0
    private var mCanvasOriginY = 0
    private fun init() {
        initPaint()
        initRadarRippleAnimator()
        initYAxisScaleList()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        mYAxisTextPaint.style = Paint.Style.FILL_AND_STROKE
        mLastPointTextPaint.style = Paint.Style.FILL_AND_STROKE
        mXyAxisLinePaint.strokeWidth = mXyAxisLineWidth
        mFirstSplinePaint.strokeWidth = mSplineWidth
        mFirstSplinePaint.style = Paint.Style.STROKE
        mSecondSplinePaint.strokeWidth = mSplineWidth
        mSecondSplinePaint.style = Paint.Style.STROKE
        mFirstAreaSplinePaint.style = Paint.Style.FILL
        mSecondAreaSplinePaint.style = Paint.Style.FILL
        mSplineDestinationPaint.style = Paint.Style.FILL
        mSplineDestinationRadarRipplePaint.style = Paint.Style.FILL
    }

    /**
     * 雷达涟漪动画
     */
    private fun initRadarRippleAnimator() {
        // 雷达涟漪动画
        val radarRippleAnimator = ValueAnimator()
        radarRippleAnimator.setFloatValues(0f, 1f)
        radarRippleAnimator.repeatCount = ValueAnimator.INFINITE
        radarRippleAnimator.repeatMode = ValueAnimator.RESTART
        radarRippleAnimator.addUpdateListener { valueAnimator ->
            mAnimatedValue = valueAnimator.animatedValue as Float
            mSplineDestinationRadarRippleRadius =
                mSplineDestinationRadius + (mSplineDestinationRadarRippleMaxRadius - mSplineDestinationRadius) * mAnimatedValue
            invalidate()
        }
        radarRippleAnimator.startDelay = 100
        radarRippleAnimator.duration = 1500L
        radarRippleAnimator.start()
    }

    /**
     * 初始化Y轴刻度列表
     */
    private fun initYAxisScaleList() {
        mYAxisScaleList.add("")
        mYAxisScaleList.add("50°")
        mYAxisScaleList.add("100°")
        mYAxisScaleList.add("150°")
        mYAxisScaleList.add("200°")
        mYAxisScaleList.add("250°")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val yItemHeight: Float = ((height - mYAxisMarginTop) / mYAxisScaleList.size)

        // 绘制Y轴线和刻度文字
        drawYAxisLineAndScaleText(canvas, yItemHeight)

        // 移动画布原点到(mCanvasOriginX, mCanvasOriginY)
        mCanvasOriginX = mFirstPointMarginLeft.toInt()
        mCanvasOriginY = (height - (3 * mYAxisTextHeight shr 1) - mXyAxisLineWidth).toInt()
        canvas.translate(mCanvasOriginX.toFloat(), mCanvasOriginY.toFloat())
        mXAxisY -= mCanvasOriginY.toFloat()
        canvas.save()
        // 设置曲线点XY坐标
        setSplinePointXY()
        // 绘制第二条曲线
        drawSecondSpline(canvas)
        // 绘制第一条曲线
        drawFirstSpline(canvas)
        // 画最后一个点的Y轴线
        initLastPointYAxisLine(canvas, yItemHeight)
        // 绘制第一条曲线的终点
        drawFirstSplineDestination(canvas)
        // 绘制第二条曲线的终点
        drawSecondSplineDestination(canvas)
        canvas.restore()
    }

    private fun drawYAxisLineAndScaleText(
        canvas: Canvas,
        yItemHeight: Float
    ) {
        // 先画Y轴和横线
        var baseLengthStr = ""
        for (str in mYAxisScaleList) {
            if (str.length > baseLengthStr.length) {
                baseLengthStr = str
            }
        }
        mYAxisTextPaint.color = mYAxisTextColor
        mYAxisTextPaint.textSize = mYAxisTextSize
        if (!isInEditMode) {
            mYAxisTextPaint.typeface = ResourcesCompat.getFont(
                context,
                R.font.dinnextltpro_light_fixed
            )
        }
        mYAxisTextPaint.getTextBounds(baseLengthStr, 0, baseLengthStr.length, mYAxisTextBounds)
        mYAxisTextWidth = mYAxisTextBounds.width()
        mYAxisTextHeight = mYAxisTextBounds.height()
        for (i in mYAxisScaleList.indices) {
            // 画Y轴文字
            val text = mYAxisScaleList[mYAxisScaleList.size - i - 1]
            val textBounds = Rect()
            mYAxisTextPaint.getTextBounds(text, 0, text.length, textBounds)
            val topY =
                yItemHeight * i + yItemHeight - mYAxisTextHeight + mYAxisMarginTop
            if (i == mYAxisScaleList.size - 1) {
                mXAxisY = topY + (mYAxisTextHeight shr 1)
            }
            val baseLine = measureBaseLine(mYAxisTextPaint, text, topY)
            canvas.drawText(
                text,
                (mYAxisTextWidth - textBounds.width() shr 1.toFloat().toInt()).toFloat(),
                baseLine + mYAxisTextHeight,
                mYAxisTextPaint
            )
            // 画横线
            mXyAxisLinePaint.color = mXyAxisLineColor
            canvas.drawLine(
                0f,
                topY + (mYAxisTextHeight shr 1),
                width.toFloat(),
                topY + (mYAxisTextHeight shr 1),
                mXyAxisLinePaint
            )
        }
    }

    /**
     * 设置曲线点XY坐标
     */
    private fun setSplinePointXY() {
        if (tempList.isEmpty()) {
            return
        }
        // 最开始的一个点的时间
        val timestampStart = tempList[0].timestamp
        // 最后一个点的时间
        val timeStampEnd = tempList[tempList.size - 1].timestamp
        // 点的时间差，单位(秒)
        val timeDifference = timeStampEnd - timestampStart
        val scale = (timeDifference / tempList.size).toFloat()
        for (i in mFirstSplinePointList.indices) {
            val temp = tempList[i].temp
            val timestamp = tempList[i].timestamp
            val y =
                (temp * (mCanvasOriginY + mYAxisTextHeight / 2) / (mYAxisScaleInterval * mYAxisScaleList.size)).toInt()
            val x =
                ((timestamp - timestampStart) * (width - mCanvasOriginX - mLastPointMarginRight) * scale / timeDifference).toInt()
            val point = mFirstSplinePointList[i]
            point[x] = -y
        }
        for (i in mSecondSplinePointList.indices) {
            val temp = mSecondTempList[i].temp
            val timestamp = mSecondTempList[i].timestamp
            val y =
                (temp * (mCanvasOriginY + mYAxisTextHeight / 2) / (mYAxisScaleInterval * mYAxisScaleList.size)).toInt()
            val x =
                ((timestamp - timestampStart) * (width - mCanvasOriginX - mLastPointMarginRight) * scale / timeDifference).toInt()
            val point = mSecondSplinePointList[i]
            point[x] = -y
        }
    }

    /**
     * 绘制第二条曲线
     *
     * @param canvas
     */
    private fun drawSecondSpline(canvas: Canvas) {
        if (mSecondSplinePointList.isNotEmpty()) {
            val firstSecondPoint = mSecondSplinePointList[0]
            val lastSecondPoint =
                mSecondSplinePointList[mSecondSplinePointList.size - 1]
            measurePath(mSecondSplinePointList)
            val dst2 = Path()
            dst2.rLineTo(0f, 0f)
            val distance2 = mPathMeasure!!.length
            if (mPathMeasure!!.getSegment(0f, distance2, dst2, true)) {
                // 绘制第二条曲线
                canvas.save()
                /*创建线性颜色渐变器*/
                @SuppressLint("DrawAllocation") val graphGradient: Shader =
                    LinearGradient(
                        firstSecondPoint.x.toFloat(),
                        firstSecondPoint.y.toFloat(),
                        lastSecondPoint.x.toFloat(),
                        lastSecondPoint.y.toFloat(),
                        intArrayOf(
                            mSecondSplineStartColor,
                            mSecondSplineCenterColor,
                            mSecondSplineEndColor
                        ),
                        floatArrayOf(0f, 0.5f, 1f),
                        Shader.TileMode.CLAMP
                    )
                mSecondSplinePaint.shader = graphGradient
                canvas.drawPath(dst2, mSecondSplinePaint)
                canvas.restore()

                // 绘制曲线区域
                mAreaSplinePath.lineTo(lastSecondPoint.x.toFloat(), mXAxisY)
                mAreaSplinePath.lineTo(firstSecondPoint.x.toFloat(), mXAxisY)
                mAreaSplinePath.lineTo(firstSecondPoint.x.toFloat(), firstSecondPoint.y.toFloat())

                /*创建线性颜色渐变器*/
                @SuppressLint("DrawAllocation") val linearGradient: Shader =
                    LinearGradient(
                        ((firstSecondPoint.x + lastSecondPoint.x) * 3 shr 2).toFloat(),
                        mXAxisY,
                        lastSecondPoint.x.toFloat(),
                        lastSecondPoint.y.toFloat(),
                        intArrayOf(mSecondAreaSplineStartColor, mSecondAreaSplineEndColor),
                        floatArrayOf(0f, 1f),
                        Shader.TileMode.CLAMP
                    )
                mSecondAreaSplinePaint.shader = linearGradient
                canvas.save()
                canvas.clipPath(mAreaSplinePath)
                canvas.drawPaint(mSecondAreaSplinePaint)
                canvas.restore()
            }
        }
    }

    /**
     * 画最后一个点的Y轴线
     *
     * @param canvas
     * @param yItemHeight
     */
    private fun initLastPointYAxisLine(
        canvas: Canvas,
        yItemHeight: Float
    ) {
        val lastPoint =
            mFirstSplinePointList[mFirstSplinePointList.size - 1]
        mXyAxisLinePaint.color = mLastPointYAxisLineColor
        canvas.drawLine(
            lastPoint.x.toFloat(),
            mXAxisY,
            lastPoint.x.toFloat(),
            -(height - yItemHeight - mYAxisMarginTop),
            mXyAxisLinePaint
        )
    }

    /**
     * 绘制第一条曲线
     *
     * @param canvas
     */
    private fun drawFirstSpline(canvas: Canvas) {
        if (mFirstSplinePointList.isNotEmpty()) {
            val firstPoint = mFirstSplinePointList[0]
            val lastPoint =
                mFirstSplinePointList[mFirstSplinePointList.size - 1]
            measurePath(mFirstSplinePointList)
            val dst = Path()
            dst.rLineTo(0f, 0f)
            val distance = mPathMeasure!!.length
            if (mPathMeasure!!.getSegment(0f, distance, dst, true)) {
                // 绘制第一条曲线
                var save = canvas.save()
                /*创建线性颜色渐变器*/
                @SuppressLint("DrawAllocation") val firstSplineShader: Shader =
                    LinearGradient(
                        firstPoint.x.toFloat(),
                        firstPoint.y.toFloat(),
                        lastPoint.x.toFloat(),
                        lastPoint.y.toFloat(),
                        intArrayOf(
                            mFirstSplineStartColor,
                            mFirstSplineCenterColor,
                            mFirstSplineEndColor
                        ),
                        floatArrayOf(0f, 0.5f, 1f),
                        Shader.TileMode.CLAMP
                    )
                mFirstSplinePaint.shader = firstSplineShader
                canvas.drawPath(dst, mFirstSplinePaint)
                canvas.restoreToCount(save)

                // 绘制曲线区域
                mAreaSplinePath.lineTo(lastPoint.x.toFloat(), mXAxisY)
                mAreaSplinePath.lineTo(firstPoint.x.toFloat(), mXAxisY)
                mAreaSplinePath.lineTo(firstPoint.x.toFloat(), firstPoint.y.toFloat())

                /*创建线性颜色渐变器*/
                @SuppressLint("DrawAllocation") val firstSplineAreaShader: Shader =
                    LinearGradient(
                        ((firstPoint.x + lastPoint.x) * 3 shr 2).toFloat(),
                        mXAxisY,
                        lastPoint.x.toFloat(),
                        lastPoint.y.toFloat(),
                        intArrayOf(mFirstAreaSplineStartColor, mFirstAreaSplineEndColor),
                        floatArrayOf(0f, 1f),
                        Shader.TileMode.CLAMP
                    )
                mFirstAreaSplinePaint.shader = firstSplineAreaShader
                save = canvas.save()
                canvas.clipPath(mAreaSplinePath)
                canvas.drawPaint(mFirstAreaSplinePaint)
                canvas.restoreToCount(save)
            }
        }
    }

    /**
     * 绘制第一条曲线的终点
     *
     * @param canvas
     */
    private fun drawFirstSplineDestination(canvas: Canvas) {
        if (mFirstSplinePointList.isNotEmpty()) {
            val lastPoint =
                mFirstSplinePointList[mFirstSplinePointList.size - 1]
            // 画第一条线的终点(雷达涟漪效果)
            mSplineDestinationPaint.color = mFirstSplineEndColor
            canvas.drawCircle(
                lastPoint.x.toFloat(),
                lastPoint.y.toFloat(),
                mSplineDestinationRadius,
                mSplineDestinationPaint
            )
            mSplineDestinationRadarRipplePaint.color = computeGradientColor(
                mFirstSplineEndColor,
                mFirstSplineStartColor,
                mAnimatedValue
            )
            canvas.drawCircle(
                lastPoint.x.toFloat(),
                lastPoint.y.toFloat(),
                mSplineDestinationRadarRippleRadius,
                mSplineDestinationRadarRipplePaint
            )
            // 画第一条线的最后一个值
            val lastTemp = tempList[tempList.size - 1].temp
            val lastTempText = "$lastTemp°"
            val textBounds = Rect()
            mLastPointTextPaint.getTextBounds(lastTempText, 0, lastTempText.length, textBounds)
            val lastTempTextBaseLine = measureBaseLine(
                mLastPointTextPaint,
                lastTempText,
                lastPoint.y - (textBounds.height() shr 1).toFloat()
            )
            mLastPointTextPaint.color = mFirstSplineEndColor
            mLastPointTextPaint.textSize = mLastPointTextSize
            if (!isInEditMode) {
                mLastPointTextPaint.typeface = ResourcesCompat.getFont(
                    context,
                    R.font.dinnextltpro_condensed_fixed
                )
            }
            canvas.drawText(
                lastTempText,
                lastPoint.x + mLastPointTextMarginLeft,
                lastTempTextBaseLine,
                mLastPointTextPaint
            )
        }
    }

    /**
     * 绘制第二条曲线的终点
     *
     * @param canvas 画布
     */
    private fun drawSecondSplineDestination(canvas: Canvas) {
        if (mSecondSplinePointList.isNotEmpty()) {
            // 画第二条线的终点(雷达涟漪效果)
            val lastPoint =
                mSecondSplinePointList[mSecondSplinePointList.size - 1]
            mSplineDestinationPaint.color = mSecondSplineEndColor
            canvas.drawCircle(
                lastPoint.x.toFloat(),
                lastPoint.y.toFloat(),
                mSplineDestinationRadius,
                mSplineDestinationPaint
            )
            mSplineDestinationRadarRipplePaint.color = computeGradientColor(
                mSecondSplineEndColor,
                mSecondSplineStartColor,
                mAnimatedValue
            )
            canvas.drawCircle(
                lastPoint.x.toFloat(),
                lastPoint.y.toFloat(),
                mSplineDestinationRadarRippleRadius,
                mSplineDestinationRadarRipplePaint
            )
            // 画第二条线的最后一个值
            val secondLastTemp = mSecondTempList[mSecondTempList.size - 1].temp
            val secondLastTempText = "$secondLastTemp°"
            val secondTextBounds = Rect()
            mLastPointTextPaint.getTextBounds(
                secondLastTempText,
                0,
                secondLastTempText.length,
                secondTextBounds
            )
            val secondLastTempTextBaseLine = measureBaseLine(
                mLastPointTextPaint,
                secondLastTempText,
                lastPoint.y - (secondTextBounds.height() shr 1).toFloat()
            )
            mLastPointTextPaint.color = mSecondSplineEndColor
            mLastPointTextPaint.textSize = mLastPointTextSize
            canvas.drawText(
                secondLastTempText,
                lastPoint.x + mLastPointTextMarginLeft,
                secondLastTempTextBaseLine,
                mLastPointTextPaint
            )
        }
    }

    /**
     * 计算贝塞尔曲线的Path和PathMeasure
     */
    private fun measurePath(pointList: List<Point>) {
        mSplinePath.reset()
        mAreaSplinePath.reset()
        mAssistPath.reset()
        var prePreviousPointX = Float.NaN
        var prePreviousPointY = Float.NaN
        var previousPointX = Float.NaN
        var previousPointY = Float.NaN
        var currentPointX = Float.NaN
        var currentPointY = Float.NaN
        var nextPointX: Float
        var nextPointY: Float
        val lineSize = pointList.size
        for (valueIndex in 0 until lineSize) {
            if (java.lang.Float.isNaN(currentPointX)) {
                val point = pointList[valueIndex]
                currentPointX = point.x.toFloat()
                currentPointY = point.y.toFloat()
            }
            if (java.lang.Float.isNaN(previousPointX)) {
                // 是否是第一个点
                if (valueIndex > 0) {
                    val point = pointList[valueIndex - 1]
                    previousPointX = point.x.toFloat()
                    previousPointY = point.y.toFloat()
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX
                    previousPointY = currentPointY
                }
            }
            if (java.lang.Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    val point = pointList[valueIndex - 2]
                    prePreviousPointX = point.x.toFloat()
                    prePreviousPointY = point.y.toFloat()
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX
                    prePreviousPointY = previousPointY
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                val point = pointList[valueIndex + 1]
                nextPointX = point.x.toFloat()
                nextPointY = point.y.toFloat()
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX
                nextPointY = currentPointY
            }
            if (valueIndex == 0) {
                // 将Path移动到开始点
                mSplinePath.moveTo(currentPointX, currentPointY)
                mAreaSplinePath.moveTo(currentPointX, currentPointY)
                mAssistPath.moveTo(currentPointX, currentPointY)
            } else {
                // 求出控制点坐标
                val firstDiffX = currentPointX - prePreviousPointX
                val firstDiffY = currentPointY - prePreviousPointY
                val secondDiffX = nextPointX - previousPointX
                val secondDiffY = nextPointY - previousPointY
                val firstControlPointX =
                    previousPointX + mBezierSplineSmoothness * firstDiffX
                val firstControlPointY =
                    previousPointY + mBezierSplineSmoothness * firstDiffY
                val secondControlPointX =
                    currentPointX - mBezierSplineSmoothness * secondDiffX
                val secondControlPointY =
                    currentPointY - mBezierSplineSmoothness * secondDiffY
                // 画出曲线
                mSplinePath.cubicTo(
                    firstControlPointX, firstControlPointY, secondControlPointX,
                    secondControlPointY,
                    currentPointX, currentPointY
                )
                mAreaSplinePath.cubicTo(
                    firstControlPointX, firstControlPointY, secondControlPointX,
                    secondControlPointY,
                    currentPointX, currentPointY
                )
                // 将控制点保存到辅助路径上
                mAssistPath.lineTo(firstControlPointX, firstControlPointY)
                mAssistPath.lineTo(secondControlPointX, secondControlPointY)
                mAssistPath.lineTo(currentPointX, currentPointY)
            }

            // 更新值,
            prePreviousPointX = previousPointX
            prePreviousPointY = previousPointY
            previousPointX = currentPointX
            previousPointY = currentPointY
            currentPointX = nextPointX
            currentPointY = nextPointY
        }
        mPathMeasure = PathMeasure(mSplinePath, false)
    }

    /**
     * 计算文字基线
     *
     * @param textPaint
     * @param text
     * @param topY      文字Y轴顶部坐标
     * @return
     */
    private fun measureBaseLine(
        textPaint: Paint,
        text: String,
        topY: Float
    ): Float {
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val fontMetricsInt = textPaint.fontMetricsInt
        val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
        return topY + (textBounds.height() shr 1) + dy
    }

    private fun sp2px(spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            resources.displayMetrics
        ).toInt()
    }

    private fun dp2px(dpValue: Float): Float {
        val scale = resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    /**
     * 计算渐变后的颜色
     *
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     * @param rate       渐变率（0,1）
     * @return 渐变后的颜色，当rate=0时，返回startColor，当rate=1时返回endColor
     */
    private fun computeGradientColor(startColor: Int, endColor: Int, rate: Float): Int {
        var rate = rate
        if (rate < 0) {
            rate = 0f
        }
        if (rate > 1) {
            rate = 1f
        }
        val alpha =
            Color.alpha(endColor) - Color.alpha(startColor)
        val red = Color.red(endColor) - Color.red(startColor)
        val green =
            Color.green(endColor) - Color.green(startColor)
        val blue =
            Color.blue(endColor) - Color.blue(startColor)
        return Color.argb(
            (Color.alpha(startColor) + alpha * rate).roundToInt(),
            (Color.red(startColor) + red * rate).roundToInt(),
            (Color.green(startColor) + green * rate).roundToInt(),
            (Color.blue(startColor) + blue * rate).roundToInt()
        )
    }

    /**
     * 设置Y轴刻度
     *
     * @param scale  单位刻度
     * @param strArr 刻度上展示的文字，若不设置则展示0,50,100,150,200,250
     */
    fun setYAxis(scale: Float, vararg strArr: String) {
        if (strArr.isNotEmpty()) {
            mYAxisScaleInterval = scale
            mYAxisScaleList = mutableListOf(*strArr)
            invalidate()
        }
    }

    fun setTempList(tempList: List<TempBean>?) {
        if (tempList != null && tempList.isNotEmpty()) {
            this.tempList = tempList
            mFirstSplinePointList.clear()
            for (i in tempList.indices) {
                val point = Point()
                mFirstSplinePointList.add(point)
            }
            invalidate()
        }
    }

    fun setSecondTempList(tempList: List<TempBean>?) {
        if (tempList != null && tempList.isNotEmpty()) {
            mSecondTempList = tempList
            mSecondSplinePointList.clear()
            for (i in tempList.indices) {
                val point = Point()
                mSecondSplinePointList.add(point)
            }
            invalidate()
        }
    }

    init {
        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.AreaSplineChartView)
            mYAxisTextColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_yAxisTextColor,
                mYAxisTextColor
            )
            mYAxisTextSize = typedArray.getDimension(
                R.styleable.AreaSplineChartView_ascv_yAxisTextSize,
                mYAxisTextSize
            )
            mXyAxisLineWidth = typedArray.getDimension(
                R.styleable.AreaSplineChartView_ascv_xyAxisLineWidth,
                mXyAxisLineWidth
            )
            mXyAxisLineColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_xyAxisLineColor,
                mXyAxisLineColor
            )
            mFirstPointMarginLeft = typedArray.getDimension(
                R.styleable.AreaSplineChartView_ascv_firstPointMarginLeft,
                mFirstPointMarginLeft
            )
            mFirstSplineStartColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_firstSplineStartColor,
                mFirstSplineStartColor
            )
            mFirstSplineCenterColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_firstSplineCenterColor,
                mFirstSplineCenterColor
            )
            mFirstSplineEndColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_firstSplineEndColor,
                mFirstSplineEndColor
            )
            mSecondSplineStartColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_secondSplineStartColor,
                mSecondSplineStartColor
            )
            mSecondSplineCenterColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_secondSplineCenterColor,
                mSecondSplineCenterColor
            )
            mSecondSplineEndColor = typedArray.getColor(
                R.styleable.AreaSplineChartView_ascv_secondSplineEndColor,
                mSecondSplineEndColor
            )
            mSplineWidth = typedArray.getDimension(
                R.styleable.AreaSplineChartView_ascv_splineWidth,
                dp2px(mSplineWidth)
            )
            mBezierSplineSmoothness = typedArray.getFloat(
                R.styleable.AreaSplineChartView_ascv_lineSmoothness,
                mBezierSplineSmoothness
            )
            typedArray.recycle()
        }
        init()
    }
}