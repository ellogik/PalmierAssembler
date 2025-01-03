#![allow(non_snake_case, non_camel_case_types)]

use std::error::Error;
use std::fmt::{Debug, Display, Formatter};

#[derive(Debug)]
pub enum ECompileError {
    NOT_IMPLEMENTED_COMMAND_IN_PALMIER,
    ERROR_WHILE_EXECUTING_PLUGIN,
}

impl Display for ECompileError {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "Error while compile occurred! See error code: {:?}", self)
    }
}

impl Error for ECompileError {

}