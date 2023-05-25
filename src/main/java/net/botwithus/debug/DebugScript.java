package net.botwithus.debug;

import net.botwithus.internal.plugins.ScriptDefinition;
import net.botwithus.rs3.events.impl.InventoryUpdateEvent;
import net.botwithus.rs3.events.impl.VariableUpdateEvent;
import net.botwithus.rs3.item.Item;
import net.botwithus.rs3.queries.ResultSet;
import net.botwithus.rs3.queries.builders.inventories.InventoryQuery;
import net.botwithus.rs3.script.Script;
import net.botwithus.rs3.skills.Skill;
import net.botwithus.rs3.skills.Skills;

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
    public void onLoop() {}

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
