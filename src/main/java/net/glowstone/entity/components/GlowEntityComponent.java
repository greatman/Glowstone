package net.glowstone.entity.components;

import com.artemis.Component;
import lombok.Data;
import net.glowstone.entity.GlowEntity;

/**
 * Component to get back to the Glowstone entity.
 */
@Data
public class GlowEntityComponent extends Component {

    private final GlowEntity entity;
}
