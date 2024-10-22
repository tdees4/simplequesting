package com.glxcier.simpleQuesting.listeners;

import com.glxcier.simpleQuesting.quests.BaseQuest;
import com.glxcier.simpleQuesting.quests.QuestType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player placingPlayer = e.getPlayer();

        Set<BaseQuest<?>> playerPlaceQuests = BaseQuest.getPlayerQuestsOfType(placingPlayer.getUniqueId(), QuestType.PLACE);

        Material blockPlaced = e.getBlockPlaced().getType();

        ListenerUtils.addProgressToQuests(playerPlaceQuests, blockPlaced, 1);
    }

}
