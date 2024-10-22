package com.glxcier.simpleQuesting.quests;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum QuestType {
    SLAY(EntityType.class),
    PLACE(Material.class),
    MINE(Material.class),
    CRAFT(Material.class);

    /* The type/class of target this QuestType targets. */
    private final Class<?> clazz;

    private QuestType(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * @return the class/type of target this QuestType is associated with.
     */
    public Class<?> getTargetClass() {
        return clazz;
    }
}
