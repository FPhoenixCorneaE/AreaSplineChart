package com.wkz.area_spline_chart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTimeMillis = System.currentTimeMillis()
        cvAreaSpline.setTempList(ArrayList<TempBean>().apply {
            add(TempBean(30, (currentTimeMillis / 1000)))
            add(
                TempBean(
                    31,
                    (currentTimeMillis / 1000 + 2)
                )
            )
            add(
                TempBean(
                    32,
                    (currentTimeMillis / 1000 + 4)
                )
            )
            add(
                TempBean(
                    34,
                    (currentTimeMillis / 1000 + 6)
                )
            )
            add(
                TempBean(
                    35,
                    (currentTimeMillis / 1000 + 8)
                )
            )
            add(
                TempBean(
                    37,
                    (currentTimeMillis / 1000 + 10)
                )
            )
            add(
                TempBean(
                    39,
                    (currentTimeMillis / 1000 + 12)
                )
            )
            add(
                TempBean(
                    40,
                    (currentTimeMillis / 1000 + 14)
                )
            )
            add(
                TempBean(
                    42,
                    (currentTimeMillis / 1000 + 16)
                )
            )
            add(
                TempBean(
                    43,
                    (currentTimeMillis / 1000 + 18)
                )
            )
            add(
                TempBean(
                    45,
                    (currentTimeMillis / 1000 + 20)
                )
            )
            add(
                TempBean(
                    43,
                    (currentTimeMillis / 1000 + 22)
                )
            )
            add(
                TempBean(
                    44,
                    (currentTimeMillis / 1000 + 24)
                )
            )
            add(
                TempBean(
                    45,
                    (currentTimeMillis / 1000 + 26)
                )
            )
            add(
                TempBean(
                    46,
                    (currentTimeMillis / 1000 + 28)
                )
            )
            add(
                TempBean(
                    48,
                    (currentTimeMillis / 1000 + 30)
                )
            )
            add(
                TempBean(
                    50,
                    (currentTimeMillis / 1000 + 32)
                )
            )
            add(
                TempBean(
                    52,
                    (currentTimeMillis / 1000 + 34)
                )
            )
            add(
                TempBean(
                    50,
                    (currentTimeMillis / 1000 + 36)
                )
            )
            add(
                TempBean(
                    51,
                    (currentTimeMillis / 1000 + 38)
                )
            )
            add(
                TempBean(
                    53,
                    (currentTimeMillis / 1000 + 40)
                )
            )
            add(
                TempBean(
                    54,
                    (currentTimeMillis / 1000 + 42)
                )
            )
            add(
                TempBean(
                    55,
                    (currentTimeMillis / 1000 + 44)
                )
            )
            add(
                TempBean(
                    56,
                    (currentTimeMillis / 1000 + 46)
                )
            )
            add(
                TempBean(
                    58,
                    (currentTimeMillis / 1000 + 48)
                )
            )
            add(
                TempBean(
                    60,
                    (currentTimeMillis / 1000 + 50)
                )
            )
            add(
                TempBean(
                    58,
                    (currentTimeMillis / 1000 + 52)
                )
            )
        })
        cvAreaSpline.setSecondTempList(ArrayList<TempBean>().apply {
            add(TempBean(50, (currentTimeMillis / 1000)))
            add(
                TempBean(
                    51,
                    (currentTimeMillis / 1000 + 2)
                )
            )
            add(
                TempBean(
                    52,
                    (currentTimeMillis / 1000 + 4)
                )
            )
            add(
                TempBean(
                    54,
                    (currentTimeMillis / 1000 + 6)
                )
            )
            add(
                TempBean(
                    55,
                    (currentTimeMillis / 1000 + 8)
                )
            )
            add(
                TempBean(
                    57,
                    (currentTimeMillis / 1000 + 10)
                )
            )
            add(
                TempBean(
                    59,
                    (currentTimeMillis / 1000 + 12)
                )
            )
            add(
                TempBean(
                    60,
                    (currentTimeMillis / 1000 + 14)
                )
            )
            add(
                TempBean(
                    62,
                    (currentTimeMillis / 1000 + 16)
                )
            )
            add(
                TempBean(
                    63,
                    (currentTimeMillis / 1000 + 18)
                )
            )
            add(
                TempBean(
                    65,
                    (currentTimeMillis / 1000 + 20)
                )
            )
            add(
                TempBean(
                    63,
                    (currentTimeMillis / 1000 + 22)
                )
            )
            add(
                TempBean(
                    64,
                    (currentTimeMillis / 1000 + 24)
                )
            )
            add(
                TempBean(
                    65,
                    (currentTimeMillis / 1000 + 26)
                )
            )
            add(
                TempBean(
                    66,
                    (currentTimeMillis / 1000 + 28)
                )
            )
            add(
                TempBean(
                    68,
                    (currentTimeMillis / 1000 + 30)
                )
            )
            add(
                TempBean(
                    70,
                    (currentTimeMillis / 1000 + 32)
                )
            )
            add(
                TempBean(
                    72,
                    (currentTimeMillis / 1000 + 34)
                )
            )
            add(
                TempBean(
                    70,
                    (currentTimeMillis / 1000 + 36)
                )
            )
            add(
                TempBean(
                    71,
                    (currentTimeMillis / 1000 + 38)
                )
            )
            add(
                TempBean(
                    73,
                    (currentTimeMillis / 1000 + 40)
                )
            )
            add(
                TempBean(
                    74,
                    (currentTimeMillis / 1000 + 42)
                )
            )
            add(
                TempBean(
                    75,
                    (currentTimeMillis / 1000 + 44)
                )
            )
            add(
                TempBean(
                    76,
                    (currentTimeMillis / 1000 + 46)
                )
            )
            add(
                TempBean(
                    78,
                    (currentTimeMillis / 1000 + 48)
                )
            )
            add(
                TempBean(
                    80,
                    (currentTimeMillis / 1000 + 50)
                )
            )
            add(
                TempBean(
                    78,
                    (currentTimeMillis / 1000 + 52)
                )
            )
        })
        flTime.showLabelWithTimeNow(60)
    }
}
