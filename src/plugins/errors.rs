#![allow(non_camel_case_types)]

use std::error::Error;
use std::fmt::{Display, Formatter};

#[derive(Debug)]
pub enum PluginError {
    CANNOT_RUN_PLUGIN_SCRIPT,
    WRONG_TYPE,
    ERROR_WHILE_PARSE_COMMANDS,
    ERROR_WHILE_PARSE_REGS,
}

impl Display for PluginError {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "Error while plugin executing! For more information {:#?}", self)
    }
}

impl Error for PluginError {

}