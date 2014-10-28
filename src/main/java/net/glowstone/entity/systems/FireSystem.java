package net.glowstone.entity.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.GlowLivingEntity;
import net.glowstone.entity.components.FireComponent;
import net.glowstone.entity.components.GlowEntityComponent;
import net.glowstone.entity.components.HealthComponent;
import net.glowstone.entity.components.MetadataComponent;
import net.glowstone.entity.meta.MetadataIndex;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireSystem extends EntityProcessingSystem {

    public FireSystem() {
        super(Aspect.getAspectForAll(FireComponent.class, HealthComponent.class, GlowEntityComponent.class, MetadataComponent.class));
    }

    @Override
    protected void process(Entity e) {
        FireComponent component = e.getComponent(FireComponent.class);
        GlowEntity entity = e.getComponent(GlowEntityComponent.class).getEntity();
        if (component.getFireTicks() > 0) {
            e.getComponent(MetadataComponent.class).getMetadata().setBit(MetadataIndex.STATUS, MetadataIndex.StatusFlags.ON_FIRE, component.getFireTicks() > 0);
            if (entity instanceof GlowLivingEntity) {
                ((GlowLivingEntity) entity).damage(1, EntityDamageEvent.DamageCause.FIRE_TICK);

            } else {
                //TODO : Destroy the entity
            }
            component.setFireTicks(component.getFireTicks() - 1);
        }
    }
}
