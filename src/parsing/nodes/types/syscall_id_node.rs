use crate::parsing::ASTNode;

#[derive(Debug, Clone)]
pub struct SyscallIdNode {}

impl ASTNode for SyscallIdNode {}

impl SyscallIdNode {
    pub fn new() -> Self {
        Self {}
    }
}