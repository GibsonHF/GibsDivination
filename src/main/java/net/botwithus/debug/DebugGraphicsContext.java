package net.botwithus.debug;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class DebugGraphicsContext extends ScriptGraphicsContext {

    @Override
    public void drawSettings() {
        ImGui.Text("Debug Text");
    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
