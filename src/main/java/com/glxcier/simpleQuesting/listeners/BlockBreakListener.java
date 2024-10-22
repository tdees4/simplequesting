package com.glxcier.simpleQuesting.listeners;

import com.glxcier.simpleQuesting.quests.BaseQuest;
import com.glxcier.simpleQuesting.quests.QuestType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Set;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player breakingPlayer = e.getPlayer();

        Set<BaseQuest<?>> playerBreakQuests = BaseQuest.getPlayerQuestsOfType(breakingPlayer.getUniqueId(), QuestType.MINE);

        Material blockBroken = e.getBlock().getType();

        ListenerUtils.addProgressToQuests(playerBreakQuests, blockBroken, 1);
    }

}
