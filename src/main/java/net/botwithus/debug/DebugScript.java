package net.botwithus.debug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.cs2.ArgumentLayout;
import net.botwithus.rs3.cs2.ScriptDescriptor;
import net.botwithus.rs3.cs2.ScriptHandle;
import net.botwithus.rs3.cs2.ScriptLookup;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.js5.types.EnumType;
import net.botwithus.rs3.game.js5.types.ItemType;
import net.botwithus.rs3.game.js5.types.configs.ConfigManager;
import net.botwithus.rs3.game.js5.types.vars.VarDomainType;
import net.botwithus.rs3.game.queries.builders.animations.SpotAnimationQuery;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.items.GroundItemQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.game.scene.entities.animation.SpotAnimation;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.scene.entities.characters.player.Player;
import net.botwithus.rs3.game.scene.entities.item.GroundItem;
import net.botwithus.rs3.game.skills.Skill;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;
import net.botwithus.rs3.script.config.ScriptConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.botwithus.rs3.cs2.ScriptDescriptor.INT;
import static net.botwithus.rs3.cs2.ScriptDescriptor.STRING;

public class DebugScript extends LoopingScript {
    public DebugScript(String name, ScriptConfig scriptConfig, ScriptDefinition scriptDefinition) {
        super(name, scriptConfig, scriptDefinition);
    }

    public Map<Integer, Varp> varps = new ConcurrentHashMap<>();
    public Map<Integer, Varbit> varbits = new ConcurrentHashMap<>();

    public boolean runScript = false;

    @Override
    public boolean initialize() {
        this.sgc = new DebugGraphicsContext(getConsole(), this);
        this.loopDelay = 590;

        //EventBus.EVENT_BUS.subscribe(this, SceneObjectUpdateEvent.class, event -> System.out.printf("LocID= %d Type= %d Rot= %d Tile={%d, %d, %d}\n", event.getObjectId(), event.getType(), event.getRotation(), event.getX(), event.getY(), event.getPlane()));

        return super.initialize();
    }

    public static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            .create();

    @Override
    public void onLoop() {
        if(runScript) {
            runScript = false;
            System.out.println("Executing Script.");
            executeCs2ScriptsWithReturnValues();
            System.out.println("Script Finished.");
        }

        npcQueryExample();

        /*Item box = InventoryItemQuery.newQuery(93).name("Archaeological soil box")
                .results().first();
        if(box != null) {
            Component comp = ComponentQuery.newQuery(517).item(box.getId()).results().first();
            if(comp != null) {
                boolean success = comp.interact(9);
                if(!success) {
                    System.out.println("Failed to interact with soil box.");
                }
            } else {
                System.out.println("Component was not found.");
            }
        } else {
            System.out.println("Item was not found.");
        }*/

        //interfaceQuery();
        //spotAnimQuery();
        //mageOfZammy();
        //System.out.println(VarManager.getVarValue(VarDomainType.PLAYER, 689));

        groundItemQuery();

        Execution.delay(5000);
    }

    public void executeCs2Scripts() {
        ScriptHandle sendGameMessage = ScriptLookup.lookup(12868, ScriptDescriptor.ofVoid(STRING));
        if(sendGameMessage != null) {
            sendGameMessage.invokeExact("[Debug Script]: Hello, World");
        }
    }

    public void executeCs2ScriptsWithReturnValues() {
        ScriptHandle handle = ScriptLookup.lookup(10500, ScriptDescriptor.of(List.of(STRING)));
        if(handle != null) {
            Object[] values = handle.invokeExact();
            if(values == null) {
                System.out.println("No return values.");
            } else if(values.length != 1) {
                System.out.println("Ugh....");
            } else if (!(values[0] instanceof String)) {
                System.out.println("Fuck what??? " + values[0].getClass().getName());
            } else {
                System.out.println("Value= " + values[0]);
            }
        } else {
            System.out.println("Script handle is null");
        }
    }

    public int getMaximumHeat(int invId, int slot) {
        EnumType enumType = ConfigManager.getEnumType(15095);
        int key = VarManager.getInvVarbit(invId, slot, 43222);
        Integer value = (Integer) enumType.getOutput(key);
        if(value == null) {
            return -1;
        }
        ItemType type = ConfigManager.getItemType(value);

        ScriptHandle handle = ScriptLookup.lookup(2547, ScriptDescriptor.of(List.of(INT), INT));
        if(handle != null && type != null) {
            Object[] values = handle.invokeExact(type.getId());
            return (int) values[0];
        }
        return -1;
    }

    public int getOreAmount(int oreId) {
        ScriptHandle handle = ScriptLookup.lookup(8250, ScriptDescriptor.of(List.of(INT), INT));
        if(handle != null) {
            Object[] rets = handle.invokeExact(oreId);
            return (int) rets[0];
        }
        return 0;
    }

    public void spotAnimQuery() {
        for (SpotAnimation result : SpotAnimationQuery.newQuery().results()) {
            if (result != null) {
                System.out.println(result.getId());
                System.out.println(result.getCoordinate());
            }
        }
    }

    public void mageOfZammy() {
        EntityResultSet<Npc> results = NpcQuery.newQuery().name("Mage of Zamorak").option("Teleport").results();
        if(!results.isEmpty()) {
            Npc mage = results.first();
            if (mage != null) {
                System.out.println(mage.getName());
                for (String option : mage.getOptions()) {
                    System.out.println(option);
                }
            }
        }
    }

    public void npcQueryExample() {
        EntityResultSet<Npc> results = NpcQuery.newQuery().name("Chicken").results();
        System.out.println(results.size());
        for (Npc chicken : results) {
            System.out.println("Name= " + chicken.getName() + " Id= " + chicken.getId());
        }
    }

    public void playerExample() {
        Player self = Client.getLocalPlayer();
        if (self == null) {
            System.out.println("Fuck");
        } else {
            self.getHitmarks();
            self.getHeadbars();
        }
    }

    private void variableExample() {
        //Skill dialogue, selected item
        int itemId = VarManager.getVarValue(VarDomainType.PLAYER, 1170);
        System.out.println(itemId);
        boolean inCombat = VarManager.getVarbitValue(1899) == 1;
        System.out.println("Is in combat= " + inCombat);
    }

    public void interfaceQuery() {
        /*Component rune_bar = ComponentQuery.newQuery(37).option("Rune bar", String::contains)
                .results().first();

        System.out.println(rune_bar);*/

        Component lodestone = ComponentQuery.newQuery(1465).option("Lodestone network")
                .results().first();
        if (lodestone != null) {
            for (String option : lodestone.getOptions()) {
                System.out.println(option);
            }
            System.out.println(lodestone.interact("Lodestone network"));
        }

        /*ComponentQuery.newQuery(37)
                .componentIndex(62)
                .subComponentIndex(3)
                .results()
                .first()
                .map(c -> c.doAction(1));*/
       /* Component first = ComponentQuery.newQuery(37)
                .componentIndex(62)
                .subComponentIndex(3)
                .results()
                .first();
        if (first != null) {
            first.interact(1);
        }*/
    }

    public void skillsExample() {
        Skill skill = Skills.SLAYER.getSkill();
        System.out.println(skill);
    }

    public void inventoryQuery() {
        //Find what inventory 1512 belongs too.
        InventoryItemQuery query = InventoryItemQuery.newQuery().ids(1512);
        ResultSet<Item> results = query.results();
        for (Item result : results) {
            System.out.printf("InvId= %d Name= %s Id= %d\n", result.getInventoryType().getId(), result.getName(), result.getId());
        }
        //Find Logs in local player inventory or Backpack
        InventoryItemQuery query1 = InventoryItemQuery.newQuery(93).name("Logs");
        Item first = query1.results().first();
        if (first != null) {
            System.out.println(first);
        }
    }

    public void groundItemQuery() {
        for (GroundItem result : GroundItemQuery.newQuery().results()) {
            System.out.println(result.getName() + " - " + result.getId() + " - " + result.getStackSize() + " - " + result.getCoordinate());
        }
    }

}
