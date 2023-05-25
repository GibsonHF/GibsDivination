package net.botwithus.debug;

import net.botwithus.rs3.variables.VarDomain;
import net.botwithus.rs3.variables.VariableManager;

import java.time.LocalTime;

public class Varbit {
    private final int id;
    private int value;
    private boolean hidden;

    private LocalTime lastUpdated;

    private final VarDomain domain;

    public Varbit(int id, int value, boolean hidden) {
        this.id = id;
        this.value = value;
        this.hidden = hidden;
        this.lastUpdated = LocalTime.now();
        this.domain = VariableManager.getVarbitDomain(id);
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.lastUpdated = LocalTime.now();
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public VarDomain getDomain() {
        return domain;
    }

    public LocalTime getLastUpdated() {
        return lastUpdated;
    }
}
