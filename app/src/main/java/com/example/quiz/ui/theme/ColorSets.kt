package com.example.quiz.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Helper object that provides color sets
 */
data object ColorSets {

    val red = ColorSet(
        main = Red,
        light = RedLight,
        dark = RedDark
    )

    val orange = ColorSet(
        main = Orange,
        light = OrangeLight,
        dark = OrangeDark
    )

    val yellow = ColorSet(
        main = Yellow,
        light = YellowLight,
        dark = YellowDark
    )

    val lime = ColorSet(
        main = Lime,
        light = LimeLight,
        dark = LimeDark
    )

    val indigo = ColorSet(
        main = Indigo,
        light = IndigoLight,
        dark = IndigoDark
    )

    val purple = ColorSet(
        main = Purple,
        light = PurpleLight,
        dark = PurpleDark
    )

    val fuchsia = ColorSet(
        main = Fuchsia,
        light = FuchsiaLight,
        dark = FuchsiaDark
    )

    val pink = ColorSet(
        main = Pink,
        light = PinkLight,
        dark = PinkDark
    )

    val rose = ColorSet(
        main = Rose,
        light = RoseLight,
        dark = RoseDark
    )

    val all = listOf(
        red,
        orange,
        yellow,
        lime,
        indigo,
        purple,
        fuchsia,
        pink,
        rose
    )

    val random: ColorSet
        get() = all.random()
}

@Immutable
data class ColorSet(
    val main: Color,
    val light: Color,
    val dark: Color
)