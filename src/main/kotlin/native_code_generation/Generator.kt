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
    val _blocks: MutableMap<String, Number> = mutableMapOf()
    private var _output: MutableList<UInt> = mutableListOf()
    private var _current_address: Number = _packer.ENTRY

    fun compile(): List<UInt> {
        for (construct in _input.withIndex()) {
            when(construct.value) {
                is DBlockNode -> {
                    val block = construct.value as DBlockNode
                    saveBlock(block)

                    _output += compileCommands(block.children)
                }

                is DPermanentDataNode -> {}

                else -> throw DCompileError("Unable to compile construction: ${construct.index + 1}, ${construct.value}")
            }
        }

        println(_blocks)

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

    private fun saveBlock(target: DBlockNode){
        _blocks += target.name to _current_address
    }
}