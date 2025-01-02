
#![allow(non_snake_case, dead_code, unreachable_code)]

use std::collections::HashMap;
use crate::parsing::{ASTNode, GeneralRegNode, InvalidNode, MoveCommandNode, SyscallCommandNode};
use crate::plugins::errors::PluginError;

#[derive(Clone, Debug)]
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

    pub fn fromString(from: String) -> Self {
        let splitted: Vec<&str> = from.split(",").collect();

        Self::new(splitted[0].to_string(), parseBoolean(splitted[1]))
    }
}

pub struct ArchitecturePlugin {
    pub for_arch: String,
    pub path_to_python_script: String,
    pub regs: HashMap<String, String>,
    pub commands: HashMap<String, Command>
}

impl ArchitecturePlugin {
    fn new(for_arch: String,
           path_to_python_script: String,
           regs: HashMap<String, String>,
           commands: HashMap<String, Command>
    ) -> Self {
        Self {
            for_arch,
            path_to_python_script,
            regs,
            commands
        }
    }

    pub fn fromPythonScript(path_to_python_script: String) -> Result<Self, PluginError> {
        let mut regs: HashMap<String, String> = HashMap::new();
        let mut commands: HashMap<String, Command> = HashMap::new();
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

        regs = Self::getAllRegsFromPythonScript(path_to_python_script.clone()).expect("FAIL::WHILE::GET_REGS");
        commands = Self::getAllCommandsFromPythonScript(path_to_python_script.clone()).expect("FAIL::WHILE::GET_COMMANDS");


        Ok(Self::new(
            for_arch,
            path_to_python_script.clone(),
            regs,
            commands
        ))
    }

    fn getAllRegsFromPythonScript(path_to_python_script: String) -> Result<HashMap<String, String>, PluginError> {
        let mut regs_ret: HashMap<String, String> = HashMap::new();
        let regs: Vec<Box<dyn ASTNode>> = vec![
            Box::new(GeneralRegNode::new(1)),
            Box::new(GeneralRegNode::new(2))
        ];



        for reg in regs {
            match std::process::Command::new("python3")
                .arg(path_to_python_script.clone())
                .arg("get_reg")
                .arg(reg.id())
                .output() {
                    Ok(out) => {
                        let _out_of_script_raw: String = String::from_utf8(out.stdout).unwrap();
                        regs_ret.insert(reg.id(), _out_of_script_raw.trim().to_string());
                    }
                    Err(err) => {
                        println!("Original error: {:?}", err);
                        return Err(PluginError::ERROR_WHILE_PARSE_REGS)
                    }
            }
        }

        Ok(regs_ret)
    }

    fn getAllCommandsFromPythonScript(path_to_python_script: String) -> Result<HashMap<String, Command>, PluginError> {
        let mut cmds_ret: HashMap<String, Command> = HashMap::new();

        let cmds: Vec<Box<dyn ASTNode>> = vec![
            Box::new(MoveCommandNode::new(
                Box::new(InvalidNode::new()),
                Box::new(InvalidNode::new())
            )),
            Box::new(SyscallCommandNode::new())
        ];

        for cmd in cmds {
            match std::process::Command::new("python3")
                .arg(path_to_python_script.clone())
                .arg("get_cmd")
                .arg(cmd.id())
                .output() {
                Ok(out) => {
                    cmds_ret.insert(
                        cmd.id(),
                        Command::fromString(
                            String::from_utf8(out.stdout).unwrap().to_string()
                        )
                    );
                }
                Err(err) => {
                    println!("Original error: {:?}", err);
                    return Err(PluginError::ERROR_WHILE_PARSE_COMMANDS)
                }
            }
        }

        Ok(cmds_ret)
    }
}

fn parseBoolean(value: &str) -> bool {
    matches!(value.trim(), "true" | "1")
}