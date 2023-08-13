package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

class DslIntSlider(
    name: Text
) : IntSliderBuilder(resetButtonKey, name, 0, Int.MIN_VALUE, Int.MAX_VALUE) {

    var default: Int = 0
    var property: KMutableProperty0<Int>? = null
    var tooltip: Text? = null

    /**
     * Note: Setting this will override [min] and [max]. If you have set [range] and would like to use [min] and [max],
     * you can set [range] to `null` again
     */
    var range: IntRange? = null

    /**
     * Note: This value will be ignored if [range] `!= null` on build
     */
    var min: Int? = null

    /**
     * Note: This value will be ignored if [range] `!= null` on build
     */
    var max: Int? = null

    override fun build(): IntegerSliderEntry {
        setDefaultValue(default)

        val propertyCached = property
        if (propertyCached != null) {
            value = propertyCached.get()
            setSaveConsumer { propertyCached.set(it) }
        }
        if (tooltip != null) setTooltip(tooltip)

        val rangeCached = range
        if (rangeCached != null) {
            setMin(rangeCached.first)
            setMax(rangeCached.last)
        } else {
            if (min != null) setMin(min)
            if (max != null) setMax(max)
        }
        return super.build()
    }
}

inline fun ConfigCategory.intSlider(
    name: Text,
    block: DslIntSlider.() -> Unit
): IntegerSliderEntry = DslIntSlider(name).run {
    block()
    build()
}.also { this.addEntry(it) }

fun ConfigCategory.intSlider(
    name: String,
    block: DslIntSlider.() -> Unit
) = intSlider(name.literal(), block)