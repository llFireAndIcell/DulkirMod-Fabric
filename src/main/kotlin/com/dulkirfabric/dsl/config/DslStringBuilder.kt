package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.gui.entries.StringListEntry
import me.shedaniel.clothconfig2.impl.builders.StringFieldBuilder
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

class DslStringBuilder(name: Text) : StringFieldBuilder(resetButtonKey, name, "") {

    var default: String = ""
    var property: KMutableProperty0<String>? = null
    var tooltip: Text? = null

    override fun build(): StringListEntry {
        setDefaultValue(default)

        val propertyCached = property
        if (propertyCached != null) {
            value = propertyCached.get()
            setSaveConsumer { propertyCached.set(it) }
        }
        if (tooltip != null) setTooltip(tooltip)
        return super.build()
    }
}

inline fun ConfigCategory.string(
    name: Text,
    block: DslStringBuilder.() -> Unit
): StringListEntry = DslStringBuilder(name).run {
    block()
    build()
}.also { this.addEntry(it) }

fun ConfigCategory.string(
    name: String,
    block: DslStringBuilder.() -> Unit
) = string(name.literal(), block)