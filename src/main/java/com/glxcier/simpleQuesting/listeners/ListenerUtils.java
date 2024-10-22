package com.glxcier.simpleQuesting.listeners;

import com.glxcier.simpleQuesting.quests.BaseQuest;

import java.util.Set;

public class ListenerUtils {

    public static <T> void addProgressToQuests(Set<BaseQuest<?>> quests, T target, int addToProgress) {
        for (BaseQuest<?> quest : quests) {
            if (!quest.isActive()) {
                return;
            }
            BaseQuest<T> typedQuest = (BaseQuest<T>) quest;
            if (typedQuest.getTargets().contains(target)) {
                typedQuest.setQuestProgress(typedQuest.getQuestProgress() + addToProgress);
            }
        }
    }

}
