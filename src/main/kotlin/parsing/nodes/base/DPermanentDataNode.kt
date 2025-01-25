package parsing.nodes.base

import parsing.nodes.AASTNode

data class DPermanentDataNode(
    val name: String,
    val value: AASTNode
) : AASTNode() {
    override val id: String = "permanent:$name,$value"
}
