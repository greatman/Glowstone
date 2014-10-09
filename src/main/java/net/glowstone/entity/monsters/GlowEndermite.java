package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;

import org.bukkit.Location;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

public class GlowEndermite extends GlowMonster implements Endermite {

    private int lifetime;

    public GlowEndermite(Location location) {
        super(location, EntityType.ENDERMITE);
    }

    @Override
    public int getLifetime()
    {
        return this.lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }
}
