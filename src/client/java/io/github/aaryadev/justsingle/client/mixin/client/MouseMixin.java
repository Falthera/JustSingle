package io.github.aaryadev.justsingle.client.mixin.client;

import io.github.aaryadev.justsingle.client.input.MouseInputInterceptor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onMouseButton", at = @At("TAIL"))
    private void justsingle$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        MouseInputInterceptor.getInstance().onMouseButton((Mouse) (Object) this, client, window, button, action, mods);
    }
}
