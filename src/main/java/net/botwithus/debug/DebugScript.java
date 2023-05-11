package net.botwithus.debug;

import net.botwithus.internal.plugins.ScriptDefinition;
import net.botwithus.rs3.entities.pathing.npc.Npc;
import net.botwithus.rs3.entities.pathing.player.Player;
import net.botwithus.rs3.interfaces.Component;
import net.botwithus.rs3.queries.EntityResultSet;
import net.botwithus.rs3.queries.Queries;
import net.botwithus.rs3.queries.ResultSet;
import net.botwithus.rs3.queries.builders.SceneObjectQuery;
import net.botwithus.rs3.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.queries.builders.npc.NpcQuery;
import net.botwithus.rs3.script.Script;
import net.botwithus.rs3.script.ScriptGraphicsContext;

import java.util.regex.Pattern;

public class DebugScript extends Script {
    public DebugScript(String name, ScriptDefinition plugin) {
        super(name, plugin);
    }

    @Override
    public void initialize() {
        super.initialize();
        setActive(false);
    }

    @Override
    public void onLoop() {
        Player self = Queries.self();

        System.out.println(self.getPosition());

        delay(3000);
    }

    @Override
    public ScriptGraphicsContext createGraphicsContext() {
        this.sgc = new DebugGraphicsContext();
        return this.sgc;
    }
}
