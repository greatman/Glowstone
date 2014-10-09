package net.glowstone.entity.monsters;

import java.util.List;

import net.glowstone.entity.GlowFlying;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

import com.flowpowered.networking.Message;

public class GlowGhast extends GlowFlying implements Ghast
{
    public GlowGhast(Location location)
    {
        super(location, EntityType.GHAST);
    }

    @Override
    public List<Message> createSpawnMessage()
    {
        return null; // TODO Implement
    }
}
