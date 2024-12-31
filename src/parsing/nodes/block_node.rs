use crate::parsing::nodes::ast_node::ASTNode;

#[derive(Debug)]
pub struct BlockNode {
    pub name: String,
    pub children: Vec<Box<dyn ASTNode>>
}

impl ASTNode for BlockNode {}

impl BlockNode {
    pub fn new(name: String, children: Vec<Box<dyn ASTNode>>) -> Self {
        Self { name, children }
    }
}