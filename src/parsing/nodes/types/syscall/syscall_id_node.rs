use crate::parsing::ASTNode;

#[derive(Debug, Clone)]
pub struct SyscallIdNode {}

impl ASTNode for SyscallIdNode {
    fn id(&self) -> String {
        "syscall_id".to_string()
    }
}

impl SyscallIdNode {
    pub fn new() -> Self {
        Self {}
    }
}