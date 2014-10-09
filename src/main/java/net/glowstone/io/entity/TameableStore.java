package net.glowstone.io.entity;

import java.util.UUID;

import net.glowstone.entity.passive.GlowTameable;
import net.glowstone.util.nbt.CompoundTag;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public abstract class TameableStore<T extends GlowTameable> extends AgeableStore<T>
{
    public TameableStore(Class<T> clazz, String id)
    {
        super(clazz, id);
    }

    @Override
    public void load(T entity, CompoundTag compound)
    {
        // TODO make this better.
        super.load(entity, compound);
        if (compound.containsKey("OwnerUUID")) {
            entity.setOwnerUUID(UUID.fromString(compound.getString("OwnerUUID")));
            if (Bukkit.getPlayer(entity.getOwnerUUID()) != null) {
                entity.setOwner(Bukkit.getPlayer(entity.getOwnerUUID()));
            }
        } else {
            String playerName = compound.getString("Owner");
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            if (player.hasPlayedBefore()) {
                entity.setOwnerUUID(player.getUniqueId());
                if (Bukkit.getPlayer(entity.getOwnerUUID()) != null) {
                    entity.setOwner(Bukkit.getPlayer(entity.getOwnerUUID()));
                }
            }
        }
    }

    @Override
    public void save(T entity, CompoundTag tag)
    {
        super.save(entity, tag);
        if (entity.getOwner() == null)
        {
            tag.putString("OwnerUUID", "");
        } else
        {
            tag.putString("OwnerUUID", entity.getOwner().getUniqueId().toString());
        }
    }
}
