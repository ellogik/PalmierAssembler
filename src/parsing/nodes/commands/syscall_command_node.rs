use crate::parsing::nodes::ASTNode;

#[derive(Clone, Debug)]
pub struct SyscallCommandNode {}

impl ASTNode for SyscallCommandNode {
    fn id(&self) -> String {
        "syscall".to_string()
    }
}

impl SyscallCommandNode {
    pub fn new() -> Self { Self {} }
}