package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.script.treescript.BranchTask;
import net.botwithus.api.game.script.treescript.LeafTask;
import net.botwithus.api.game.script.treescript.TreeTask;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.Entity;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.script.Execution;


import net.botwithus.rs3.util.RandomGenerator;

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
        return !Backpack.isFull() || getLocalPlayer().distanceTo(currentWisp) > 2.5 || !currentWisp.validate() || getLocalPlayer().getAnimationId() == -1;
    }

    @Override
    public void perform() {
        // Check for chronicle
       // String selectedWispName = ((DebugGraphicsContext)debugScript.getSgc()).wispNames[((DebugGraphicsContext)debugScript.getSgc()).selectedWispIndex.get()];
       // debugScript.println("Selected wisp: " + selectedWispName);
        if(debugScript.Enriched) {
            debugScript.println("Checking for Chronicle fragment");
            EntityResultSet<Npc> chronicle = NpcQuery.newQuery().name("Chronicle fragment").results();
            currentWisp = chronicle.nearest();
            if (currentWisp != null) {

                Execution.delayWhile(RandomGenerator.nextInt(1500, 3000), () -> NpcQuery.newQuery().name("Chronicle fragment").results().nearest() != null);
                debugScript.println("Found Chronicle fragment, interacting");
                chronicle.nearest().interact("Capture");
                debugScript.totalCaughtChronicles++;
            }
        }
        //check for butterflies
        if(debugScript.Butterfly) {
            debugScript.println("Checking for Butterfly");
            EntityResultSet<Npc> butterfly = NpcQuery.newQuery().name("Guthixian butterfly").results();
            currentWisp = butterfly.nearest();
            if (currentWisp != null) {
                Execution.delayWhile(RandomGenerator.nextInt(1500, 3000), () -> NpcQuery.newQuery().name("Guthixian butterfly").results().nearest() != null);
                debugScript.println("Found Guthixian butterfly, interacting");
                butterfly.nearest().interact("Capture");
                debugScript.totalCaughtButterflies++;
            }
        }

        if(debugScript.serenSpirits)
        {
            debugScript.println("Checking for Seren spirit");
            EntityResultSet<Npc> serenSpirit = NpcQuery.newQuery().name("Seren spirit").results();
            currentWisp = serenSpirit.nearest();
            if (currentWisp != null) {
                debugScript.println("Found Seren spirit, interacting");
                serenSpirit.nearest().interact("Capture");
                Execution.delayWhile(RandomGenerator.nextInt(1500, 3000), () -> NpcQuery.newQuery().name("Seren spirit").results().nearest() != null);
            }
        }

        // Check for enriched wisps
       // debugScript.println("Checking for Enriched wisps");
        EntityResultSet<Npc> enrichedWisps = NpcQuery.newQuery().name("Enriched ", String::contains).results();
        currentWisp = enrichedWisps.nearest();
        if (currentWisp != null) {
            if (shouldInteract()) return;
            debugScript.println("Found Enriched wisp, harvesting");
            harvestWisp(currentWisp);
            return;
        }

        // Check for springs
        //debugScript.println("Checking for springs");
        EntityResultSet<Npc> springs = NpcQuery.newQuery().name("spring", String::contains).results();
        currentWisp = springs.nearest();
        if (currentWisp != null) {
            if (shouldInteract()) return;
            debugScript.println("Found spring, harvesting");

            harvestWisp(currentWisp);
            return;
        }

        // Check for regular wisps
        //debugScript.println("Checking for regular wisps");
        EntityResultSet<Npc> wisps = NpcQuery.newQuery().name("wisp", String::contains).results();
        currentWisp = wisps.nearest();
        if (currentWisp != null) {
            if (shouldInteract()) return;
            debugScript.println("Found regular wisp, harvesting");
            harvestWisp(currentWisp);
            return;
        }
    }

    public boolean shouldInteract() {
        boolean shouldinteract = Execution.delayUntil(RandomGenerator.nextInt(2000, 4500), () -> Backpack.isFull() || getLocalPlayer().distanceTo(currentWisp) > 2.5 || !currentWisp.validate() || getLocalPlayer().getAnimationId() == -1);
        if(!shouldinteract) {
            debugScript.println("Not interacting with wisp Didnt meet conditions");
            return true;
        }
        return false;
    }

    private void harvestWisp(Npc wisp) {
        //debugScript.println("Harvesting wisp");
        Execution.delay(RandomGenerator.nextInt(500, 1500));
        wisp.interact("Harvest");
        debugScript.println("clicked wisp");
    }

}