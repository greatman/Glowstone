package net.glowstone.entity.components;

import com.artemis.Component;
import lombok.Data;

@Data
public class HealthComponent extends Component {

    private double maxHealth, health;
}
