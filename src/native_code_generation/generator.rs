#![allow(non_snake_case)]

use std::fmt::Display;
use crate::native_code_generation
::errors::ECompileError;
use crate::parsing::{ASTNode, BlockNode, BLOCK_ID};
use crate::plugins::ArchitecturePlugin;

pub struct CodeGenerator {
    architecture_plugin: ArchitecturePlugin,
    input: Vec<Box<dyn ASTNode>>,
    output: Vec<usize>
}

impl CodeGenerator {
    pub fn new(architecture_plugin: ArchitecturePlugin, input: Vec<Box<dyn ASTNode>>) -> Self {
        Self {
            architecture_plugin,
            input,
            output: vec![],
        }
    }
    pub fn compile(&mut self) -> Result<Vec<usize>, ECompileError> {
        for parent_node in &self.input {
            match parent_node.id() {
                c if c == BLOCK_ID => {
                    if let Some(block) = (*parent_node).as_any().downcast_ref::<BlockNode>() {
                        match self.compileBlock(block) {
                            Ok(out) => {
                                self.output.extend(out);
                            }
                            Err(err) => {
                                return Err(err)
                            }
                        };
                    }
                }

                _ => {}
            }
        }

        Ok(self.output.clone())
    }

    fn compileBlock(&self, content: &BlockNode) -> Result<(Vec<usize>), ECompileError>{
        let mut native_cmds = vec![];



        for child in &content.children {
            native_cmds.extend(
                self.compileCommand(child)
                    .expect(
                        format!(
                            "Failed to compile command: {:#?}", child
                        ).as_str()
                    )
            )
        }


        Ok(native_cmds)
    }

    fn compileCommand(&self, content: &Box<dyn ASTNode>) -> Result<Vec<usize>, ECompileError> {
        let mut native_cmd: Vec<usize> = vec![];
        if let Some(cmd) = self.architecture_plugin.commands.get(&content.id()) {
            if cmd.is_simple {
                native_cmd.extend(Self::parseHexNumbers(cmd.op_code.as_str()))
            } else {
                match std::process::Command::new("python3")
                    .arg(self.architecture_plugin.path_to_python_script.clone())
                    .arg("combine_difficult_cmd")
                    .arg(cmd.op_code.as_str())
                    .output() {
                    Ok(out) => {}
                    Err(err) => {
                        return Err(ECompileError::ERROR_WHILE_EXECUTING_PLUGIN)
                    }
                };

            }
            println!("{:#?}", native_cmd);

            Ok(native_cmd)
        } else {
            Err(ECompileError::NOT_IMPLEMENTED_COMMAND_IN_PALMIER)
        }



    }

    fn parseHexNumbers(input: &str) -> Vec<usize> {
        input
            .split_whitespace() // divide by spaces
            .filter_map(|hex| usize::from_str_radix(hex.trim_start_matches("0x"), 16).ok()) // Parse every number as hex
            .collect() // Collect vector of hex numbers
    }
}