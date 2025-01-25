package parsing.nodes.regs_and_vars

import parsing.nodes.AASTNode

data class DGeneralRegNode(
    val label: UByte
) : AASTNode() {
    override val id: String = "general_reg$label"

    constructor(from: String) : this(from.last().digitToInt().toUByte())
}
