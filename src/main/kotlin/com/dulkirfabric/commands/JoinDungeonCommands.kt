package com.dulkirfabric.commands

import com.dulkirfabric.dsl.registerCommand
import com.dulkirfabric.util.TextUtils
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object JoinDungeonCommands : ICommand {

    private val normalCommands: Array<ICommand> = arrayOf(
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("entrance") {
                executes {
                    TextUtils.info("§6Attempting to join Entrance...")
                    TextUtils.sendCommand("joindungeon catacombs 0")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("f1") {
                executes {
                    TextUtils.info("§6Attempting to join F1...")
                    TextUtils.sendCommand("joindungeon catacombs 1")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("f2") {
                executes {
                    TextUtils.info("§6Attempting to join F2...")
                    TextUtils.sendCommand("joindungeon catacombs 2")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("f3") {
                executes {
                    TextUtils.info("§6Attempting to join F3...")
                    TextUtils.sendCommand("joindungeon catacombs 3")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("f4") {
                executes {
                    TextUtils.info("§6Attempting to join F4...")
                    TextUtils.sendCommand("joindungeon catacombs 4")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("f5") {
                executes {
                    TextUtils.info("§6Attempting to join F5...")
                    TextUtils.sendCommand("joindungeon catacombs 5")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("f6") {
                executes {
                    TextUtils.info("§6Attempting to join F6...")
                    TextUtils.sendCommand("joindungeon catacombs 6")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("f7") {
                executes {
                    TextUtils.info("§6Attempting to join F7...")
                    TextUtils.sendCommand("joindungeon catacombs 7")
                    return@executes 0
                }
            }
        }
    )
    private val masterCommands: Array<ICommand> = arrayOf(
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("m1") {
                executes {
                    TextUtils.info("§6Attempting to join M1...")
                    TextUtils.sendCommand("joindungeon master_catacombs 1")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("m2") {
                executes {
                    TextUtils.info("§6Attempting to join M2...")
                    TextUtils.sendCommand("joindungeon master_catacombs 2")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("m3") {
                executes {
                    TextUtils.info("§6Attempting to join M3...")
                    TextUtils.sendCommand("joindungeon master_catacombs 3")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("m4") {
                executes {
                    TextUtils.info("§6Attempting to join M4...")
                    TextUtils.sendCommand("joindungeon master_catacombs 4")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("m5") {
                executes {
                    TextUtils.info("§6Attempting to join M5...")
                    TextUtils.sendCommand("joindungeon master_catacombs 5")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("m6") {
                executes {
                    TextUtils.info("§6Attempting to join M6...")
                    TextUtils.sendCommand("joindungeon master_catacombs 6")
                    return@executes 0
                }
            }
        },
        ICommand { dispatcher, _ ->
            dispatcher.registerCommand("m7") {
                executes {
                    TextUtils.info("§6Attempting to join M7...")
                    TextUtils.sendCommand("joindungeon master_catacombs 7")
                    return@executes 0
                }
            }
        }
    )

    override fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        for (command in normalCommands) command.register(dispatcher, registryAccess)
        for (command in masterCommands) command.register(dispatcher, registryAccess)
    }
}
