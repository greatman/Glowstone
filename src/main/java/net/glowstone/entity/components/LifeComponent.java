package net.glowstone.entity.components;

import com.artemis.Component;
import lombok.Data;

/**
 * Keep track of the entity life time
 */
@Data
public class LifeComponent extends Component {

    private int ticksLived;


}
