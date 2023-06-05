package net.botwithus.debug;

import net.botwithus.internal.plugins.ScriptDefinition;
import net.botwithus.rs3.events.impl.VariableUpdateEvent;
import net.botwithus.rs3.interfaces.InterfaceMode;
import net.botwithus.rs3.interfaces.prayer.AncientBook;
import net.botwithus.rs3.interfaces.prayer.NormalBook;
import net.botwithus.rs3.interfaces.prayer.Prayer;
import net.botwithus.rs3.item.Item;
import net.botwithus.rs3.queries.ResultSet;
import net.botwithus.rs3.queries.builders.components.ComponentQuery;
import net.botwithus.rs3.queries.builders.inventories.InventoryQuery;
import net.botwithus.rs3.script.Delay;
import net.botwithus.rs3.script.Script;
import net.botwithus.rs3.skills.Skill;
import net.botwithus.rs3.skills.Skills;
import net.botwithus.rs3.variables.VariableManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DebugScript extends Script {
    public DebugScript(String name, ScriptDefinition plugin) {
        super(name, plugin);
    }

    public Map<Integer, Varp> varps = new ConcurrentHashMap<>();
    public Map<Integer, Varbit> varbits = new ConcurrentHashMap<>();

    @Override
    public void initialize() {
        super.initialize();
        this.sgc = new DebugGraphicsContext(this);

        //What if jagex updates and we didn't fix it
        InterfaceMode.MODERN.overrideInterface(Prayer.class, "quick_toggle", 1435);
        //Scripters wants to support legacy interface to!
        InterfaceMode.LEGACY.overrideInterface(Prayer.class, "quick_toggle", 1506);

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
        });
    }

    @Override
    public void onLoop() {
        Delay.delay(5000);
    }

    private void variableExample() {
        //Skill dialogue, selected item
        int itemId = VariableManager.getVarPlayerValue(1170);
        System.out.println(itemId);
        boolean inCombat = VariableManager.getVarbitValue(1899) == 1;
        System.out.println("Is in combat= " + inCombat);
    }

    private void prayerExample() {
        Prayer.toggleQuickPrayer();
        Prayer.toggle(NormalBook.PROTECT_ITEM);

        if(Prayer.isActive(NormalBook.PROTECT_ITEM)) {
            System.out.println("Protect item is active");
        }

        Prayer.toggle(AncientBook.BERSERKER);
        if(Prayer.isActive(AncientBook.BERSERKER)) {
            System.out.println("Berserker is active");
        }
    }

    public void interfaceQuery() {
        ComponentQuery.newQuery(37).option("Rune bar", String::contains)
                .results().first().ifPresent(c -> {
                    System.out.println(c.getSpriteId());
                });
    }

    public void skillsExample() {
        Skill skill = Skills.getSkill(Skills.SLAYER);
        System.out.println(skill);
    }

    public void inventoryQuery() {
        //Find what inventory 1512 belongs too.
        InventoryQuery query = InventoryQuery.newQuery().itemId(1512);
        ResultSet<Item> results = query.results();
        for (Item result : results) {
            System.out.printf("InvId= %d Name= %s Id= %d\n", result.getInventoryId(), result.getName(), result.getItemId());
        }
        //Find Logs in local player inventory or Backpack
        InventoryQuery query1 = InventoryQuery.newQuery(93).itemName("Logs");
        query1.results().first().ifPresent(System.out::println);
    }

}
