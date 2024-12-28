#pragma once

#include <string>
#include <vector>
#include <stdexcept>
#include <bits/stdc++.h>

#include "Token.hpp"


namespace Lexer {
	class Lexer {
	public:
		explicit Lexer(const std::string &text);

		[[nodiscard]] std::vector<std::vector<Token>> tokenize();

	private:
		std::string text;
		uint position_in_line = 0;

		Token processIdentifier(const std::string &line);
	};
} // Lexer