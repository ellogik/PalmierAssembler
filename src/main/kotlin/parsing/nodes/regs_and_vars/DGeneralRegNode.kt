package parsing.nodes.regs_and_vars

import parsing.nodes.AASTNode
import parsing.nodes.regs_and_vars.types.IRegNode

data class DGeneralRegNode(
    val label: UByte
) : AASTNode(), IRegNode {
    constructor(from: String) : this(from.last().digitToInt().toUByte())
}
