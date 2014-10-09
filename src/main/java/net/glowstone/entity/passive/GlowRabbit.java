package net.glowstone.entity.passive;

import net.glowstone.entity.GlowAnimal;

import org.bukkit.Location;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.EntityType;

public class GlowRabbit extends GlowAnimal implements Rabbit {

    /**
     * Creates a new Chicken.
     *
     * @param location The location of the monster.
     */
    public GlowRabbit(Location location) {
        super(location, EntityType.RABBIT);
        setSize(0.3F, 0.7F);
    }

    @Override
    public RabbitType getRabbitType()
    {
        // TODO Implement
        return null;
    }

    @Override
    public void setRabbitType(RabbitType type)
    {
        // TODO Implement

    }
}
