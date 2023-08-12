package com.dulkirfabric.dsl

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode

/**
 * Since the first argument is always a literal, this function removes the redundant step of creating a
 * [LiteralArgumentBuilder] manually
 */
inline fun <S> CommandDispatcher<S>.registerCommand(name: String, block: LiteralArgumentBuilder<S>.() -> Unit): LiteralCommandNode<S> {
    return LiteralArgumentBuilder.literal<S>(name).let {
        it.block()
        register(it)
        it.build()
    }
}

inline fun <S> literalArgument(
    name: String,
    block: LiteralArgumentBuilder<S>.() -> Unit
): LiteralArgumentBuilder<S> {
    return LiteralArgumentBuilder.literal<S>(name).apply { block() }
}

inline fun <S, T> requiredArgument(
    name: String,
    type: ArgumentType<T>,
    block: RequiredArgumentBuilder<S, T>.() -> Unit
): RequiredArgumentBuilder<S, T> {
    return RequiredArgumentBuilder.argument<S, T>(name, type).apply { block() }
}