use std::error::Error;
use std::fmt::{Display, Formatter};

#[derive(Debug)]
pub struct UnknownCommandError {
    at_file: String,
    at_line: usize,
    command: String,
}

impl Display for UnknownCommandError {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "Unknown command '{}' in file '{}' at line {}",
               self.command,
               self.at_file,
               self.at_line + 1
        )
    }
}

impl Error for UnknownCommandError {}

impl UnknownCommandError {
    pub fn new(at_file: String, at_line: usize, command: String) -> Self {
        Self {
            command,
            at_file,
            at_line,
        }
    }
}