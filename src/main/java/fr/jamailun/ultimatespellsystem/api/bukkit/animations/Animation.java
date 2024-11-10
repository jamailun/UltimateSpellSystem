package fr.jamailun.ultimatespellsystem.api.bukkit.animations;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class Animation {

    @Getter(AccessLevel.PROTECTED) private int ticks = 0;

    protected abstract int getDuration();

    protected abstract void onStart();

    public final void tick() {
        if(ticks == 0) {
            onStart();
        }
        ticks++;
        onTick();
        if(ticks >= getDuration()) {
            onFinish();
        }
    }

    protected abstract void onTick();

    protected abstract void onFinish();


    public final boolean isOver() {
        return ticks >= getDuration();
    }
}
