package com.dulkirfabric.commands

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

/**
 * An interface to help with creating command implementations, that provides the following benefits:
 * - ensures your command implements the [register] with the right signature
 * - avoids a compiler warning if you don't use the registryAccess parameter
 * - allows you to implement a command with just a lambda, using [this kotlin feature](https://kotlinlang.org/docs/fun-interfaces.html)
 */
fun interface ICommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess)
}