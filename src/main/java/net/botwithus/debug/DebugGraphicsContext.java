package net.botwithus.debug;

import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.script.ScriptGraphicsContext;

public class DebugGraphicsContext extends ScriptGraphicsContext {

    @Override
    public void drawSettings() {
        ImGui.SetWindowSize(200.f, 200.f);
        if(ImGui.Begin("Debug Settings", 0)) {

            ImGui.Text("Debug Script Settings.");

            ImGui.End();
        }
    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }
}
