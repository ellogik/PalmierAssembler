package parsing.nodes.regs_and_vars

import parsing.nodes.AASTNode

data class DSystemCallArgVarNode(
    val label: UByte
) : AASTNode() {
    override val id: String = "system_call_id"

    constructor(from: String) : this(from.last().digitToInt().toUByte())
}
