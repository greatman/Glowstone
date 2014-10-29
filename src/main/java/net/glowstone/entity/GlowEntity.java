package net.glowstone.entity;

import com.flowpowered.networking.Message;
import lombok.Getter;
import net.glowstone.EventFactory;
import net.glowstone.GlowChunk;
import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.entity.components.*;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.entity.physics.BoundingBox;
import net.glowstone.entity.physics.EntityBoundingBox;
import net.glowstone.net.message.play.entity.*;
import net.glowstone.util.Position;
import org.apache.commons.lang.Validate;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Represents some entity in the world such as an item on the floor or a player.
 * @author Graham Edgecombe
 */
public abstract class GlowEntity implements Entity {

    /**
     * The server this entity belongs to.
     */
    protected final GlowServer server;

    /**
     * The world this entity belongs to.
     */
    protected GlowWorld world;

    /**
     * A flag indicating if this entity is currently active.
     */
    protected boolean active = true;
    /**
     * This entity's current identifier for its world.
     */
    protected int id;
    /**
     * This entity's unique id.
     */
    private UUID uuid;
    /**
     * The entity's bounding box, or null if it has no physical presence.
     */
    private EntityBoundingBox boundingBox;
    /**
     * An EntityDamageEvent representing the last damage cause on this entity.
     */
    private EntityDamageEvent lastDamageCause;

    @Getter
    private com.artemis.Entity arthemisEntity;

    /**
     * Creates an entity and adds it to the specified world.
     * @param location The location of the entity.
     */
    public GlowEntity(Location location) {
        this.world = (GlowWorld) location.getWorld();
        arthemisEntity = world.getArthemisWorld().createEntity()
                .edit()
                .add(new LocationComponent(location.clone(), location.clone()))
                .add(new VelocityComponent())
                .add(new LifeComponent())
                .add(new FallGroundComponent())
                .add(new GlowEntityComponent(this))
            .getEntity();
        this.server = world.getServer();
        world.getEntityManager().allocate(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public final GlowServer getServer() {
        return server;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Core properties

    @Override
    public final GlowWorld getWorld() {
        return world;
    }

    @Override
    public final int getEntityId() {
        return id;
    }

    @Override
    public UUID getUniqueId() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    /**
     * Sets this entity's unique identifier if possible.
     * @param uuid The new UUID. Must not be null.
     * @throws IllegalArgumentException if the passed UUID is null.
     * @throws IllegalStateException if a UUID has already been set.
     */
    public void setUniqueId(UUID uuid) {
        Validate.notNull(uuid, "uuid must not be null");
        if (this.uuid == null) {
            this.uuid = uuid;
        } else if (!this.uuid.equals(uuid)) {
            // silently allow setting the same UUID, since
            // it can't be checked with getUniqueId()
            throw new IllegalStateException("UUID of " + this + " is already " + this.uuid);
        }
    }

    @Override
    public boolean isDead() {
        return !active;
    }

    @Override
    public boolean isValid() {
        return world.getEntityManager().getEntity(id) == this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Location stuff

    @Override
    public Location getLocation() {
        return arthemisEntity.getComponent(LocationComponent.class).getLocation();
    }

    @Override
    public Location getLocation(Location loc) {
        return Position.copyLocation(arthemisEntity.getComponent(LocationComponent.class).getLocation(), loc);
    }

    /**
     * Get the direction (SOUTH, WEST, NORTH, or EAST) this entity is facing.
     * @return The cardinal BlockFace of this entity.
     */
    public BlockFace getFacingDirection() {
        double rot = getLocation().getYaw() % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        if (0 <= rot && rot < 45) {
            return BlockFace.SOUTH;
        } else if (45 <= rot && rot < 135) {
            return BlockFace.WEST;
        } else if (135 <= rot && rot < 225) {
            return BlockFace.NORTH;
        } else if (225 <= rot && rot < 315) {
            return BlockFace.EAST;
        } else if (315 <= rot && rot < 360.0) {
            return BlockFace.SOUTH;
        } else {
            return BlockFace.EAST;
        }
    }

    @Override
    public Vector getVelocity() {
        return arthemisEntity.getComponent(VelocityComponent.class).getVelocity().clone();
    }

    @Override
    public void setVelocity(Vector velocity) {
        getVelocity().copy(velocity);
        arthemisEntity.getComponent(VelocityComponent.class).setVelocityChanged(true);
    }

    @Override
    public boolean teleport(Location location) {
        if (location.getWorld() != world) {
            world.getEntityManager().deallocate(this);
            world = (GlowWorld) location.getWorld();
            world.getEntityManager().allocate(this);
        }
        setRawLocation(location);
        arthemisEntity.getComponent(LocationComponent.class).setTeleported(true);
        return true;
    }

    @Override
    public boolean teleport(Entity destination) {
        return teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        return teleport(location);
    }

    @Override
    public boolean teleport(Entity destination, TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internals

    /**
     * Checks if this entity is within the visible radius of another.
     * @param other The other entity.
     * @return {@code true} if the entities can see each other, {@code false} if
     * not.
     */
    public boolean isWithinDistance(GlowEntity other) {
        return isWithinDistance(other.getLocation());
    }

    /**
     * Checks if this entity is within the visible radius of a location.
     * @param loc The location.
     * @return {@code true} if the entities can see each other, {@code false} if
     * not.
     */
    public boolean isWithinDistance(Location loc) {
        double dx = Math.abs(getLocation().getX() - loc.getX());
        double dz = Math.abs(getLocation().getZ() - loc.getZ());
        return loc.getWorld() == getWorld() && dx <= (server.getViewDistance() * GlowChunk.WIDTH) && dz <= (server.getViewDistance()
                                                                                                            * GlowChunk.HEIGHT);
    }

    /**
     * Checks whether this entity should be saved as part of the world.
     * @return True if the entity should be saved.
     */
    public boolean shouldSave() {
        return true;
    }

    /**
     * Called every game cycle. Subclasses should implement this to implement
     * periodic functionality e.g. mob AI.
     */
    public void pulse() {

        // resend position if it's been a while

        pulsePhysics();

        if (hasMoved()) {
            Block currentBlock = getLocation().getBlock();
            if (currentBlock.getType() == Material.ENDER_PORTAL) {
                EventFactory.callEvent(new EntityPortalEnterEvent(this, currentBlock.getLocation()));
                if (server.getAllowEnd()) {
                    Location previousLocation = getLocation().clone();
                    boolean success;
                    if (getWorld().getEnvironment() == World.Environment.THE_END) {
                        success = teleportToSpawn();
                    } else {
                        success = teleportToEnd();
                    }
                    if (success) {
                        EntityPortalExitEvent e = EventFactory.callEvent(new EntityPortalExitEvent(this, previousLocation, getLocation().clone(), getVelocity().clone(), new Vector()));
                        if (!e.getAfter().equals(getVelocity())) {
                            setVelocity(e.getAfter());
                        }
                    }
                }
            }
        }
    }

    /**
     * Resets the previous location and other properties to their current value.
     */
    public void reset() {
        Position.copyLocation(getLocation(), arthemisEntity.getComponent(LocationComponent.class).getPreviousLocation());
        arthemisEntity.getComponent(MetadataComponent.class).getMetadata().resetChanges();
        arthemisEntity.getComponent(LocationComponent.class).setTeleported(false);
        arthemisEntity.getComponent(VelocityComponent.class).setVelocityChanged(false);
    }

    /**
     * Sets this entity's location.
     * @param location The new location.
     */
    public void setRawLocation(Location location) {
        if (location.getWorld() != world) {
            throw new IllegalArgumentException(
                    "Cannot setRawLocation to a different world (got " + location.getWorld() + ", expected " + world + ")");
        }
        world.getEntityManager().move(this, location);
        Position.copyLocation(location, getLocation());
    }

    /**
     * Creates a {@link Message} which can be sent to a client to spawn this
     * entity.
     * @return A message which can spawn this entity.
     */
    public abstract List<Message> createSpawnMessage();

    /**
     * Creates a {@link Message} which can be sent to a client to update this
     * entity.
     * @return A message which can update this entity.
     */
    public List<Message> createUpdateMessage() {
        boolean moved = hasMoved();
        boolean rotated = hasRotated();

        LocationComponent locationComponent = arthemisEntity.getComponent(LocationComponent.class);
        int x = Position.getIntX(locationComponent.getLocation());
        int y = Position.getIntY(locationComponent.getLocation());
        int z = Position.getIntZ(locationComponent.getLocation());

        int dx = x - Position.getIntX(locationComponent.getPreviousLocation());
        int dy = y - Position.getIntY(locationComponent.getPreviousLocation());
        int dz = z - Position.getIntZ(locationComponent.getPreviousLocation());

        boolean
                teleport =
                dx > Byte.MAX_VALUE || dy > Byte.MAX_VALUE || dz > Byte.MAX_VALUE || dx < Byte.MIN_VALUE || dy < Byte.MIN_VALUE
                || dz < Byte.MIN_VALUE;

        int yaw = Position.getIntYaw(locationComponent.getLocation());
        int pitch = Position.getIntPitch(locationComponent.getLocation());

        List<Message> result = new LinkedList<>();
        if (locationComponent.isTeleported() || (moved && teleport)) {
            result.add(new EntityTeleportMessage(id, x, y, z, yaw, pitch));
        } else if (moved && rotated) {
            result.add(new RelativeEntityPositionRotationMessage(id, dx, dy, dz, yaw, pitch));
        } else if (moved) {
            result.add(new RelativeEntityPositionMessage(id, dx, dy, dz));
        } else if (rotated) {
            result.add(new EntityRotationMessage(id, yaw, pitch));
        }

        // todo: handle head rotation as a separate value
        if (rotated) {
            result.add(new EntityHeadRotationMessage(id, yaw));
        }

        // send changed metadata
        List<MetadataMap.Entry> changes = arthemisEntity.getComponent(MetadataComponent.class).getMetadata().getChanges();
        if (changes.size() > 0) {
            result.add(new EntityMetadataMessage(id, changes));
        }

        // send velocity if needed
        if (arthemisEntity.getComponent(VelocityComponent.class).isVelocityChanged()) {
            result.add(new EntityVelocityMessage(id, arthemisEntity.getComponent(VelocityComponent.class).getVelocity()));
        }

        return result;
    }

    /**
     * Checks if this entity has moved this cycle.
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean hasMoved() {
        LocationComponent locationComponent = arthemisEntity.getComponent(LocationComponent.class);
        return Position.hasMoved(locationComponent.getLocation(), locationComponent.getPreviousLocation());
    }

    /**
     * Checks if this entity has rotated this cycle.
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean hasRotated() {
        LocationComponent locationComponent = arthemisEntity.getComponent(LocationComponent.class);
        return Position.hasRotated(locationComponent.getLocation(), locationComponent.getPreviousLocation());
    }

    protected final void setBoundingBox(double xz, double y) {
        boundingBox = new EntityBoundingBox(xz, y);
    }

    /**
     * Teleport this entity to the spawn point of the main world.
     * This is used to teleport out of the End.
     * @return {@code true} if the teleport was successful.
     */
    protected boolean teleportToSpawn() {
        Location target = server.getWorlds().get(0).getSpawnLocation();

        EntityPortalEvent event = EventFactory.callEvent(new EntityPortalEvent(this, getLocation().clone(), target, null));
        if (event.isCancelled()) {
            return false;
        }
        target = event.getTo();

        teleport(target);
        return true;
    }

    /**
     * Teleport this entity to the End.
     * If no End world is loaded this does nothing.
     * @return {@code true} if the teleport was successful.
     */
    protected boolean teleportToEnd() {
        if (!server.getAllowEnd()) {
            return false;
        }
        Location target = null;
        for (World world : server.getWorlds()) {
            if (world.getEnvironment() == World.Environment.THE_END) {
                target = world.getSpawnLocation();
                break;
            }
        }
        if (target == null) {
            return false;
        }

        EntityPortalEvent event = EventFactory.callEvent(new EntityPortalEvent(this, getLocation().clone(), target, null));
        if (event.isCancelled()) {
            return false;
        }
        target = event.getTo();

        teleport(target);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Physics stuff

    public boolean intersects(BoundingBox box) {
        return boundingBox != null && boundingBox.intersects(box);
    }

    protected void pulsePhysics() {
        // todo: update location based on velocity,
        // do gravity, all that other good stuff

        // make sure bounding box is up to date
        if (boundingBox != null) {
            boundingBox.setCenter(getLocation().getX(), getLocation().getY(), getLocation().getZ());
        }
    }

    @Override
    public int getFireTicks() {
        return arthemisEntity.getComponent(FireComponent.class).getFireTicks();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Various properties

    @Override
    public void setFireTicks(int ticks) {
        arthemisEntity.getComponent(FireComponent.class).setFireTicks(ticks);
    }

    @Override
    public int getMaxFireTicks() {
        return 160;  // this appears to be Minecraft's default value
    }

    @Override
    public float getFallDistance() {
        return arthemisEntity.getComponent(FallGroundComponent.class).getFallDistance();
    }

    @Override
    public void setFallDistance(float distance) {
        arthemisEntity.getComponent(FallGroundComponent.class).setFallDistance(Math.max(distance, 0));
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return lastDamageCause;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageCause = event;
    }

    @Override
    public int getTicksLived() {
        return arthemisEntity.getComponent(LifeComponent.class).getTicksLived();
    }

    @Override
    public void setTicksLived(int value) {
        arthemisEntity.getComponent(LifeComponent.class).setTicksLived(value);
    }

    @Override
    public boolean isOnGround() {
        return arthemisEntity.getComponent(FallGroundComponent.class).isOnGround();
    }

    public void setOnGround(boolean onGround) {
        arthemisEntity.getComponent(FallGroundComponent.class).setOnGround(onGround);
    }

    protected void setSize(float xz, float y) {
        //todo Size stuff with bounding boxes.
    }

    @Override
    public void remove() {
        active = false;
        world.getEntityManager().deallocate(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Miscellaneous actions

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        // This behavior is similar to CraftBukkit, where a call with args
        // (0, 0, 0) finds any entities whose bounding boxes intersect that of
        // this entity.

        BoundingBox searchBox;
        if (boundingBox == null) {
            searchBox = BoundingBox.fromPositionAndSize(getLocation().toVector(), new Vector(0, 0, 0));
        } else {
            searchBox = BoundingBox.copyOf(boundingBox);
        }
        Vector vec = new Vector(x, y, z);
        searchBox.minCorner.subtract(vec);
        searchBox.maxCorner.add(vec);

        return world.getEntityManager().getEntitiesInside(searchBox, this);
    }

    @Override
    public void playEffect(EntityEffect type) {
        // TODO Implement
    }

    @Override
    public boolean isInsideVehicle() {
        return getVehicle() != null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Entity stacking

    @Override
    public boolean leaveVehicle() {
        return false;
    }

    @Override
    public Entity getVehicle() {
        return null;
    }

    @Override
    public Entity getPassenger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty() {
        return getPassenger() == null;
    }

    @Override
    public boolean eject() {
        return !isEmpty() && setPassenger(null);
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        MetadataComponent.bukkitMetadata.setMetadata(this, metadataKey, newMetadataValue);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Metadata

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return MetadataComponent.bukkitMetadata.getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return MetadataComponent.bukkitMetadata.hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        MetadataComponent.bukkitMetadata.removeMetadata(this, metadataKey, owningPlugin);
    }
}
