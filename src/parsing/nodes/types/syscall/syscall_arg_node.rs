use std::any::Any;
use crate::parsing::ASTNode;

#[derive(Debug, Clone)]
pub struct SyscallArgNode{
    pub label: u8
}

impl ASTNode for SyscallArgNode {
    fn id(&self) -> String {
        format!("syscall-arg{}", self.label)
    }

    fn as_any(&self) -> &dyn Any {
        self
    }
}

impl SyscallArgNode {
    pub fn new(label: u8) -> Self {
        Self {
            label
        }
    }
}