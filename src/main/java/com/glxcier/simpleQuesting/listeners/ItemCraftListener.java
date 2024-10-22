package com.glxcier.simpleQuesting.listeners;

import com.glxcier.simpleQuesting.quests.BaseQuest;
import com.glxcier.simpleQuesting.quests.QuestType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.Set;

public class ItemCraftListener implements Listener {

    @EventHandler
    public void onItemCraft(CraftItemEvent e) {
        Player craftingPlayer = (Player) e.getView().getPlayer();

        Material itemCrafted = e.getRecipe().getResult().getType();

        Set<BaseQuest<?>> playerCraftQuests = BaseQuest.getPlayerQuestsOfType(craftingPlayer.getUniqueId(), QuestType.CRAFT);

        ListenerUtils.addProgressToQuests(playerCraftQuests, itemCrafted, 1);
    }

}
