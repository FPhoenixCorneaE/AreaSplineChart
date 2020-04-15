package com.wkz.area_spline_chart

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.wkz.animation_dsl.animSet
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeAnimLayout(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    private val mKeyArray = ArrayList<Int>()
    private val mTimeArray = SparseArray<ArrayList<String>>()
    private val mDurationArray = SparseArray<Long>()
    private val mChildViews = ArrayList<TextView>(5)
    private val mChildTranslationXArray = SparseArray<Float>(5)
    private val mChildTextColor = Color.parseColor("#999999")
    private val mChildTextSize = 13f
    private var mTimeNow = 0
    private var mKey = 0
    private var mMoveDistance = 0f
    private var mDistancePerSecond = 0f
    private var mDuration = 0L
    private var mShowLabelCount = 0
    private var mRealTimeShowWidth = 0

    init {
        mKeyArray.apply {
            add(0)
            add(1)
            add(2)
            add(3)
            add(4)
            add(5)
            add(6)
            add(8)
            add(10)
            add(15)
            add(20)
            add(25)
            add(30)
            add(40)
            add(50)
            add(60)
            add(120)
            add(180)
            add(240)
            add(300)
            add(360)
            add(480)
            add(600)
            add(900)
            add(1200)
            forEachIndexed { index, value ->
                when (value) {
                    0 -> {
                        /* 0~1min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("")
                        })
                        mDurationArray.put(value, 60 * 1000)
                    }
                    1 -> {
                        /* 1~2min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                        })
                        mDurationArray.put(value, 60 * 1000)
                    }
                    2 -> {
                        /* 2~3min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                        })
                        mDurationArray.put(value, 60 * 1000)
                    }
                    3 -> {
                        /* 3~4min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                            add("3:00")
                        })
                        mDurationArray.put(value, 60 * 1000)
                    }
                    4 -> {
                        /* 4~5min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                            add("3:00")
                            add("4:00")
                        })
                        mDurationArray.put(value, 60 * 1000)
                    }
                    5 -> {
                        /* 5~6min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                            add("3:00")
                            add("4:00")
                            add("5:00")
                        })
                        mDurationArray.put(value, 60 * 1000)
                    }
                    6 -> {
                        /* 6~8min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("2:00")
                            add("4:00")
                            add("6:00")
                        })
                        mDurationArray.put(value, 2 * 60 * 1000)
                    }
                    8 -> {
                        /* 8~10min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("2:00")
                            add("4:00")
                            add("6:00")
                            add("8:00")
                        })
                        mDurationArray.put(value, 2 * 60 * 1000)
                    }
                    10 -> {
                        /* 10~15min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("5:00")
                            add("10:00")
                        })
                        mDurationArray.put(value, 5 * 60 * 1000)
                    }
                    15 -> {
                        /* 15~20min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("5:00")
                            add("10:00")
                            add("15:00")
                        })
                        mDurationArray.put(value, 5 * 60 * 1000)
                    }
                    20 -> {
                        /* 20~25min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("5:00")
                            add("10:00")
                            add("15:00")
                            add("20:00")
                        })
                        mDurationArray.put(value, 5 * 60 * 1000)
                    }
                    25 -> {
                        /* 25~30min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("10:00")
                            add("20:00")
                        })
                        mDurationArray.put(value, 10 * 60 * 1000)
                    }
                    30 -> {
                        /* 30~40min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("10:00")
                            add("20:00")
                            add("30:00")
                        })
                        mDurationArray.put(value, 10 * 60 * 1000)
                    }
                    40 -> {
                        /* 40~50min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("10:00")
                            add("20:00")
                            add("30:00")
                            add("40:00")
                        })
                        mDurationArray.put(value, 10 * 60 * 1000)
                    }
                    50 -> {
                        /* 50~60min */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("20:00")
                            add("40:00")
                        })
                        mDurationArray.put(value, 20 * 60 * 1000)
                    }
                    60 -> {
                        /* 1~2h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("20")
                            add("40")
                            add("1:00")
                        })
                        mDurationArray.put(value, 20 * 60 * 1000)
                    }
                    120 -> {
                        /* 2~3h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                        })
                        mDurationArray.put(value, 60 * 60 * 1000)
                    }
                    180 -> {
                        /* 3~4h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                            add("3:00")
                        })
                        mDurationArray.put(value, 60 * 60 * 1000)
                    }
                    240 -> {
                        /* 4~5h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                            add("3:00")
                            add("4:00")
                        })
                        mDurationArray.put(value, 60 * 60 * 1000)
                    }
                    300 -> {
                        /* 5~6h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("1:00")
                            add("2:00")
                            add("3:00")
                            add("4:00")
                            add("5:00")
                        })
                        mDurationArray.put(value, 60 * 60 * 1000)
                    }
                    360 -> {
                        /* 6~8h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("2:00")
                            add("4:00")
                            add("6:00")
                        })
                        mDurationArray.put(value, 2 * 60 * 60 * 1000)
                    }
                    480 -> {
                        /* 8~10h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("2:00")
                            add("4:00")
                            add("6:00")
                            add("8:00")
                        })
                        mDurationArray.put(value, 2 * 60 * 60 * 1000)
                    }
                    600 -> {
                        /* 10~15h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("5:00")
                            add("10:00")
                        })
                        mDurationArray.put(value, 5 * 60 * 60 * 1000)
                    }
                    900 -> {
                        /* 15~20h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("5:00")
                            add("10:00")
                            add("15:00")
                        })
                        mDurationArray.put(value, 5 * 60 * 60 * 1000)
                    }
                    1200 -> {
                        /* 20~24h */
                        mTimeArray.put(value, ArrayList<String>().apply {
                            add("5:00")
                            add("10:00")
                            add("15:00")
                            add("20:00")
                        })
                        mDurationArray.put(value, 5 * 60 * 60 * 1000)
                    }
                }
            }
        }
        mRealTimeShowWidth = getScreenWidth() - paddingStart - paddingEnd
    }

    private fun getKeyFromTimeNow(): Int {
        when {
            mTimeNow < 60 -> {
                mKey = 0
            }
            mTimeNow < 2 * 60 -> {
                mKey = 1
            }
            mTimeNow < 3 * 60 -> {
                mKey = 2
            }
            mTimeNow < 4 * 60 -> {
                mKey = 3
            }
            mTimeNow < 5 * 60 -> {
                mKey = 4
            }
            mTimeNow < 6 * 60 -> {
                mKey = 5
            }
            mTimeNow < 8 * 60 -> {
                mKey = 6
            }
            mTimeNow < 10 * 60 -> {
                mKey = 8
            }
            mTimeNow < 15 * 60 -> {
                mKey = 10
            }
            mTimeNow < 20 * 60 -> {
                mKey = 15
            }
            mTimeNow < 25 * 60 -> {
                mKey = 20
            }
            mTimeNow < 30 * 60 -> {
                mKey = 25
            }
            mTimeNow < 40 * 60 -> {
                mKey = 30
            }
            mTimeNow < 50 * 60 -> {
                mKey = 40
            }
            mTimeNow < 60 * 60 -> {
                mKey = 50
            }
            mTimeNow < 2 * 60 * 60 -> {
                mKey = 60
            }
            mTimeNow < 3 * 60 * 60 -> {
                mKey = 120
            }
            mTimeNow < 4 * 60 * 60 -> {
                mKey = 180
            }
            mTimeNow < 5 * 60 * 60 -> {
                mKey = 240
            }
            mTimeNow < 6 * 60 * 60 -> {
                mKey = 300
            }
            mTimeNow < 8 * 60 * 60 -> {
                mKey = 360
            }
            mTimeNow < 10 * 60 * 60 -> {
                mKey = 480
            }
            mTimeNow < 15 * 60 * 60 -> {
                mKey = 600
            }
            mTimeNow < 20 * 60 * 60 -> {
                mKey = 900
            }
            mTimeNow < 24 * 60 * 60 -> {
                mKey = 1200
            }
        }
        return mKey
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mChildViews.apply {
            val layoutParams = LayoutParams(-2, -2)
            layoutParams.gravity = Gravity.CENTER_VERTICAL
            layoutParams.leftMargin = width
            for (index in 0..4) {
                val child = TextView(context)
                child.setTextColor(mChildTextColor)
                child.textSize = mChildTextSize
                child.translationX = width.toFloat()
                if (!isInEditMode) {
                    child.typeface = ResourcesCompat.getFont(context,R.font.dinnextltpro_light_fixed)
                }
                addView(child, index)
                add(child)
            }
        }
    }

    /**
     * 根据TimeNow显示横轴时间段,并开启动画
     */
    fun showLabelWithTimeNow(timeNow: Int) {
        this.mTimeNow = timeNow
        this.mKey = getKeyFromTimeNow()
        this.mShowLabelCount = mTimeArray[mKey].size
        this.mDuration = mDurationArray[mKey]
        this.mDistancePerSecond =
            (mRealTimeShowWidth / (mShowLabelCount + 1)).toFloat() / (mDuration / 1000)
        this.mMoveDistance =
            (mRealTimeShowWidth / mShowLabelCount - mRealTimeShowWidth / (mShowLabelCount + 1)).toFloat()
        val startSecond = mKey * 60
        this.mChildViews.forEachIndexed { index, textView ->
            when {
                index < mShowLabelCount -> {
                    textView.visibility = View.VISIBLE
                    textView.text = mTimeArray.get(mKey)[index]
                    textView.measure(0, 0)
                    // 计算每个childView所在的位置
                    val translationX = mRealTimeShowWidth -
                            ((mShowLabelCount - 1 - index) * ((mRealTimeShowWidth) / mShowLabelCount) +
                                    textView.measuredWidth +
                                    (timeNow - startSecond) * mDistancePerSecond)
                    mChildTranslationXArray.put(index, translationX)
                    textView.translationX = translationX
                }
                else -> {
                    textView.visibility = View.GONE
                }
            }
        }
        if (mKey > 0) {
            animSet {
                anim {
                    values = floatArrayOf(
                        0f,
                        mMoveDistance
                    )
                    action = { value ->
                        mChildViews.forEachIndexed { index, textView ->
                            if (textView.visibility == View.VISIBLE) {
                                textView.translationX =
                                    mChildTranslationXArray[index] - (value as Float) * (index + 1)
                            }
                        }
                    }
                }
                onEnd = {
                    val keyIndex = mKeyArray.indexOf(mKey)
                    val nextKey = mKeyArray[keyIndex + 1]
                    val nextStartSecond = nextKey * 60
                    showLabelWithTimeNow(nextStartSecond)
                }
                duration = mDuration
                interpolator = LinearInterpolator()
                start()
            }
        } else {
            animSet {
                anim {
                    values = intArrayOf(mTimeNow, 60)
                    action = { value ->
                        mTimeNow = value as Int
                    }
                }
                onEnd = {
                    showLabelWithTimeNow(mTimeNow)
                }
                duration = ((60 - mTimeNow) * 1000).toLong()
                interpolator = LinearInterpolator()
                start()
            }
        }
    }

    /**
     * 格式化时间为"m:ss"格式
     *
     * @param millisecondValue 毫秒值
     */
    fun formatTime(millisecondValue: Long): String? {
        val dateFormat =
            SimpleDateFormat("m:ss", Locale.getDefault())
        // 设置时区，否则会有时差
        dateFormat.timeZone = TimeZone.getTimeZone("UT+08:00")
        return dateFormat.format(millisecondValue)
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    fun getScreenWidth(): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }
}
