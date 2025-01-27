package parsing

import lexicalization.DToken
import lexicalization.ETokenType
import parsing.nodes.AASTNode
import parsing.nodes.base.DBlockNode
import parsing.nodes.base.DPermanentDataNode
import parsing.nodes.commands.DMoveCommandNode
import parsing.nodes.commands.DSystemCallCommandNode
import parsing.nodes.expressions.DIdNode
import parsing.nodes.expressions.DIntegerNode
import parsing.nodes.expressions.DMinusNode
import parsing.nodes.expressions.DStringNode
import parsing.nodes.regs_and_vars.DCurrentAddressVarNode
import parsing.nodes.regs_and_vars.DGeneralRegNode
import parsing.nodes.regs_and_vars.system_calls.DSystemCallArgVarNode
import parsing.nodes.regs_and_vars.system_calls.DSystemCallIdVarNode
import utils.errors.DParserError
import java.lang.reflect.Type
import kotlin.reflect.KClass

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
                parsePermanent()
            else
                throw DParserError("Unknown top-layer construction on line ${line.index + 1}")
        }

        return parsed
    }

    private fun parseBlock(start_index: Int): DBlockNode {
        var name = ""
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

    private fun parsePermanent(): DPermanentDataNode {
        val line = input[current_line_index]

        if( line[0].type != ETokenType.KW_PERMANENT
            || line[1].type != ETokenType.IDENTIFIER
            || line[2].type != ETokenType.CMD_AND_ARGS_DIVIDER ) throw DParserError("Invalid permanent value at ${current_line_index} line")

        current_line_index++

        return DPermanentDataNode(line[1].value!!, parseExpression(line.slice(3..<line.size)))
    }

    private fun parseCommand(): AASTNode {
        return when(input[current_line_index][0].type){
            ETokenType.CMD_SYSTEM_CALL -> DSystemCallCommandNode()
            ETokenType.CMD_MOVE -> parseMoveCommand()

            else -> throw DParserError("Unknown command at $current_line_index, command: ${input[current_line_index]}")
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

    private fun parseSimpleExpression(from: DToken) = when (from.type) {
        ETokenType.NUMBER -> DIntegerNode(from.value!!.toInt())
        ETokenType.STRING -> DStringNode(from.value!!)
        ETokenType.IDENTIFIER -> DIdNode(from.value!!)

        else -> throw DParserError("Invalid expression at $from")
    }

    private fun parseExpression(from: List<DToken>) = when {
        from.size == 1 -> parseSimpleExpression(from[0])

        from[0].type == ETokenType.KW_TERM -> {
            lateinit var part_first: AASTNode
            lateinit var part_second: AASTNode
            lateinit var symbol: ETokenType
            var first_or_second = false


            for ( token in from.slice(1..<from.size).withIndex() ) {
                when(token.value.type) {
                    ETokenType.VAR_PREFIX, ETokenType.REG_PREFIX ->
                        if( first_or_second )
                            part_first = parseRegOrVar(from.slice(token.index + 1..token.index + 2))
                        else
                            part_second = parseRegOrVar(from.slice(token.index + 1..token.index + 2))

                    ETokenType.MINUS, ETokenType.PLUS, ETokenType.MULTIPLY, ETokenType.DIVIDE -> {
                        symbol = token.value.type
                        first_or_second = !first_or_second
                    }

                    ETokenType.NUMBER, ETokenType.STRING, ETokenType.IDENTIFIER ->
                        if( first_or_second )
                            part_first = parseSimpleExpression(token.value)
                        else
                            part_second = parseSimpleExpression(token.value)



                    else ->
                        throw DParserError("Invalid expression at $from")

                }
            }

            when(symbol) {
                ETokenType.MINUS -> DMinusNode (
                    part_first,
                    part_second
                )

                else -> throw DParserError("Invalid expression at $from")
            }

        }

        else -> throw DParserError("Invalid expression at $from")
    }

    private fun parseRegOrVar(from: List<DToken>): AASTNode = when (from[0].type) {
        ETokenType.REG_PREFIX ->
            when {
                from[1].value!!.startsWith("general_reg") -> DGeneralRegNode(from[1].value!!)

                else -> throw DParserError("Undefined reg or var: $from")
            }
        ETokenType.VAR_PREFIX ->
            when {
                from[1].value!! == "system_call_id" -> DSystemCallIdVarNode()
                from[1].value!! == "current_address" -> DCurrentAddressVarNode()
                from[1].value!!.startsWith("system_call_arg") -> DSystemCallArgVarNode(from[1].value!!)

                else -> throw DParserError("Undefined reg or var: $from")
            }

        else -> throw DParserError("Undefined reg or var: $from")
    }


}