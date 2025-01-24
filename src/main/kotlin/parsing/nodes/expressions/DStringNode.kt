package parsing.nodes.expressions

import parsing.nodes.AASTNode

data class DStringNode(val value: String) : AASTNode() {
    override val id: String = "STRING:$value"
}