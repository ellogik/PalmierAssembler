
#![allow(non_snake_case, dead_code, unreachable_code)]

use std::collections::HashMap;
use crate::parsing::{ASTNode, GeneralRegNode, InvalidNode};
use crate::plugins::errors::PluginError;

#[derive(Clone)]
pub struct Command {
    pub op_code: String,
    pub is_simple: bool,
}

impl Command {
    pub fn new(op_code: String, is_simple: bool) -> Self {
        Self {
            op_code,
            is_simple,
        }
    }

    pub fn from_string(from: String) -> Self {
        let splitted: Vec<&str> = from.split(",").collect();
        let is_simple = match splitted[1] {
            "true" => true,
            "false" => false,
            "0" => false,
            "1" => true,

            _ => false
        };

        Self::new(splitted[0].to_string(), is_simple)
    }
}

pub struct ArchitecturePlugin {
    pub for_arch: String,
    pub path_to_python_script: String,
    pub regs: HashMap<Box<dyn ASTNode>, String>,
    pub commands: HashMap<Box<dyn ASTNode>, Command>
}

impl ArchitecturePlugin {
    fn new(for_arch: String,
           path_to_python_script: String,
           regs: HashMap<Box<dyn ASTNode>, String>,
           commands: HashMap<Box<dyn ASTNode>, Command>
    ) -> Self {
        Self {
            for_arch,
            path_to_python_script,
            regs,
            commands
        }
    }

    pub fn fromPythonScript(path_to_python_script: String) -> Result<Self, PluginError> {
        let mut regs: HashMap<Box<dyn ASTNode>, String> = HashMap::new();
        let mut commands: HashMap<Box<dyn ASTNode>, Command> = HashMap::new();
        let mut for_arch: String = "".to_string();

        match std::process::Command::new("python3")
            .arg(path_to_python_script.clone())
            .arg("base_info")
            .output() {
            Ok(out) => {
                let _out_of_script_raw = String::from_utf8(out.stdout).unwrap();
                let _out_of_script: Vec<&str> = _out_of_script_raw.split(",").collect();
                if _out_of_script[0] != "Architecture" {
                    return Err(PluginError::WRONG_TYPE)
                }
                for_arch = _out_of_script[1].to_string();
            }
            Err(err) => {
                println!("Original error: {:?}", err);
                return Err(PluginError::CANNOT_RUN_PLUGIN_SCRIPT)
            }
        };

        regs = Self::getAllRegsFromPythonScript(path_to_python_script.clone()).expect("FAIL::WHILE::GET_COMMANDS");



        Ok(Self::new(
            for_arch,
            path_to_python_script.clone(),
            regs,
            commands
        ))
    }

    fn getAllRegsFromPythonScript(path_to_python_script: String) -> Result<HashMap<Box<dyn ASTNode>, String>, PluginError> {
        let mut regs_ret: HashMap<Box<dyn ASTNode>, String> = HashMap::new();
        let mut regs: HashMap<String, Box<dyn ASTNode>> = HashMap::new();

        regs.insert("general_reg1".to_string(), Box::new(GeneralRegNode::new(1)));
        regs.insert("general_reg2".to_string(), Box::new(GeneralRegNode::new(2)));



        for reg in regs {
            match std::process::Command::new("python3")
                .arg(path_to_python_script.clone())
                .arg("get_reg")
                .arg("general_reg1")
                .output() {
                    Ok(out) => {
                        let _out_of_script_raw: String = String::from_utf8(out.stdout).unwrap();

                        regs_ret.insert(reg.1, _out_of_script_raw);
                    }
                    Err(err) => {
                        println!("Original error: {:?}", err);
                        return Err(PluginError::ERROR_WHILE_PARSE_COMMANDS)
                    }
            }
        }

        Ok(regs_ret)
    }
}