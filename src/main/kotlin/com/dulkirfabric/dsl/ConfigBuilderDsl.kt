package com.dulkirfabric.dsl

import com.dulkirfabric.config.DulkirConfig
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.*
import me.shedaniel.clothconfig2.impl.builders.ColorFieldBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import java.util.*
import kotlin.reflect.KMutableProperty0

/**
 * Creates a [ConfigBuilder] and passes it into [init] as `this`
 * @return The [Screen] that is returned when [ConfigBuilder.build] is called
 */
fun configBuilder(init: ConfigBuilder.() -> Unit): Screen = ConfigBuilder.create().apply(init).build()

/**
 * Creates a [ConfigCategory] and passes it into [init] as `this`
 * @return The newly created [ConfigCategory]
 */
fun ConfigBuilder.category(
    name: MutableText,
    init: ConfigCategory.() -> Unit
): ConfigCategory = getOrCreateCategory(name).apply { init() }

/**
 * Creates a [ConfigCategory] and passes it into [init] as `this`
 * @return The newly created [ConfigCategory]
 */
fun ConfigBuilder.category(
    name: String,
    init: ConfigCategory.() -> Unit
): ConfigCategory = category(name.literal(), init)

fun ConfigCategory.addEntry(init: ConfigEntryBuilder.() -> AbstractConfigListEntry<*>): AbstractConfigListEntry<*> {
    val entry = EntryBuilderInstance.get().init()
    addEntry(entry)
    return entry
}

/**
 * Creates a [StringListEntry] in the receiving [ConfigCategory]
 * @return The new [StringListEntry]
 */
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

/**
 * Creates a [StringListEntry] in the receiving [ConfigCategory]
 * @return The new [StringListEntry]
 */
fun ConfigCategory.makeString(
    text: String,
    property: KMutableProperty0<String>
): StringListEntry = makeString(text.literal(), property)

/**
 * Creates an [IntegerListEntry] in the receiving [ConfigCategory]
 * @return The new [IntegerListEntry]
 */
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

/**
 * Creates an [IntegerListEntry] in the receiving [ConfigCategory]
 * @return The new [IntegerListEntry]
 */
fun ConfigCategory.makeInt(
    text: String,
    property: KMutableProperty0<Int>
): IntegerListEntry = makeInt(text.literal(), property)

/**
 * Creates a [FloatListEntry] in the receiving [ConfigCategory]
 * @return The new [FloatListEntry]
 */
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

/**
 * Creates a [FloatListEntry] in the receiving [ConfigCategory]
 * @return The new [FloatListEntry]
 */
fun ConfigCategory.makeFloat(
    text: String,
    property: KMutableProperty0<Float>,
    tooltip: String? = null
): FloatListEntry = makeFloat(text.literal(), property, tooltip.literal())

/**
 * Creates a [BooleanListEntry] in the receiving [ConfigCategory]
 * @return The new [BooleanListEntry]
 */
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

/**
 * Creates a [BooleanListEntry] in the receiving [ConfigCategory]
 * @return The new [BooleanListEntry]
 */
fun ConfigCategory.makeToggle(
    text: String,
    property: KMutableProperty0<Boolean>,
    tooltip: String? = null
): BooleanListEntry = makeToggle(text.literal(), property, tooltip.literal())

/**
 * Creates a [KeyCodeEntry] in the receiving [ConfigCategory]
 * @return The new [KeyCodeEntry]
 */
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

/**
 * Creates a [KeyCodeEntry] in the receiving [ConfigCategory]
 * @return The new [KeyCodeEntry]
 */
fun ConfigCategory.makeKeybind(
    text: String,
    property: KMutableProperty0<InputUtil.Key>
): KeyCodeEntry = makeKeybind(text.literal(), property)

/**
 * Creates an [IntegerSliderEntry] in the receiving [ConfigCategory]
 * @return The new [IntegerSliderEntry]
 */
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

/**
 * Creates an [IntegerSliderEntry] in the receiving [ConfigCategory]
 * @return The new [IntegerSliderEntry]
 */
fun ConfigCategory.makeIntSlider(
    text: String,
    property: KMutableProperty0<Int>,
    min: Int,
    max: Int,
    tooltip: String? = null
): IntegerSliderEntry = makeIntSlider(text.literal(), property, min, max, tooltip.literal())

/**
 * Creates a [ColorEntry] in the receiving [ConfigCategory]
 * @return The new [ColorEntry]
 */
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

/**
 * Creates a [ColorEntry] in the receiving [ConfigCategory]
 * @return The new [ColorEntry]
 */
fun ConfigCategory.makeColor(
    text: String,
    property: KMutableProperty0<Int>,
    options: (ColorFieldBuilder.() -> ColorFieldBuilder)? = null
): ColorEntry = makeColor(text.literal(), property, options)

/**
 * Creates a [NestedListListEntry] in the receiving [ConfigCategory]
 * @return The new [NestedListListEntry]
 */
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

/**
 * Creates a [NestedListListEntry] in the receiving [ConfigCategory]
 * @return The new [NestedListListEntry]
 */
fun <T> ConfigCategory.makeConfigList(
    name: String,
    property: KMutableProperty0<List<T>>,
    newT: () -> T,
    elementName: String,
    render: (T) -> List<AbstractConfigListEntry<*>>,
    canDelete: Boolean = true,
): NestedListListEntry<T, MultiElementListEntry<T>> = makeConfigList(
    name.literal(),
    property,
    newT,
    elementName.literal(),
    render,
    canDelete
)