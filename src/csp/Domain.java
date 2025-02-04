package csp;

public class Domain {
    
    protected String name;
    protected int[] values;

    public Domain(String name, int[] values) {
        this.name = name;
        this.values = values;
    }

    public String toString() {
        String s = "{";
        for (int i = 0; i < values.length; i++) {
            s += values[i];
            if (i != (values.length -1)) {
                s += ", ";
            }
        }
        s += "}";
        return s;
    }

    public String getName() {
        return this.name;
    }

    public int[] getValues() {
        return this.values;
    }

}
