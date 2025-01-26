package parsing.nodes.regs_and_vars.system_calls

import parsing.nodes.AASTNode
import parsing.nodes.regs_and_vars.types.IOsVarNode
import parsing.nodes.regs_and_vars.types.IVarNode

data class DSystemCallArgVarNode(
    val label: UByte
) : AASTNode(), IOsVarNode {
    constructor(from: String) : this(from.last().digitToInt().toUByte())
}
