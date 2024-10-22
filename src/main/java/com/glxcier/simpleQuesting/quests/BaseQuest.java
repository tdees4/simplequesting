package com.glxcier.simpleQuesting.quests;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The base class that represents all quests.
 * Each quest has one instance in the quests set. Static methods may be used to get these
 * instances and use them.
 */
public class BaseQuest<TargetType> {

    /* A list of all possible quests. */
    private static Set<BaseQuest> quests = new HashSet<>();

    /* Tracks all quests that each player has. */
    private static Map<UUID, Set<BaseQuest<?>>> playerQuestMap = new HashMap<>();

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

    /* The targets of the quest. (EX: Slay quest target could be zombie and creeper) */
    private final Set<TargetType> targets;

    /* Functional interface that represents what will happen when a user complete the quest. */
    private CompleteQuestAction completeQuestAction;

    /**
     * Functional interface that defines what will happen when the quest is
     * completed.
     */
    public interface CompleteQuestAction {
        public abstract void completeQuest();
    }

    private BaseQuest(QuestType type, String name, String description, int questGoal, Set<TargetType> targets, CompleteQuestAction completeQuestAction) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.questGoal = questGoal;
        this.targets = targets;
        this.completeQuestAction = completeQuestAction;
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
     * @throws IllegalStateException if the quest has already been completed.
     */
    public void setQuestProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress cannot be less than 0.");
        }
        if (completed) {
            throw new IllegalStateException("Quest has already been completed.");
        }

        if (progress >= questGoal) {
            completed = true;
            active = false;
            completeQuestAction.completeQuest();
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
     * @return the list of targets the quest is tracked against.
     */
    public Set<TargetType> getTargets() {
        return targets;
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

        BaseQuest<?> quest = getQuest(name);

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
    public static BaseQuest<?> getQuest(String name) {
        for (BaseQuest<?> quest : quests) {
            if (quest.getName().equalsIgnoreCase(name)) {
                return quest;
            }
        }
        return null;
    }

    /**
     * Returns the quests owned by the player with UUID {@code uuid}.
     * @param uuid The UUID associated with the player.
     * @return a set of quests owned by the player.
     */
    public static Set<BaseQuest<?>> getPlayerQuests(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("Cannot pass a null uuid.");
        }
        return playerQuestMap.get(uuid);
    }

    /**
     * Returns a set of quests with the type {@code type} that are owned by the
     * player with UUID {@code uuid}.
     * @param uuid The UUID of the player.
     * @param type The type of quests to filter for.
     * @return a set of quests owned by the player with the UUID {@code uuid} and of type {@code type}.
     * @throws IllegalArgumentException if {@code uuid} or {@code type} are null.
     */
    public static Set<BaseQuest<?>> getPlayerQuestsOfType(UUID uuid, QuestType type) {
        if (uuid == null || type == null) {
            throw new IllegalArgumentException("Cannot pass a null parameter.");
        }

        Set<BaseQuest<?>> playerQuests = playerQuestMap.get(uuid);
        if (playerQuests.isEmpty()) {
            return new HashSet<>();
        }
        return playerQuests.stream()
                .filter(quest -> quest.getType() == type)
                .collect(Collectors.toSet());
    }

    /**
     * @param name The name of the quest to search for.
     * @return true if the quest with the name {@code name} exists, false otherwise.
     */
    public static boolean hasQuest(String name) {
        BaseQuest<?> dummy = new BaseQuest<>(null, name, null, 0, null, null);
        return quests.contains(dummy);
    }

    /**
     * Creates a new quest and adds it to the quest set.
     * @param type The type of quest.
     * @param name The unique name of the quest.
     * @param description The description of the quest.
     * @param questGoal The goal of the quest.
     * @param targets List of targets the quest is tracked against.
     * @param completeQuestAction The code that runs when the quest is completed.
     * @param <TargetType> The type of the target. (EX: EntityType)
     * @throws IllegalArgumentException if a quest already has the name {@code name} or if {@code questGoal} is less than or equal to 0.
     * @throws NullPointerException if {@code type}, {@code name}, {@code targets}, {@code completeQuestAction}, or {@code description} are null.
     */
    public static <TargetType> void createNewQuest(QuestType type, String name, String description, int questGoal,
                                                   Set<TargetType> targets, CompleteQuestAction completeQuestAction) {
        if (name == null || type == null || description == null || targets == null || completeQuestAction == null) {
            throw new NullPointerException("Cannot pass a null parameter.");
        }
        if (questGoal <= 0) {
            throw new IllegalArgumentException("questGoal cannot be less than or equal to 0.");
        }
        if (targets.isEmpty()) {
            throw new IllegalArgumentException("targets cannot be empty.");
        }
        if (!(targets.iterator().next() instanceof TargetType)) {
            throw new IllegalArgumentException("targets must be a TargetType.");
        }

        BaseQuest<TargetType> quest = new BaseQuest<>(type, name, description, questGoal, targets, completeQuestAction);

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
        if (!(other instanceof BaseQuest<?> qOther)) {
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
