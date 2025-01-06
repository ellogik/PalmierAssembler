package parsing.nodes.commands

import parsing.nodes.AASTNode

data class DMoveCommandNode(
    val receiver: AASTNode,
    val value: AASTNode
) : AASTNode(){
    override val id: String = "move"
}