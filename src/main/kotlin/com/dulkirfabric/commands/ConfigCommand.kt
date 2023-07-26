package com.dulkirfabric.commands

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.dsl.literalArgument
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object ConfigCommand {

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(
            literalArgument("dulkir") {
                executes {
                    mc.send { mc.setScreen(DulkirConfig().screen) }
                    return@executes 0
                }
            })
    }
}