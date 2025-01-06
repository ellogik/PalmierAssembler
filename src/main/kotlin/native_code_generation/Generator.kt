package native_code_generation

import parsing.nodes.AASTNode
import parsing.nodes.base.DBlockNode
import parsing.nodes.commands.DMoveCommandNode
import plugins.AArchitecturePlugin
import plugins.APackerPlugin

class Generator(
    private val architecture: AArchitecturePlugin,
    packer: APackerPlugin,
    private val input: List<AASTNode>
) {
    private var output: MutableList<UInt> = mutableListOf()
    private var current_address: ULong = packer.code_start_point
    private var blocks: MutableMap<String, ULong> = mutableMapOf()

    fun compile(): List<UInt> {
        for (construct in input.withIndex()) {
            when(construct.value) {
                is DBlockNode -> {
                    val block = construct.value as DBlockNode
                    saveBlock(block)

                    output += compileCommands(block.children)
                }

                else -> throw DCompileError("Unable to compile construction: ${construct.index + 1}")
            }
        }

        return output
    }

    private fun compileCommands(target: List<AASTNode>): List<UInt> {
        val op_codes = mutableListOf<UInt>()

        for (cmd in target){
            op_codes += compileCommand(cmd)
        }

        return op_codes
    }

    private fun compileCommand(target: AASTNode): List<UInt> {
        return when(target) {
            is DMoveCommandNode -> architecture.processMoveCommand(target)

            else -> {
                val op_code = architecture.SIMPLE_COMMANDS[target::class.java]
                op_code ?: throw DCompileError("Unknown command: $target")
            }
        }
    }

    private fun saveBlock(target: DBlockNode){
        blocks += Pair(target.name, current_address)
    }
}