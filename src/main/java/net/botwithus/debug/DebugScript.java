package net.botwithus.debug;

import net.botwithus.internal.plugins.ScriptDefinition;
import net.botwithus.rs3.entities.pathing.player.Player;
import net.botwithus.rs3.queries.Queries;
import net.botwithus.rs3.script.Script;

public class DebugScript extends Script {
    public DebugScript(String name, ScriptDefinition plugin) {
        super(name, plugin);
    }

    @Override
    public void initialize() {
        super.initialize();
        setActive(false);
        this.sgc = new DebugGraphicsContext();
    }

    @Override
    public void onLoop() {
        Player self = Queries.self();

        System.out.println(self.getPosition());

        delay(3000);
    }
}
