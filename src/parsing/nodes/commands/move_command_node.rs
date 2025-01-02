use crate::parsing::nodes::ASTNode;

#[derive(Debug)]
pub struct MoveCommandNode {
    receiver: Box<dyn ASTNode>,
    value: Box<dyn ASTNode>
}

impl ASTNode for MoveCommandNode {
    fn id(&self) -> String {
        "move".to_string()
    }
}

impl MoveCommandNode {
    pub fn new(receiver: Box<dyn ASTNode>, value: Box<dyn ASTNode>) -> Self {
        Self { receiver, value }
    }
}
