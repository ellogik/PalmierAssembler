use std::any::Any;
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

    fn as_any(&self) -> &dyn Any {
        self
    }
}

impl MoveCommandNode {
    pub fn new(receiver: Box<dyn ASTNode>, value: Box<dyn ASTNode>) -> Self {
        Self { receiver, value }
    }
}
