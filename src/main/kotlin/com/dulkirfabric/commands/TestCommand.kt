package com.dulkirfabric.commands

import com.dulkirfabric.dsl.literalArgument
import com.dulkirfabric.util.ScoreBoardUtils
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object TestCommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(literalArgument("linetest") {
            executes {
                val lines = ScoreBoardUtils.getLines()
                return@executes 0
            }
        })
    }
}