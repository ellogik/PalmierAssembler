use std::any::Any;
use crate::parsing::ASTNode;

#[derive(Clone, Debug)]
pub struct GeneralRegNode {
    label: u8
}

impl ASTNode for GeneralRegNode {
    fn id(&self) -> String {
        format!("general_reg{}", self.label)
    }

    fn as_any(&self) -> &dyn Any {
        self
    }
}

impl GeneralRegNode {
    pub fn new(label: u8) -> Self {
        Self { label }
    }
}