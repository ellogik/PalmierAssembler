use crate::parsing::nodes::ASTNode;

#[derive(Clone)]
pub struct SyscallCommandNode {}

impl ASTNode for SyscallCommandNode {}

impl SyscallCommandNode {
    pub fn new() -> Self { Self {} }
}