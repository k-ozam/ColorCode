package com.solit.colorcode


class Color() {
    var r = "00"
    var g = "00"
    var b = "00"
    var colorCode: String = "#000000"
    var color: Int = android.graphics.Color.parseColor(colorCode)
    var oppositeColorCode: String = "#000000"
    var complementaryColorCode: String = "#000000"
    var oppositeColor = android.graphics.Color.parseColor(oppositeColorCode)
    var complementaryColor = android.graphics.Color.parseColor(complementaryColorCode)

    fun inputTextColor(): String {
        return colorCode.replacement(0..0, "")
    }

    fun convert(c: Int, model: Model): String {
        return when (model) {
            Model.R -> {
                "0" + Integer.toHexString(c) + g + b
            }
            Model.G -> {
                r + "0" + Integer.toHexString(c) + b
            }
            Model.B -> {
                r + g + "0" + Integer.toHexString(c)
            }
        }
    }

    fun shed(c: Int, model: Model): String {
        return when (model) {
            Model.R -> {
                Integer.toHexString(c) + g + b
            }
            Model.G -> {
                r + Integer.toHexString(c) + b
            }
            Model.B -> {
                r + g + Integer.toHexString(c)
            }
        }
    }

    fun updateColor(new: String, position: String? = null) {
        r = new.substring(0..1).toUpperCase()
        g = new.substring(2..3).toUpperCase()
        b = new.substring(4..5).toUpperCase()
        colorCode = "#${r + g + b}"
        color = android.graphics.Color.parseColor(colorCode)
        complementaryColor()
        oppositeColor()
    }

    fun complementaryColor() {
//        val newR = Integer.toHexString(255 - Integer.parseInt(r, 16))
//        val newG = Integer.toHexString(255 - Integer.parseInt(g, 16))
//        val newB = Integer.toHexString(255 - Integer.parseInt(b, 16))
        val newR = 255 - Integer.parseInt(r, 16)
        val newG = 255 - Integer.parseInt(g, 16)
        val newB = 255 - Integer.parseInt(b, 16)
        complementaryColor = android.graphics.Color.parseColor(complementaryColorCode)

        oppositeColorCode = newString(newR, newG, newB)
    }


    fun oppositeColor() {
        val r = Integer.parseInt(this.r, 16)
        val g = Integer.parseInt(this.g, 16)
        val b = Integer.parseInt(this.b, 16)
        var max = Math.max(r, Math.max(g, b))
        var min = Math.min(r, Math.min(g, b))
        val p = max + min
        val newR = p - r
        val newG = p - g
        val newB = p - b
        oppositeColor = android.graphics.Color.parseColor(oppositeColorCode)
        complementaryColorCode = newString(newR, newG, newB)
    }

    fun newString(newR: Int, newG: Int, newB: Int): String {
        var newRString = Integer.toHexString(newR)
        var newGString = Integer.toHexString(newG)
        var newBString = Integer.toHexString(newB)
        if (newR < 16) {
            newRString = "0$newRString"
        }
        if (newG < 16) {
            newGString = "0$newGString"
        }
        if (newB < 16) {
            newBString = "0$newBString"
        }
        return "#${newRString + newGString + newBString}"
    }

    enum class Model {
        R, G, B;
    }
}

fun String.replacement(range: IntRange, new: String): String {
    return replace(substring(range), new)
}