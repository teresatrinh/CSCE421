package csp;

public class InstantiatedVariable {

    public Variable variable;
    public int value;

    public InstantiatedVariable(Variable var, int value) {
        this.variable = var;
        this.value = value;
    }

    public InstantiatedVariable(Variable var) {
        this.variable = var;
    }

    public void setValue(int a) {
        this.value = a;
    }

    public Variable getVar() {
        return this.variable;
    }

    public int getValue() {
        return this.value;
    }


}
