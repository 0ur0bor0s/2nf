# 2nf
2NF is the variation of CNF which allows rules A-> xy where x and y can be either a single variable or a single terminal.
This program, given a grammar file containing the production rules of a context free grammar, is able to parse a given string to determine whether the string is generated accordiding the context free grammar in 2nf form.

The program will output `YES` if the grammar generates the string and `NO` if it does not.

# Example
```bash
java Cyk grammar_file aabba
YES
```
