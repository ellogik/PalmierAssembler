use crate::parsing::ASTNode;

#[derive(Clone, Debug)]
pub struct GeneralRegNode {
    label: u8
}

impl ASTNode for GeneralRegNode {

}

impl GeneralRegNode {
    pub fn new(label: u8) -> Self {
        Self { label }
    }
}