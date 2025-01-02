#![allow(non_camel_case_types, non_snake_case)]

use crate::parsing::ASTNode;

#[derive(Debug, Clone, PartialEq)]
pub struct InvalidNode {}

impl ASTNode for InvalidNode {
    fn id(&self) -> String {
        "INVALID".to_string()
    }
}

impl InvalidNode {
    pub fn new() -> Self {
        Self {}
    }
}