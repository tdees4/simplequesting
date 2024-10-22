package com.glxcier.simpleQuesting.listeners;

import com.glxcier.simpleQuesting.quests.BaseQuest;
import com.glxcier.simpleQuesting.quests.QuestType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Set;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity damagingEntity = e.getDamageSource().getCausingEntity();

        if (!(damagingEntity instanceof Player damagingPlayer)) {
            return;
        }

        EntityType damagedEntity = e.getEntity().getType();

        Set<BaseQuest<?>> playerSlayQuests = BaseQuest.getPlayerQuestsOfType(damagingPlayer.getUniqueId(), QuestType.SLAY);

        ListenerUtils.addProgressToQuests(playerSlayQuests, damagedEntity, 1);
    }

}
