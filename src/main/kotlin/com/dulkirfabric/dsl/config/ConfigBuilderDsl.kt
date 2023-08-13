@file:Suppress("unused")

package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.*
import me.shedaniel.clothconfig2.gui.entries.*
import me.shedaniel.clothconfig2.impl.builders.*
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

// this is necessary for some weird reason idk what it does but the value is constant
val resetButtonKey: Text = Text.translatable("text.cloth-config.reset_value")

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
) = category(name.literal(), init)

fun makeString(
    text: Text,
    property: KMutableProperty0<String>
) = StringFieldBuilder(resetButtonKey, text, property.get()).run {
    setSaveConsumer { property.set(it) }
    setDefaultValue("")
    build()
}

fun makeString(
    text: String,
    property: KMutableProperty0<String>
) = makeString(text.literal(), property)

/**
 * Creates an [IntegerListEntry] in the receiving [ConfigCategory]
 * @return The new [IntegerListEntry]
 */
fun ConfigCategory.makeInt(
    text: Text,
    property: KMutableProperty0<Int>
) = IntFieldBuilder(resetButtonKey, text, property.get()).run {
    setSaveConsumer { property.set(it) }
    setDefaultValue(0)
    build()
}.also { addEntry(it) }

/**
 * Creates an [IntegerListEntry] in the receiving [ConfigCategory]
 * @return The new [IntegerListEntry]
 */
fun ConfigCategory.makeInt(
    text: String,
    property: KMutableProperty0<Int>
) = makeInt(text.literal(), property)

/**
 * Creates a [FloatListEntry] in the receiving [ConfigCategory]
 * @return The new [FloatListEntry]
 */
fun ConfigCategory.makeFloat(
    text: Text,
    property: KMutableProperty0<Float>,
    tooltip: Text = Text.empty()
) = FloatFieldBuilder(resetButtonKey, text, property.get()).run {
    setTooltip(tooltip)
    setSaveConsumer { property.set(it) }
    build()
}.also { addEntry(it) }

/**
 * Creates a [FloatListEntry] in the receiving [ConfigCategory]
 * @return The new [FloatListEntry]
 */
fun ConfigCategory.makeFloat(
    text: String,
    property: KMutableProperty0<Float>,
    tooltip: String? = null
) = makeFloat(text.literal(), property, tooltip.literal())

/**
 * Creates a [BooleanListEntry] in the receiving [ConfigCategory]
 * @return The new [BooleanListEntry]
 */
fun ConfigCategory.makeToggle(
    text: Text,
    property: KMutableProperty0<Boolean>,
    tooltip: Text = Text.empty()
) = BooleanToggleBuilder(resetButtonKey, text, property.get()).run {
    setSaveConsumer { property.set(it) }
    setDefaultValue(false)
    setTooltip(tooltip)
    build()
}.also { addEntry(it) }

/**
 * Creates a [BooleanListEntry] in the receiving [ConfigCategory]
 * @return The new [BooleanListEntry]
 */
fun ConfigCategory.makeToggle(
    text: String,
    property: KMutableProperty0<Boolean>,
    tooltip: String? = null
) = makeToggle(text.literal(), property, tooltip.literal())

/**
 * Creates a [KeyCodeEntry] in the receiving [ConfigCategory]. This does not support modifier keys
 * @return The new [KeyCodeEntry]
 */
fun makeKeybind(
    text: Text,
    property: KMutableProperty0<InputUtil.Key>
) = KeyCodeBuilder(resetButtonKey, text, ModifierKeyCode.of(property.get(), Modifier.none())).run {
    setKeySaveConsumer { property.set(it) }
    setDefaultValue(InputUtil.UNKNOWN_KEY)
    build()
}

/**
 * Creates a [KeyCodeEntry] in the receiving [ConfigCategory]
 * @return The new [KeyCodeEntry]
 */
fun makeKeybind(
    text: String,
    property: KMutableProperty0<InputUtil.Key>
) = makeKeybind(text.literal(), property)

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
) = IntSliderBuilder(resetButtonKey, text, property.get(), min, max).run {
    setSaveConsumer { property.set(it) }
    setTooltip(tooltip)
    build()
}.also { addEntry(it) }

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
) = makeIntSlider(text.literal(), property, min, max, tooltip.literal())

/**
 * Creates a [ColorEntry] in the receiving [ConfigCategory]
 * @return The new [ColorEntry]
 */
fun ConfigCategory.makeColor(
    text: Text,
    property: KMutableProperty0<Int>,
    options: (ColorFieldBuilder.() -> ColorFieldBuilder)? = null
) = ColorFieldBuilder(resetButtonKey, text, property.get()).run {
    setSaveConsumer { property.set(it) }
    if (options != null) options()
    build()
}.also { addEntry(it) }

/**
 * Creates a [ColorEntry] in the receiving [ConfigCategory]
 * @return The new [ColorEntry]
 */
fun ConfigCategory.makeColor(
    text: String,
    property: KMutableProperty0<Int>,
    options: (ColorFieldBuilder.() -> ColorFieldBuilder)? = null
) = makeColor(text.literal(), property, options)

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
) = NestedListListEntry<T, MultiElementListEntry<T>>(
    name, // field name
    property.get(), // value
    false, // defaultExpanded
    { Optional.empty() }, // tooltipSupplier
    { property.set(it) }, // saveConsumer
    { mutableListOf() }, // defaultValue
    Text.literal("Reset"), // resetButtonKey
    canDelete,
    false,
    { value, _ -> // createNewCell
        val realValue = value ?: newT()
        MultiElementListEntry(elementName, realValue, render(realValue), true)
    }
).also { addEntry(it) }

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
) = makeConfigList(
    name.literal(),
    property,
    newT,
    elementName.literal(),
    render,
    canDelete
)