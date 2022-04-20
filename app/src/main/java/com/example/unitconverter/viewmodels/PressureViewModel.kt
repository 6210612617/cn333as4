package com.example.unitconverter.viewmodels

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unitconverter.R

class PressureViewModel : ViewModel() {
    private val _unit: MutableLiveData<Int> = MutableLiveData(R.string.bar)

    val unit: LiveData<Int>
        get() = _unit

    fun setUnit(value: Int) {
        _unit.value = value
    }

    private val _pressure: MutableLiveData<String> = MutableLiveData("")

    val pressure: LiveData<String>
        get() = _pressure

    fun getPressureAsFloat(): Float = (_pressure.value ?: "").let {
        return try {
            it.toFloat()
        } catch (e: NumberFormatException) {
            Float.NaN
        }
    }

    fun setPressure(value: String) {
        _pressure.value = value
    }

    fun convert() = getPressureAsFloat().let {
        if (!it.isNaN())
            if (_unit.value == R.string.bar)
                it * 14.5037738F
            else
                it / 14.5037738F
        else
            Float.NaN
    }
}