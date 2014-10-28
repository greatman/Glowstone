package net.glowstone.entity.components;

import com.artemis.Component;
import lombok.Data;

@Data
public class NameComponent extends Component {

    private String customName = null;
    private boolean customNameVisible = false;
}
