package net.glowstone.entity.passive;

import net.glowstone.entity.GlowGolem;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Snowman;

public class GlowIronGolem extends GlowGolem implements IronGolem
{
    private boolean isPlayerCreated = false;

    public GlowIronGolem(Location location)
    {
        super(location, EntityType.IRON_GOLEM);
    }

    @Override
    public boolean isPlayerCreated()
    {
        return this.isPlayerCreated;
    }

    @Override
    public void setPlayerCreated(boolean playerCreated)
    {
        this.isPlayerCreated = playerCreated;
    }
}
