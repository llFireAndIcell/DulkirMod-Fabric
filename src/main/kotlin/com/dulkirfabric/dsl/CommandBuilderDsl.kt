package com.dulkirfabric.dsl

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder

inline fun <S> literalArgument(
    name: String,
    block: LiteralArgumentBuilder<S>.() -> Unit
): LiteralArgumentBuilder<S> {
    val builder = LiteralArgumentBuilder.literal<S>(name)
    builder.block()
    return builder
}

inline fun <S, T> requiredArgument(
    name: String,
    type: ArgumentType<T>,
    block: RequiredArgumentBuilder<S, T>.() -> Unit
): RequiredArgumentBuilder<S, T> {
    val builder = RequiredArgumentBuilder.argument<S, T>(name, type)
    builder.block()
    return builder
}