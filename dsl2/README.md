# USS - DSL 2

### Objectif

```javascript

let position = position_of(caster);
position += [0, 2, 0];

for(let entity in query_entities(position, 20, "players")) {
    entity.send_message("Hello World!");
    entity.send_effect("resistance", D`20s`, 1);
}


```

```cpp
void main() {
    vector position = position_of(caster);
    position += (vector){0, 2, 0};

    entity_query_result result = query_entities(position, 20, "players");
    for(int i = 0; i < result.size; i++) {
        entity entity = result.entities[i];
        entity_send_message(entity, "Hello World!");
        entity_send_effect(entity, "resistance", duration_from_seconds(20), 1);
    }
}
```


## syntax

```
IDENTIFIER ::= [a-zA-Z_][a-zA-Z0-9_]*
STRING ::= '"' .* '"'
NUMBER ::= [0-9]*(.[0-9]+)?
BOOLEAN ::= "true" | "false"

LITERAL ::=
    STRING
    | NUMBER
    | BOOLEAN
    | "null"
    | LIST
    | MAP
    
LIST ::= [ (EXPRESSION (EXPRESSION )*)? ]

MAP ::= { (IDENTIFIER: EXPRESSION,)* }

a.b[c].d(x).e = z

EXPRESSION ::= 
    LITERAL
    | IDENTIFIER
    | IDENTIFIER(PARAMETERS)
    | EXPRESSION OPERATOR EXPRESSION
    | (EXPRESSION)
    | -EXPRESSION
    | !EXPRESSION
    | EXPRESSION[EXPRESSION]
    | EXPRESSION.EXPRESSION

ANNOTATION ::= "@" IDENTIFIER ( "(" EXPRESSIONS (, EXPRESSION)* ")" )?

PARAMETERS ::= TYPE IDENTIFIER (, TYPE IDENTIFIER)* | ε

TYPE ::= IDENTIFIER | "void"

OPERATOR ::= "+" | "-" | "*" | "/" | "%" | "==" | "!=" | "<" | ">" | "<=" | ">=" | "&&" | "||"

STATEMENT ::=
    TYPE IDENTIFIER (= EXPRESSION)?;
    | EXPRESSION = EXPRESSION;
    | for(STATEMENT; STATEMENT; STATEMENT) BLOCK_STATEMENTS
    | foreach(TYPE IDENTIFIER in EXPRESSION) BLOCK_STATEMENTS
    | while(EXPRESSION) BLOCK_STATEMENTS
    | do BLOCK_STATEMENTS while(EXPRESSION);
    | if(EXPRESSION) BLOCK_STATEMENTS (else BLOCK_STATEMENTS)?
    | "break"; | "continue"; | "return" (EXPRESSION)?;
    | EXPRESSION(PARAMETERS);
    
BLOCK_STATEMENTS ::=
    {
        STATEMENT (, STATEMENT)*
    }

FUNCTION ::=
    TYPE IDENTIFIER(PARAMETERS) {
        STATEMENT (, STATEMENT)*
    }


```

