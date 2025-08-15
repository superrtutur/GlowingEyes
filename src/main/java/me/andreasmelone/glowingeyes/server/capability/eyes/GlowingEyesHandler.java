package me.andreasmelone.glowingeyes.server.capability.eyes;

import me.andreasmelone.glowingeyes.GlowingEyes;
import me.andreasmelone.glowingeyes.server.util.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class GlowingEyesHandler implements INBTSerializable<CompoundTag>, ICapabilityProvider {
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(GlowingEyes.MOD_ID, "glowingeyes");

    IGlowingEyes glowingeyes = new GlowingEyesImpl();
    LazyOptional<IGlowingEyes> instance = LazyOptional.of(() -> glowingeyes);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("toggledOn", glowingeyes.isToggledOn());
        tag.putByteArray("glowingEyesMap", Util.serializeMap(glowingeyes.getGlowingEyesMap()));
        Color color = glowingeyes.getBasicColor();
        tag.putInt("basicColorR", color.getRed());
        tag.putInt("basicColorG", color.getGreen());
        tag.putInt("basicColorB", color.getBlue());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        glowingeyes.setToggledOn(compoundTag.getBoolean("toggledOn"));
        glowingeyes.setGlowingEyesMap(Util.deserializeMap(compoundTag.getByteArray("glowingEyesMap")));
        int r = compoundTag.getInt("basicColorR");
        int g = compoundTag.getInt("basicColorG");
        int b = compoundTag.getInt("basicColorB");
        glowingeyes.setBasicColor(new Color(r, g, b));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        return GlowingEyesCapability.INSTANCE.orEmpty(capability, instance);
    }

    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            GlowingEyesHandler glowingEyesHandler = new GlowingEyesHandler();
            event.addCapability(GlowingEyesHandler.IDENTIFIER, glowingEyesHandler);
        }
    }
}
