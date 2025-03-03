package csp;

import java.util.Arrays;

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

    public void removeValue(int a) {
        int[] newValues = new int[this.values.length - 1];
        for (int i = 0, k = 0; i < this.values.length; i++) {
            if (this.values[i] != a) {
                newValues[k] = this.values[i];
                k++;
            }
        }
        this.values = newValues;

    }

    public boolean isEmpty() {
        if (this.values.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void sort() {
        Arrays.sort(this.values);
    }
}
