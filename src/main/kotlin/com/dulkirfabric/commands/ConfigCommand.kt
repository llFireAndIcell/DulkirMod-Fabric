package com.dulkirfabric.commands

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.dsl.registerCommand
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object ConfigCommand : ICommand {

    override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.registerCommand("dulkir") {
            executes {
                mc.send { mc.setScreen(DulkirConfig().screen) }
                return@executes 0
            }
        }
    }
}