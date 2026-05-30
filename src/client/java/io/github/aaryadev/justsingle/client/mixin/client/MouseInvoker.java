package io.github.aaryadev.justsingle.client.mixin.client;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mouse.class)
public interface MouseInvoker {
    @Invoker("onMouseButton")
    void justsingle$invokeOnMouseButton(long window, int button, int action, int mods);
}
