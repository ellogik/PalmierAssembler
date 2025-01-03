use std::any::Any;
use std::fmt::{Debug, Display, Formatter};
use std::hash::Hash;

pub trait ASTNode: Debug {
    fn id(&self) -> String;
    fn as_any(&self) -> &dyn Any;
}