package net.glowstone.entity.components;

import com.artemis.Component;
import lombok.Data;

@Data
public class AirComponent extends Component {

    public static final int MAX_AIR = 300;
    private int air = MAX_AIR;
}
