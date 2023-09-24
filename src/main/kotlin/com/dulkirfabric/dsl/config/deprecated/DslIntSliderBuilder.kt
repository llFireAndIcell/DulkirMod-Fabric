package com.dulkirfabric.dsl.config.deprecated

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

    /**
     * The kotlin property that stores the config value. This is not required, but is used to streamline a common way
     * of storing config values
     */
    var property: KMutableProperty0<Int>? = null

    /**
     * A custom lambda that is run when the config value is changed. This is ONLY used if [property] is `null`.
     * Otherwise, the save consumer is set to be the setter for the property
     */
    var saveConsumer: ((Int) -> Unit)? = null
    var tooltip: Text? = null

    var range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE

    override fun build(): IntegerSliderEntry {
        setDefaultValue(default)

        if (property != null) {
            // non-null assertion calls are required by compiler, but property should not change after build function
            // is called anyway
            value = property!!.get()
            setSaveConsumer { property!!.set(it) }
        }
        else if (saveConsumer != null) setSaveConsumer { saveConsumer!!(it) }
        if (tooltip != null) setTooltip(tooltip)

        setMin(range.first)
        setMax(range.last)
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