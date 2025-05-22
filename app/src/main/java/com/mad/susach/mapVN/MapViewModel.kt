package com.mad.susach.mapVN

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mad.susach.R

class MapViewModel : ViewModel() {
    val maps = listOf(
        R.drawable._1009,
        R.drawable._1014,
        R.drawable._1039,
        R.drawable._1069,
        R.drawable._1084,
        R.drawable._1306,
        R.drawable._1407,
        R.drawable._1416,
        R.drawable._1428,
        R.drawable._1434,
        R.drawable._1479,
        R.drawable._1540,
        R.drawable._1608,
        R.drawable._1611,
        R.drawable._1653,
        R.drawable._1732,
        R.drawable._1739,
        R.drawable._1757,
        R.drawable._1771,
        R.drawable._1789,
        R.drawable._1802,
        R.drawable._1834,
        R.drawable._1840,
        R.drawable._1867,
        R.drawable._1887,
        R.drawable._1895,
        R.drawable._1945,
        R.drawable._1990
    )

    var currentIndex by mutableStateOf(0)
        private set

    fun nextMap() {
        if (currentIndex < maps.size - 1) currentIndex++
    }

    fun prevMap() {
        if (currentIndex > 0) currentIndex--
    }
}
