package com.solit.colorcode

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(val pref: SharedPreferences) : ViewModel(), MainCallBack {
    override fun onClickTwo() {
        clipBoardLiveData.postValue(color.oppositeColorCode)

    }

    override fun onClickThree() {
        clipBoardLiveData.postValue(color.complementaryColorCode)

    }

    val clipBoardLiveData = MutableLiveData<String>()
    val updateColorLiveData = MutableLiveData<Unit>()
    var color = Color()

    init {
        updateColor(pref.getString("cc", "#000000").replacement(0..0, ""))
    }

    override fun onClick() {
        clipBoardLiveData.postValue(color.colorCode)
    }

    fun saveColor() {
        val editor = pref.edit()
        editor.putString("cc", color.colorCode)
        editor.apply()
    }

    fun updateColor(new: String) {
        try {
            color.updateColor(new)
            updateColorLiveData.postValue(Unit)

        } catch (e: Exception) {
            color.updateColor("ffffff")
        }
    }

    fun updateSeekBar(new: Int, model: Color.Model) {
        if (new < 16) {
            updateColor(color.convert(new, model))
        } else {
            updateColor(color.shed(new, model))
        }
    }
}