use crate::parsing::ASTNode;

#[derive(Debug, Clone)]
pub struct SyscallArgNode{
    pub label: u8
}

impl ASTNode for SyscallArgNode {}

impl SyscallArgNode {
    pub fn new(label: u8) -> Self {
        Self {
            label
        }
    }
}