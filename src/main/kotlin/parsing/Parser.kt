package parsing

import lexicalization.DToken
import lexicalization.ETokenType
import parsing.nodes.AASTNode
import parsing.nodes.base.DBlockNode
import parsing.nodes.commands.DMoveCommandNode
import parsing.nodes.commands.DSystemCallCommandNode
import parsing.nodes.expressions.DIdNode
import parsing.nodes.expressions.DIntegerNode
import parsing.nodes.expressions.DStringNode
import parsing.nodes.regs_and_vars.DGeneralRegNode
import parsing.nodes.regs_and_vars.system_calls.DSystemCallArgVarNode
import parsing.nodes.regs_and_vars.system_calls.DSystemCallIdVarNode
import utils.errors.DParserError

class Parser(private val input: List<List<DToken>>) {
    private val parsed: MutableList<AASTNode> = mutableListOf()
    private var current_line_index = 0

    fun parse(): MutableList<AASTNode> {
        for(line in input.withIndex()){
            if(line.index < current_line_index) continue

            if (line.value[0].type == ETokenType.KW_BLOCK &&
                line.value[1].type == ETokenType.IDENTIFIER &&
                line.value[2].type == ETokenType.START_CODE_SPACE){
                parsed += parseBlock(current_line_index)
            }
            else if (line.value[0].type == ETokenType.KW_PERMANENT)
                current_line_index++
            else
                throw DParserError("Unknown top-layer construction on line ${line.index + 1}")
        }

        return parsed
    }

    private fun parseBlock(start_index: Int): DBlockNode {
        var name: String = ""
        val children: MutableList<AASTNode> = mutableListOf()

        for(line in input.withIndex()){
            if(line.index < current_line_index) continue

            if(line.index == start_index) {
                name = line.value[1].value!!
            }
            else if(line.value[0].type == ETokenType.END_CODE_SPACE) {
                current_line_index++
                break
            }
            else {
                current_line_index++

                children += parseCommand()
            }
        }

        if( name.isBlank() || children.isEmpty() ) throw DParserError("Empty name or no commands in block at ${start_index + 1}th line")

        current_line_index++

        return DBlockNode(name, children)
    }

    private fun parseCommand(): AASTNode {
        return when(input[current_line_index][0].type){
            ETokenType.CMD_SYSTEM_CALL -> DSystemCallCommandNode()
            ETokenType.CMD_MOVE -> parseMoveCommand()

            else -> throw DParserError("Unknown command at $current_line_index")
        }
    }

    private fun parseMoveCommand(): DMoveCommandNode{
        val arg1_tkn = mutableListOf<DToken>()
        val arg2_tkn = mutableListOf<DToken>()

        var first_flag = false
        var second_flag = false

        for(token in input[current_line_index]) {
            if(token.type == ETokenType.CMD_AND_ARGS_DIVIDER) { first_flag = true; continue }
            if (first_flag){
                if(token.type == ETokenType.ARGS_DIVIDER){
                    first_flag = false
                    second_flag = true
                    continue
                }
                else {
                    arg1_tkn += token
                }
            }
            if(second_flag) {
                arg2_tkn += token
            }
        }


        return DMoveCommandNode(
            receiver = parseRegOrVar(arg1_tkn),
            value = parseExpression(arg2_tkn)
        )
    }

    private fun parseExpression(from: MutableList<DToken>) = when {
        from.size == 1 -> when (from[0].type) {
            ETokenType.NUMBER -> DIntegerNode(from[0].value!!.toInt())
            ETokenType.STRING -> DStringNode(from[0].value!!)
            ETokenType.IDENTIFIER -> DIdNode(from[0].value!!)

            else -> throw DParserError("Invalid expression at $from")
        }

        else -> throw DParserError("Invalid expression at $from")
    }


    private fun parseRegOrVar(from: MutableList<DToken>): AASTNode = when (from[0].type) {
        ETokenType.REG_PREFIX ->
            when {
                from[1].value!!.startsWith("general_reg") -> DGeneralRegNode(from[1].value!!)

                else -> throw DParserError("Undefined reg or var: $from")
            }
        ETokenType.VAR_PREFIX ->
            when {
                from[1].value!! == "system_call_id" -> DSystemCallIdVarNode()
                from[1].value!!.startsWith("system_call_arg") -> DSystemCallArgVarNode(from[1].value!!)

                else -> throw DParserError("Undefined reg or var: $from")
            }

        else -> throw DParserError("Undefined reg or var: $from")
    }


}