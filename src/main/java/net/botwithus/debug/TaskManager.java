package net.botwithus.debug;

import net.botwithus.api.game.hud.Dialog;
import net.botwithus.api.game.hud.inventories.Backpack;

import java.util.List;

public class TaskManager {

        private final List<Task> tasks;
    public enum GameState {
        DIALOG_OPEN,
        BACKPACK_FULL,
        BACKPACK_NOT_FULL,
        DEFAULT
    }
    private final MainScript mainScript;

        public TaskManager(List<Task> tasks, MainScript mainScript) {
            this.tasks = tasks;
            this.mainScript = mainScript;

        }

    public void runTasks() {
        for (Task task : tasks) {
            try {
                GameState gameState = getGameState();

                switch (gameState) {
                    case DIALOG_OPEN, DEFAULT, BACKPACK_FULL:
                        switchTo(new InventoryManagementTask(mainScript));
                        break;
                    case BACKPACK_NOT_FULL:
                        switchTo(new HarvestWispTask(mainScript));
                        break;

                }
                if (task.validate()) {
                    task.perform();
                }
            } catch (Exception e) {
                System.out.println("Exception occurred while executing task: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private GameState getGameState() {
        if (Dialog.isOpen()) {
            return GameState.DIALOG_OPEN;
        }
        if (Backpack.isFull()) {
            return GameState.BACKPACK_FULL;
        }
        if (!Backpack.isFull()) {
            return GameState.BACKPACK_NOT_FULL;
        }

        return GameState.DEFAULT;
    }

    public void switchTo(Task task) {
        tasks.clear();
        tasks.add(task);
    }

    public interface Task {
        boolean validate();
        void perform();
    }


}
