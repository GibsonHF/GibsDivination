package net.botwithus.debug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Item;
import net.botwithus.rs3.game.hud.interfaces.Component;
import net.botwithus.rs3.game.js5.types.vars.VarDomainType;
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery;
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery;
import net.botwithus.rs3.game.queries.results.EntityResultSet;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.game.scene.entities.characters.npc.Npc;
import net.botwithus.rs3.game.scene.entities.characters.player.Player;
import net.botwithus.rs3.game.skills.Skill;
import net.botwithus.rs3.game.skills.Skills;
import net.botwithus.rs3.game.vars.VarManager;
import net.botwithus.rs3.script.Execution;
import net.botwithus.rs3.script.LoopingScript;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DebugScript extends LoopingScript {
    public DebugScript(String name, ScriptDefinition plugin) {
        super(name, plugin);
    }

    public Map<Integer, Varp> varps = new ConcurrentHashMap<>();
    public Map<Integer, Varbit> varbits = new ConcurrentHashMap<>();

    @Override
    public boolean initialize() {
        this.sgc = new DebugGraphicsContext(getConsole(), this);
        this.loopDelay = 590;

        /*//What if jagex updates and we didn't fix it
        InterfaceMode.MODERN.overrideInterface(Prayer.class, "quick_toggle", 1430);
        //Scripters wants to support legacy interface to!
        InterfaceMode.LEGACY.overrideInterface(Prayer.class, "quick_toggle", 1505);

        subscribe(ChatMessageEvent.class, event -> {
            if(event.getMessage().equals("test")) {
                Prayer.toggleQuickPrayer(); // throws exception Not on script thread
                Prayer.toggle(NormalBook.PROTECT_ITEM); // throws exception Not on script thread
            }
        });

        subscribe(VariableUpdateEvent.class, event -> {
            if(event.isVarbit()) {
                if(varbits.containsKey(event.getId())) {
                    Varbit varbit = varbits.get(event.getId());
                    varbits.put(event.getId(), new Varbit(event.getId(), event.getValue(), varbit.isHidden()));
                } else {
                    varbits.put(event.getId(), new Varbit(event.getId(), event.getValue(), false));
                }
            } else {
                if(varps.containsKey(event.getId())) {
                    varps.get(event.getId()).setValue(event.getValue());
                } else {
                    varps.put(event.getId(), new Varp(event.getId(), event.getValue(), false));
                }
            }
        });*/
        return super.initialize();
    }

    public static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            .create();

    @Override
    public void onLoop() {
        npcQueryExample();
        Execution.delay(5000);
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
        ComponentQuery.newQuery(37).option("Rune bar", String::contains)
                .results().first();

        /*ComponentQuery.newQuery(37)
                .componentIndex(62)
                .subComponentIndex(3)
                .results()
                .first()
                .map(c -> c.doAction(1));*/
        Component first = ComponentQuery.newQuery(37)
                .componentIndex(62)
                .subComponentIndex(3)
                .results()
                .first();
        if (first != null) {
            first.interact(1);
        }
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
            System.out.printf("InvId= %d Name= %s Id= %d\n", result.getInventory().getId(), result.getName(), result.getId());
        }
        //Find Logs in local player inventory or Backpack
        InventoryItemQuery query1 = InventoryItemQuery.newQuery(93).name("Logs");
        Item first = query1.results().first();
        if (first != null) {
            System.out.println(first);
        }
    }

}
