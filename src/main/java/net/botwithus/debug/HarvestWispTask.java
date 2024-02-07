package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.script.treescript.BranchTask;
import net.botwithus.api.game.script.treescript.LeafTask;
import net.botwithus.api.game.script.treescript.Permissive;
import net.botwithus.api.game.script.treescript.TreeTask;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.script.Execution;

import static net.botwithus.rs3.game.Client.getLocalPlayer;

public class HarvestWispTask implements TaskManager.Task {
    private Npc currentWisp = null;


    public DebugScript debugScript;

    public HarvestWispTask(DebugScript debugScript) {
        this.debugScript = debugScript;
    }

    @Override
    public boolean validate() {
        debugScript.println("Validating");
        return !Backpack.isFull();
    }

    @Override
    public void perform() {
        // Check for chronicle
        String selectedWispName = ((DebugGraphicsContext)debugScript.getSgc()).wispNames[((DebugGraphicsContext)debugScript.getSgc()).selectedWispIndex.get()];
        debugScript.println("Selected wisp: " + selectedWispName);
        if(debugScript.Enriched) {
            debugScript.println("Checking for Chronicle fragment");
            EntityResultSet<Npc> chronicle = NpcQuery.newQuery().name("Chronicle fragment").results();
            currentWisp = chronicle.nearest();
            if (currentWisp != null) {
                debugScript.println("Found Chronicle fragment, interacting");
                chronicle.nearest().interact("Capture");
                debugScript.totalCaughtChronicles++;
                Execution.delayWhile(2000, () -> NpcQuery.newQuery().name("Chronicle fragment").results().nearest() != null);
            }
        }

        if(debugScript.Butterfly) {
            debugScript.println("Checking for Butterfly");
            EntityResultSet<Npc> butterfly = NpcQuery.newQuery().name("Guthixian butterfly").results();
            currentWisp = butterfly.nearest();
            if (currentWisp != null) {
                debugScript.println("Found Guthixian butterfly, interacting");
                butterfly.nearest().interact("Capture");
                //add total caught butterflies
                debugScript.totalCaughtButterflies++;
                Execution.delayWhile(2000, () -> NpcQuery.newQuery().name("Guthixian butterfly").results().nearest() != null);
            }
        }

        // Check for enriched wisps
        debugScript.println("Checking for Enriched wisps");
        EntityResultSet<Npc> enrichedWisps = NpcQuery.newQuery().name("Enriched ", String::contains).results();
        currentWisp = enrichedWisps.nearest();
        if (currentWisp != null) {
            debugScript.println("Found Enriched wisp, harvesting");
            harvestWisp(currentWisp);
            return;
        }

        // Check for springs
        debugScript.println("Checking for springs");
        EntityResultSet<Npc> springs = NpcQuery.newQuery().name("spring", String::contains).results();
        currentWisp = springs.nearest();
        if (currentWisp != null) {
            debugScript.println("Found spring, harvesting");
            harvestWisp(currentWisp);
            return;
        }

        // Check for regular wisps
        debugScript.println("Checking for regular wisps");
        EntityResultSet<Npc> wisps = NpcQuery.newQuery().name("wisp", String::contains).results();
        currentWisp = wisps.nearest();
        if (currentWisp != null) {
            debugScript.println("Found regular wisp, harvesting");
            harvestWisp(currentWisp);
            return;
        }
    }

    private void harvestWisp(Npc wisp) {
        debugScript.println("Harvesting wisp");
        wisp.interact("Harvest");
        Execution.delayWhile(10000, () -> !Backpack.isFull() || getLocalPlayer().distanceTo(wisp) > 2.5);
    }

//    private void harvestWisp(Npc wisp) {
//        debugScript.println("Harvesting wisp");
//        wisp.interact("Harvest");
//        Execution.delayWhile(10000, () -> {
//            // Check for chronicle
//            debugScript.println("Checking for Chronicle fragment");
//            EntityResultSet<Npc> chronicle = NpcQuery.newQuery().name("Chronicle fragment").results();
//            Npc currentChronicle = chronicle.nearest();
//            if (currentChronicle != null) {
//                debugScript.println("Found Chronicle fragment, interacting");
//                currentChronicle.interact("Take");
//                Execution.delayWhile(10000, () -> NpcQuery.newQuery().name("Chronicle fragment").results().nearest() != null);
//            }
//            // Continue delaying while the backpack is not full
//            return !Backpack.isFull();
//        });
//    }
}