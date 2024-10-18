package com.glxcier.simpleQuesting.quests;

import java.util.*;

/**
 * The base class that represents all quests.
 * Each quest has one instance in the quests set. Static methods may be used to get these
 * instances and use them.
 */
public class BaseQuest<TargetType> {

    /* A list of all possible quests. */
    private static Set<BaseQuest> quests = new HashSet<>();

    /* Tracks all quests that each player has. */
    private static Map<UUID, Set<BaseQuest>> playerQuestMap = new HashMap<>();

    /* Name of the quest. (NON-CASE SENSITIVE) (MUST BE UNIQUE) */
    private String name;

    /* Description of the quest. */
    private String description;

    /* True if the quest is active, false if not. */
    private boolean active;

    /* True if the quest is completed, false if not. */
    private boolean completed;

    /* The goal of the quest "activity". */
    private final int questGoal;

    /* The quest's progress towards the goal. */
    private int questProgress;

    /* The quest's type. */
    private final QuestType type;

    protected BaseQuest(QuestType type, String name, String description, int questGoal) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.questGoal = questGoal;
        completed = false;
        active = false;
    }

    /**
     * @param completed Whether the quest is completed or not.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * @param active Whether the quest is active or not.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @param description The new description of the quest.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the progress of the quest to {@code progress}.
     * @param progress The value to set the quest's progress to.
     * @throws IllegalArgumentException if {@code progress} is less than 0.
     */
    public void setQuestProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress cannot be less than 0.");
        }

        if (progress >= questGoal) {
            // TODO: Quest completion logic.
        } else {
            questProgress = progress;
        }
    }

    /**
     * @return true if the quest is completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @return true if the quest is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return the goal of the quest.
     */
    public int getQuestGoal() {
        return questGoal;
    }

    /**
     * @return the progress towards the goal.
     */
    public int getQuestProgress() {
        return questProgress;
    }

    /**
     * @return the type of the quest.
     */
    public QuestType getType() {
        return type;
    }

    /**
     * @return the description of the quest.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the unique name of the quest.
     */
    public String getName() {
        return name;
    }

    /**
     * Assigns the quest with the name {@code name} to the player with the UUID {@code uuid}.
     * @param uuid The UUID of the player.
     * @param name The name of the quest.
     * @throws NullPointerException if {@code uuid} or {@code name} are null.
     * @throws IllegalArgumentException if no quest with the name {@code name} exists.
     */
    public static void assignQuestToPlayer(UUID uuid, String name) {
        if (uuid == null) {
            throw new NullPointerException("Cannot pass a null uuid.");
        }
        if (name == null) {
            throw new NullPointerException("Cannot pass a null name.");
        }

        BaseQuest quest = getQuest(name);

        if (quest == null) {
            throw new IllegalArgumentException("Must pass a valid quest name.");
        }

        if (!playerQuestMap.containsKey(uuid) || playerQuestMap.get(uuid) == null) {
            playerQuestMap.put(uuid, new HashSet<>());
        }

        playerQuestMap.get(uuid).add(quest);
    }

    /**
     * Returns the quest with the name {@code name}.
     * @param name The name of the quest to fetch.
     * @return the quest with the name {@code name} or null if no such quest exists.
     */
    public static BaseQuest getQuest(String name) {
        for (BaseQuest quest : quests) {
            if (quest.getName().equalsIgnoreCase(name)) {
                return quest;
            }
        }
        return null;
    }

    /**
     * @param name The name of the quest to search for.
     * @return true if the quest with the name {@code name} exists, false otherwise.
     */
    public static boolean hasQuest(String name) {
        BaseQuest dummy = new BaseQuest(null, name, null, 0);
        return quests.contains(dummy);
    }

    /**
     * Creates a new quest and adds it to the quest set.
     * @param type The type of quest.
     * @param name The unique name of the quest.
     * @param description The description of the quest.
     * @param questGoal The goal of the quest.
     * @throws IllegalArgumentException if a quest already has the name {@code name} or if {@code questGoal} is less than or equal to 0.
     * @throws NullPointerException if {@code type}, {@code name}, or {@code description} are null.
     */
    public static void createNewQuest(QuestType type, String name, String description, int questGoal) {
        if (name == null || type == null || description == null) {
            throw new NullPointerException("Cannot pass a null parameter.");
        }
        if (questGoal <= 0) {
            throw new IllegalArgumentException("questGoal cannot be less than or equal to 0.");
        }

        BaseQuest quest = new BaseQuest(type, name, description, questGoal);

        if (quests.contains(quest)) {
            throw new IllegalArgumentException("Must provide a unique quest name.");
        }

        quests.add(quest);
    }

    /**
     * Loads all quests into the static quest list {@code quests}.
     */
    public static void loadAllQuests() {
        // TODO: Implementation of quest loading.
    }

    /**
     * @param other The other BaseQuest object.
     * @return true if {@code other} has the same name as this quest.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BaseQuest qOther)) {
            return false;
        }
        return qOther.getName().equalsIgnoreCase(getName());
    }

    /**
     * @return the hashcode of the quest's name.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
