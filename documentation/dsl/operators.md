# Operators

All langages have operators. The syntax with USS is somewhat classic.

## Math-operators

Nothing game-changing here :
- `+`, `-`, `/`, `^` : will do math operations on numbers. Will "return" a number.
- If left and right operands are durations, returns a duration (but `/` and `*` need the same duration's type).

**Examples**
```bash
define %a = 7.5 + 4.5; # a = 12
define %b = %a - 10;   # b = 2
define %c = %a / %b;   # c = 6
```

### Durations

You can add and subtract durations, and obtain a new one.
You can also divide a duration by another, and obtain a number.

**Examples:**
```bash
define %dur_a = 5 minutes;
define %dur_b = %dur_a * 2; # 10 minutes
define %dur_c = %dur_a + %dur_b; # 15 minutes
define %count = %dur_c / %dur_a; # 3
```

### Concatenation

The `+` operator can also "add" other types, such as :
- `string` + `string` => Concatenation of both strings.
- `string` + `any` => Concatenation of the string and the other elements (using the `Object#toString()` java method).

### Position delta

You can add a **Vector** to a location, using a `+` and `-` operator.

**Example:**
```bash
define %pos_caster = position of %caster;
define %block_under_caster = %pos_caster - [[0, -1, 0]];
```

## Comparators

Each of them accept numbers OR durations and return a boolean.
- `>=`, `>`, `<=`, `<`

Each of the following can test equality on any type :
- `==`, `!=`
