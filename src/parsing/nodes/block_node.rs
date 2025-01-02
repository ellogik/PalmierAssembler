use crate::parsing::nodes::ast_node::ASTNode;

#[derive(Debug)]
pub struct BlockNode {
    pub name: String,
    pub children: Vec<Box<dyn ASTNode>>
}

pub const BLOCK_ID: &str = "block";

impl ASTNode for BlockNode {
    fn id(&self) -> String {
        BLOCK_ID.to_string()
    }
}

impl BlockNode {
    pub fn new(name: String, children: Vec<Box<dyn ASTNode>>) -> Self {
        Self { name, children }
    }
}