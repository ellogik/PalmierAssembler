package parsing.nodes.expressions

import parsing.nodes.AASTNode

data class DIdNode(
    val label: String
) : AASTNode() {
    override val id: String = "id:$label"
}