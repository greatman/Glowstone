package net.glowstone.entity.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import net.glowstone.entity.components.GlowEntityComponent;
import net.glowstone.entity.components.LifeComponent;
import net.glowstone.entity.components.LocationComponent;

public class LifeSystem extends EntityProcessingSystem {

    public LifeSystem() {
        super(Aspect.getAspectForAll(LifeComponent.class));
    }

    @Override
    protected void process(Entity e) {
        e.getComponent(LifeComponent.class).setTicksLived(e.getComponent(LifeComponent.class).getTicksLived() + 1);
        if (e.getComponent(LifeComponent.class).getTicksLived() % (30 * 20) == 0) {
            e.getComponent(LocationComponent.class).setTeleported(true);
        }
    }
}
