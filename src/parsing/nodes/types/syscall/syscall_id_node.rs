use std::any::Any;
use crate::parsing::ASTNode;

#[derive(Debug, Clone)]
pub struct SyscallIdNode {}

impl ASTNode for SyscallIdNode {
    fn id(&self) -> String {
        "syscall_id".to_string()
    }

    fn as_any(&self) -> &dyn Any {
        self
    }
}

impl SyscallIdNode {
    pub fn new() -> Self {
        Self {}
    }
}