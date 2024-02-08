package net.botwithus.debug;

import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.NativeInteger;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.rs3.script.ScriptGraphicsContext;
import java.util.concurrent.TimeUnit;

public class MainGraphicsContext extends ScriptGraphicsContext {

    private final MainScript script;
    private long startTime;
    private int startXP;
    private int startLevel;
    public String[] wispNames = {
            "Pale", "Flickering", "Bright", "Glowing", "Sparkling",
            "Gleaming", "Vibrant", "Lustrous", "Elder", "Brilliant",
            "Radiant", "Luminous", "Incandescent"
    };
   public NativeInteger selectedWispIndex = new NativeInteger(0);

    public MainGraphicsContext(ScriptConsole console, MainScript script) {
        super(console);
        this.script = script;
        this.startTime = System.currentTimeMillis();
        this.startXP = Skills.DIVINATION.getSkill().getExperience();
        this.startLevel = Skills.DIVINATION.getSkill().getLevel();
    }


    public void drawSettings() {
        ImGui.SetWindowSize(200.f, 200.f);
        if (ImGui.Begin("GibsDivination", 0)) {


            if (ImGui.BeginTabBar("SettingsTabBar", 0)) {

                if (ImGui.BeginTabItem("Settings", 0)) {
                    script.runScript = ImGui.Checkbox("Run Script", script.runScript);
                    script.Enriched = ImGui.Checkbox("Capture Chronicles", script.Enriched);
                    script.Butterfly = ImGui.Checkbox("Capture Butterflies", script.Butterfly);
                    script.serenSpirits = ImGui.Checkbox("Capture Seren Spirits", script.serenSpirits);
                    ImGui.EndTabItem();
                }

                if (ImGui.BeginTabItem("Statistics", 0)) {
                    ImGui.Text("Total Chronicles: " + script.totalCaughtChronicles);
                    ImGui.Text("Total Butterflies: " + script.totalCaughtButterflies);
                    ImGui.Text("Total Seren Spirits: " + script.totalCaughtSerenSpirits);

                    long timeElapsedMillis = System.currentTimeMillis() - startTime;
                    String timeElapsed = String.format("%02dh %02dm %02ds",
                            TimeUnit.MILLISECONDS.toHours(timeElapsedMillis),
                            TimeUnit.MILLISECONDS.toMinutes(timeElapsedMillis) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(timeElapsedMillis) % TimeUnit.MINUTES.toSeconds(1));
                    ImGui.Text("Time Elapsed: " + timeElapsed);

                    int currentXP = Skills.DIVINATION.getSkill().getExperience();
                    int xpGained = Math.max(0, currentXP - startXP);
                    int xpPerHour = (int) (xpGained / (timeElapsedMillis / 3600000.0));
                    ImGui.Text("XP Gained: " + xpGained);
                    ImGui.Text("XP/Hour: " + xpPerHour);

                    int xpToLevel = Skills.DIVINATION.getSkill().getExperienceToNextLevel();
                    int currentLevel = Skills.DIVINATION.getSkill().getLevel();
                    int levelsGained = currentLevel - startLevel;
                    ImGui.Text("Current Level: " + currentLevel + " (+" + levelsGained + ")");
                    ImGui.Text("XP to Level: " + xpToLevel);

                    int secondsTillLevel = (int) (xpToLevel / (xpPerHour / 3600.0));
                    String timeTillLevel = String.format("%02dh %02dm %02ds",
                            TimeUnit.SECONDS.toHours(secondsTillLevel),
                            TimeUnit.SECONDS.toMinutes(secondsTillLevel) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.SECONDS.toSeconds(secondsTillLevel) % TimeUnit.MINUTES.toSeconds(1));
                    ImGui.Text("Time till level: " + timeTillLevel);

                    if(ImGui.Button("Reset XP/Time"))
                    {
                        startTime = System.currentTimeMillis();
                        startXP = Skills.DIVINATION.getSkill().getExperience();
                        startLevel = Skills.DIVINATION.getSkill().getLevel();
                    }

                    ImGui.EndTabItem();
                }


                if (ImGui.BeginTabItem("Instructions", 0)) {
                    ImGui.Text("Start at the energy rift of your choice");
                    ImGui.Text("Make sure you have the required level to harvest the wisp");
                    ImGui.Text("Press 'Run Script' to start the bot");
                    ImGui.Text("Pathing coming soon with progression mode");
                    ImGui.EndTabItem();
                }

                ImGui.EndTabBar();
            }

            ImGui.End();
        }
    }

    private float RGBToFloat(int i) {
        return i / 255.0f;
    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();
    }



}
