package net.botwithus.debug;

import net.botwithus.api.game.script.treescript.LeafTask;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.script.Execution;

public class HarvestButterflyLeaf extends LeafTask {

        private DebugScript debugScript;
    private Npc currentWisp = null;

    public HarvestButterflyLeaf(DebugScript script) {
            super(script);
            this.debugScript = script;
        }

        @Override
        public void execute() {
            debugScript.println("Checking for Butterfly");
            EntityResultSet<Npc> butterfly = NpcQuery.newQuery().name("Guthixian butterfly").results();
            currentWisp = butterfly.nearest();
            if (currentWisp != null) {
                debugScript.println("Found Guthixian butterfly, interacting");
                butterfly.nearest().interact("Capture");
                debugScript.totalCaughtButterflies++;
                Execution.delayWhile(2000, () -> NpcQuery.newQuery().name("Guthixian butterfly").results().nearest() != null);
            }
        }
}
