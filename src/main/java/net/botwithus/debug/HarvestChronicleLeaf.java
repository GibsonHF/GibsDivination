package net.botwithus.debug;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.script.treescript.LeafTask;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.Script;

public class HarvestChronicleLeaf extends LeafTask {

    private Npc currentWisp = null;

    private DebugScript debugScript;

    public HarvestChronicleLeaf(DebugScript script) {
        super(script);
        this.debugScript = script;
    }

    @Override
    public void execute() {
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
}
