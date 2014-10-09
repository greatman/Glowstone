package net.glowstone.entity.minecart;

import java.util.List;

import net.glowstone.entity.GlowEntity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

import com.flowpowered.networking.Message;

public abstract class GlowAbstractMinecart extends GlowEntity implements Minecart
{
    public GlowAbstractMinecart(Location location)
    {
        super(location);
    }

    @Override
    public List<Message> createSpawnMessage()
    {
        return null; // TODO Implement
    }

    @Override
    public void _INVALID_setDamage(int damage)
    {
        // TODO Implement
    }

    @Override
    public void setDamage(double damage)
    {
        // TODO Implement
    }

    @Override
    public int _INVALID_getDamage()
    {
        return 0; // TODO Implement
    }

    @Override
    public double getDamage()
    {
        return 0; // TODO Implement
    }

    @Override
    public double getMaxSpeed()
    {
        return 0; // TODO Implement
    }

    @Override
    public void setMaxSpeed(double speed)
    {
        // TODO Implement
    }

    @Override
    public boolean isSlowWhenEmpty()
    {
        return false; // TODO Implement
    }

    @Override
    public void setSlowWhenEmpty(boolean slow)
    {
        // TODO Implement
    }

    @Override
    public Vector getFlyingVelocityMod()
    {
        return null; // TODO Implement
    }

    @Override
    public void setFlyingVelocityMod(Vector flying)
    {
        // TODO Implement
    }

    @Override
    public Vector getDerailedVelocityMod()
    {
        return null; // TODO Implement
    }

    @Override
    public void setDerailedVelocityMod(Vector derailed)
    {
        // TODO Implement
    }

    @Override
    public EntityType getType()
    {
        return null; // TODO Implement
    }
}
