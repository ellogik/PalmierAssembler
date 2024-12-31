use crate::parsing::nodes::ASTNode;

#[derive(Debug)]
pub struct MoveCommandNode {
    receiver: Box<dyn ASTNode>,
    value: Box<dyn ASTNode>
}

impl ASTNode for MoveCommandNode {}

impl MoveCommandNode {
    pub fn new(receiver: Box<dyn ASTNode>, value: Box<dyn ASTNode>) -> Self {
        Self { receiver, value }
    }
}
