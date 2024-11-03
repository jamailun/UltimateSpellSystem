# Operators

All langages have operators. The syntax with USS is really classic.

## Math-operators

Nothing game-changing here :
- `+`, `-`, `/`, `^` : will do math operations on numbers. Will "return" a number.
- If left and right operands are durations, returns a duration (but `/` and `*` need the same duration's type).


### Concatenation

The `+` operator can also "add" other types, such as :
- `string` + `string` => Concatenation of both strings.
- `string` + `any` => Concatenation of the string and the other elements (using the `Object#toString()` java method).


## Comparators

Each of them accept numbers OR durations and return a boolean.
- `>=`, `>`, `<=`, `<`

Each of the following can test equality on any type :
- `==`, `!=`
