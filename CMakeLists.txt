cmake_minimum_required(VERSION 3.29)

project(PalmierAssembler CXX)

add_executable(PalmierAssembler
    Source/main.cpp
        Source/Lexer/Lexer.hpp
        Source/Lexer/Lexer.cpp
        Source/Lexer/Token.hpp
)