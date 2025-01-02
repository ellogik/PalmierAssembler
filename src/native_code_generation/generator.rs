use std::any::Any;
use std::fmt::Display;
use crate::parsing::{ASTNode, BlockNode, BLOCK_ID};
use crate::plugins::ArchitecturePlugin;

pub struct CodeGenerator {
    architecture_plugin: ArchitecturePlugin,
    input: Vec<Box<dyn ASTNode>>,
    output: Vec<isize>
}

impl CodeGenerator {
    pub fn new(architecture_plugin: ArchitecturePlugin, input: Vec<Box<dyn ASTNode>>) -> Self {
        Self {
            architecture_plugin,
            input,
            output: vec![],
        }
    }
    pub fn compile(&mut self) {
        for parent_node in &self.input {
            match parent_node.id() {
                c if c == BLOCK_ID => {
                    let block: BlockNode = parent_node.parse::<BlockNode>();

                    println!("Block HERE!")
                }

                _ => {}
            }
        }
    }
}