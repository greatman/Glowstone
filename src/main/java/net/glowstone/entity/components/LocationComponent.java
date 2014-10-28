package net.glowstone.entity.components;

import com.artemis.Component;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.Location;

@Data
public class LocationComponent extends Component {

    @NonNull
    private Location location;
    @NonNull
    private Location previousLocation;
    /**
     * Whether the entity should have its position resent as if teleported.
     */
    private boolean teleported = true;
}
