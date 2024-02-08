package net.botwithus.debug;

import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Area;
import net.botwithus.rs3.game.Coordinate;

import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.ScriptGraphicsContext;
import net.botwithus.rs3.script.config.ScriptConfig;

import java.util.ArrayList;
import java.util.List;

public class MainScript extends LoopingScript {

    public boolean Enriched;
    public boolean Butterfly;

    public int totalCaughtChronicles = 0;
    public int totalCaughtButterflies = 0;
    public boolean serenSpirits;
    public int totalCaughtSerenSpirits = 0;

    public MainScript(String name, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(name, scriptConfig, scriptDefinition);
    }


    public boolean runScript = false;


    TaskManager taskManager;
    List<TaskManager.Task> tasks = new ArrayList<>();

    @Override
    public boolean initialize() {
        this.sgc = new MainGraphicsContext(getConsole(), this);
        this.loopDelay = 590;

        taskManager = new TaskManager(tasks, this);
        //do a starter task to get it started
        tasks.add(new InventoryManagementTask(this));
        return super.initialize();
    }

    @Override
    public void onLoop() {
        if(!runScript)
        {
            return;
        }
        try {

            taskManager.runTasks();

        }catch (Exception e)
        {
            println(e.getMessage());
        }
    }

    public Area checker() {
        //user when traverse update to navigate to the correct wisp
        int level = Skills.DIVINATION.getActualLevel();
        if (level < 10) {
            println("Level 1-10 detected");
            return Wisp.PALE_WISP.getArea();
        } else if (level < 20) {
            println("Level 10-20 detected");
            return Wisp.FLICKERING_WISP.getArea();
        } else if (level < 30) {
            println("Level 20-30 detected");
            return Wisp.BRIGHT_WISP.getArea();
        } else if (level < 40) {
            println("Level 30-40 detected");
            return Wisp.GLOWING_WISP.getArea();
        } else if (level < 50) {
            println("Level 40-50 detected");
            return Wisp.SPARKLING_WISP.getArea();
        } else if (level < 60) {
            println("Level 50-60 detected");
            return Wisp.GLEAMING_WISP.getArea();
        } else if (level < 70) {
            println("Level 60-70 detected");
            return Wisp.VIBRANT_WISP.getArea();
        } else if (level < 75) {
            println("Level 70-75 detected");
            return Wisp.LUSTROUS_WISP.getArea();
        } else if (level < 80) {
            println("Level 75-80 detected");
            return Wisp.ELDER_WISP.getArea();
        } else if (level < 85) {
            println("Level 80-85 detected");
            return Wisp.BRILLIANT_WISP.getArea();
        } else if (level < 90) {
            println("Level 85-90 detected");
            return Wisp.RADIANT_WISP.getArea();
        } else if (level < 95) {
            println("Level 90-95 detected");
            return Wisp.LUMINOUS_WISP.getArea();
        } else {
            println("Level 95-99 detected");
            return Wisp.INCANDESCENT_WISP.getArea();
        }
    }

    public ScriptGraphicsContext getSgc() {
        return sgc;
    }

    public enum Wisp {
        PALE_WISP(new Area.Rectangular(new Coordinate(3113, 3221, 0), new Coordinate(3123, 3213, 0))),
        FLICKERING_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        BRIGHT_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        GLOWING_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        SPARKLING_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        GLEAMING_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        VIBRANT_WISP(new Area.Rectangular(new Coordinate(2414, 2871, 0), new Coordinate(2423, 2863, 0))),
        LUSTROUS_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        ELDER_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        BRILLIANT_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        RADIANT_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        LUMINOUS_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0))),
        INCANDESCENT_WISP(new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0)));

        private final Area area;

        Wisp(Area area) {
            this.area = area;
        }

        public Area getArea() {
            return area;
        }
    }

}
