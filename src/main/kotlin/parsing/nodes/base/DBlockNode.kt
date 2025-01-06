package parsing.nodes.base

import parsing.nodes.AASTNode

data class DBlockNode(
    val name: String,
    val children: List<AASTNode>
) : AASTNode () {
    override val id: String = "BLOCK"
}
