package native_code_generation

import parsing.nodes.AASTNode
import parsing.nodes.base.DBlockNode
import parsing.nodes.commands.DMoveCommandNode
import native_code_generation.helpers.AArchitecture
import packing.APacker
import parsing.nodes.base.DPermanentDataNode
import utils.errors.DCompileError

class Generator (
    private val _architecture: AArchitecture,
    private val _input: List<AASTNode>,
    private val _packer: APacker
) {
    val blocks: MutableMap<String, Pair<Number, Number>> = mutableMapOf()
    private var _output: MutableList<UInt> = mutableListOf()
    private var _current_address: Number = _packer.ENTRY

    fun compile(): List<UInt> {
        for (construct in _input.withIndex()) {
            when(construct.value) {
                is DBlockNode -> {
                    val block = construct.value as DBlockNode
                    val block_address = _current_address

                    _output += compileCommands(block.children).let {
                        saveBlock(block, it.size, block_address)
                        it
                    }
                }

                is DPermanentDataNode -> {}

                else -> throw DCompileError("Unable to compile construction: ${construct.index + 1}, ${construct.value}")
            }
        }

        return _output
    }

    private fun compileCommands(target: List<AASTNode>): List<UInt> {
        val op_codes = mutableListOf<UInt>()

        for (cmd in target){
            compileCommand(cmd).let {
                _current_address = _current_address.toLong() + it.size
                op_codes += it
            }
        }

        return op_codes
    }

    private fun compileCommand(target: AASTNode): List<UInt> {
        return when(target) {
            is DMoveCommandNode -> _architecture.processMoveCommand(target)

            else -> {
                val op_code = _architecture.SIMPLE_COMMANDS[target::class.java]
                op_code ?: throw DCompileError("Unknown command: $target")
            }
        }
    }

    private fun saveBlock(target: DBlockNode, size: Number, address: Number){
        blocks += target.name to (address to size)
    }
}