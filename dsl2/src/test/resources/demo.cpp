function void foo(a, b) {
    x = 42;
    for(y = 0; y < 10; y++) {
        x += y;
    }
    dur = 20s;
    if(dur * 2 > 1min) {
        x = x - 5;
    }
    caster.send_message("salut");
}