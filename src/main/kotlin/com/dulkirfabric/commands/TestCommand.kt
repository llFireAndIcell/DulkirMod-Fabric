package com.dulkirfabric.commands

import com.dulkirfabric.dsl.registerCommand
import com.dulkirfabric.util.ScoreBoardUtils
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object TestCommand : ICommand {

    override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.registerCommand("linetest") {
            executes {
                @Suppress("UNUSED_VARIABLE")
                val lines = ScoreBoardUtils.getLines()
                return@executes 0
            }
        }
    }
}