@file:Suppress("NOTHING_TO_INLINE")

package com.dulkirfabric.dsl

import net.minecraft.text.MutableText
import net.minecraft.text.Text

// inlining these bc they're so simple, idc if the performance benefit is minimal
inline fun String.literal(): MutableText = Text.literal(this)
inline fun String?.literal(): MutableText = this?.literal() ?: Text.empty()
