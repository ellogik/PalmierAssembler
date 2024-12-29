#pragma once

#include <string>
#include <utility>

class Parser {
public:
    explicit Parser(std::string text);

private:
    const std::string text;
};
