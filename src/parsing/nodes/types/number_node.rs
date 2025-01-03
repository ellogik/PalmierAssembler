use std::any::Any;
use crate::parsing::ASTNode;

#[derive(Debug, Clone)]
pub enum NumberType {
    INTEGER,
    FLOAT,
}

#[derive(Debug, Clone)]
pub struct NumberNode {
    pub value: String,
    pub number_type: NumberType
}

impl ASTNode for NumberNode {
    fn id(&self) -> String {
        "number".to_string()
    }

    fn as_any(&self) -> &dyn Any {
        self
    }
}

impl NumberNode {
    pub fn new(value: String, number_type: NumberType) -> Self {
        Self {
            value,
            number_type
        }
    }
}