package net.glowstone.io.entity;

import net.glowstone.entity.minecart.GlowAbstractMinecart;
import net.glowstone.util.nbt.CompoundTag;

import org.bukkit.Location;
import org.bukkit.entity.Minecart;

public abstract class MinecartStore<T extends GlowAbstractMinecart> extends EntityStore<T>
{
    public MinecartStore(Class<T> clazz, String id)
    {
        super(clazz, id);
    }
}
