package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;

import java.util.Arrays;
import java.util.Objects;

public class InventoryManagementTask implements TaskManager.Task {

    private MainScript mainScript;

    public InventoryManagementTask(MainScript mainScript) {
        this.mainScript = mainScript;
    }

    @Override
    public boolean validate() {
        mainScript.println("Validating InventoryManagementTask");
        return Backpack.isFull() || Backpack.contains(29384, 29385, 29386, 29387, 29388, 29389, 29390, 29391, 29392, 29393, 29394, 29395, 29396, 29397, 29398, 29399, 29400, 29401, 29402, 29403, 29404, 29405, 29406);
    }

    @Override
    public void perform() {
        mainScript.println("Performing InventoryManagementTask");
        SceneObject rift = findRift();

        if (rift != null) {
            mainScript.println("Found Energy rift");
            rift.interact("Convert memories");
            Execution.delayWhile(60000, () -> Backpack.contains(29384, 29385, 29386, 29387, 29388, 29389, 29390, 29391, 29392, 29393, 29394, 29395, 29396, 29397, 29398, 29399, 29400, 29401, 29402, 29403, 29404, 29405, 29406));
        } else {
            mainScript.println("Energy rift not found");
        }
    }

    public SceneObject findRift() {
        int[] riftIds = {93489, 87306};
        mainScript.println("Searching for rifts with IDs: " + Arrays.toString(riftIds));
        SceneObject rift = Arrays.stream(riftIds)
                .mapToObj(id -> {
                    SceneObject result = SceneObjectQuery.newQuery().id(id).results().nearest();
                    mainScript.println("Rift with ID " + id + (result != null ? " found" : " not found"));
                    return result;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (rift == null) {
            mainScript.println("No rift found");
        } else {
            mainScript.println("Rift found with ID: " + rift.getId());
        }
        return rift;
    }

}