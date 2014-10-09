package net.glowstone.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Flying;

public abstract class GlowFlying extends GlowLivingEntity implements Flying {

    private final EntityType type;

    protected GlowFlying(Location location, EntityType type) {
        super(location);
        this.type = type;
    }

    @Override
    public final EntityType getType() {
        return this.type;
    }
}
