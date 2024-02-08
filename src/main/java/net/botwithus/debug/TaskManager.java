package net.botwithus.debug;

import net.botwithus.api.game.hud.Dialog;
import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.world.Traverse;
import net.botwithus.rs3.game.Area;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.cs2.ScriptBuilder;
import net.botwithus.rs3.game.cs2.layouts.StringLayout;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.input.KeyboardInput;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.Script;

import java.util.List;

public class TaskManager {

        private final List<Task> tasks;
    public enum GameState {
        DIALOG_OPEN,
        BACKPACK_FULL,
        BACKPACK_NOT_FULL,
        DEFAULT
    }
    private final DebugScript debugScript;

        public TaskManager(List<Task> tasks, DebugScript debugScript) {
            this.tasks = tasks;
            this.debugScript = debugScript;

        }

    public void runTasks() {
        for (Task task : tasks) {
            try {
                GameState gameState = getGameState();

                switch (gameState) {
                    case DIALOG_OPEN, DEFAULT, BACKPACK_FULL:
                        switchTo(new InventoryManagementTask(debugScript));
                        break;
                    case BACKPACK_NOT_FULL:
                        switchTo(new HarvestWispTask(debugScript));
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
