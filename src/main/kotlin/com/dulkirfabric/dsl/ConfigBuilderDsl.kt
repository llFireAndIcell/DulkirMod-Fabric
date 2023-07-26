package com.dulkirfabric.dsl

import com.dulkirfabric.config.ConfigHelper
import com.dulkirfabric.config.DulkirConfig
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import me.shedaniel.clothconfig2.gui.entries.ColorEntry
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry
import me.shedaniel.clothconfig2.gui.entries.KeyCodeEntry
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
import me.shedaniel.clothconfig2.gui.entries.StringListEntry
import me.shedaniel.clothconfig2.impl.builders.ColorFieldBuilder
import net.minecraft.client.util.InputUtil
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import java.util.*
import kotlin.reflect.KMutableProperty0

fun configBuilder(init: ConfigBuilder.() -> Unit): ConfigBuilder = ConfigBuilder.create().apply(init)

fun ConfigBuilder.category(name: MutableText, init: ConfigCategory.() -> Unit): ConfigCategory =
    getOrCreateCategory(name).apply { init() }

fun ConfigCategory.addEntry(init: ConfigEntryBuilder.() -> AbstractConfigListEntry<*>): AbstractConfigListEntry<*> {
    val entry = ConfigHelper.entryBuilder.init()
    addEntry(entry)
    return entry
}

fun ConfigCategory.makeString(
    text: Text,
    property: KMutableProperty0<String>
): StringListEntry {
    return addEntry {
        startStrField(text, property.get()).run {
            setSaveConsumer { property.set(it) }
            setDefaultValue("")
            build()
        }
    } as StringListEntry
}

fun ConfigCategory.makeInt(
    text: Text,
    property: KMutableProperty0<Int>
): IntegerListEntry {
    return addEntry {
        startIntField(text, property.get()).run {
            setSaveConsumer { property.set(it) }
            setDefaultValue(0)
            build()
        }
    } as IntegerListEntry
}

fun ConfigCategory.makeFloat(
    text: Text,
    property: KMutableProperty0<Float>,
    tooltip: Text = Text.empty()
): FloatListEntry {
    return addEntry {
        startFloatField(text, DulkirConfig.configOptions.inventoryScale).run {
            setTooltip(tooltip)
            setSaveConsumer { property.set(it) }
            build()
        }
    } as FloatListEntry
}

fun ConfigCategory.makeToggle(
    text: Text,
    property: KMutableProperty0<Boolean>,
    tooltip: Text = Text.empty()
): BooleanListEntry {
    return addEntry {
        startBooleanToggle(text, property.get()).run {
            setSaveConsumer { property.set(it) }
            setDefaultValue(false)
            setTooltip(tooltip)
            build()
        }
    } as BooleanListEntry
}

fun ConfigCategory.makeKeybind(
    text: Text,
    property: KMutableProperty0<InputUtil.Key>
): KeyCodeEntry {
    return addEntry {
        startKeyCodeField(text, property.get()).run {
            setKeySaveConsumer { property.set(it) }
            setDefaultValue(InputUtil.UNKNOWN_KEY)
            build()
        }
    } as KeyCodeEntry
}

fun ConfigCategory.makeIntSlider(
    text: Text,
    property: KMutableProperty0<Int>,
    min: Int,
    max: Int,
    tooltip: Text = Text.empty()
): IntegerSliderEntry {
    return addEntry {
        startIntSlider(text, property.get(), min, max).run {
            setSaveConsumer { property.set(it) }
            setTooltip(tooltip)
            build()
        }
    } as IntegerSliderEntry
}

fun ConfigCategory.makeColor(
    text: Text,
    property: KMutableProperty0<Int>,
    options: (ColorFieldBuilder.() -> ColorFieldBuilder)? = null
): ColorEntry {
    return addEntry {
        startColorField(text, TextColor.fromRgb(property.get())).run {
            setSaveConsumer { property.set(it) }
            if (options != null) options()
            build()
        }
    } as ColorEntry
}

@Suppress("UnstableApiUsage")
fun <T> ConfigCategory.makeConfigList(
    name: Text,
    property: KMutableProperty0<List<T>>,
    newT: () -> T,
    elementName: Text,
    render: (T) -> List<AbstractConfigListEntry<*>>,
    canDelete: Boolean = true,
): NestedListListEntry<T, MultiElementListEntry<T>> {
    val entry = NestedListListEntry<T, MultiElementListEntry<T>>(
        name, // field name
        property.get(), // value
        false, // defaultExpanded
        { Optional.empty() }, // tooltipSupplier
        { property.set(it) }, // saveConsumer
        { mutableListOf() }, // defaultValue
        Text.literal("Reset"), // resetButtonKey
        canDelete,
        false,
        { value, entry -> // createNewCell
            val value = value ?: newT()
            MultiElementListEntry(elementName, value, render(value), true)
        }
    )
    addEntry { entry }
    return entry
}