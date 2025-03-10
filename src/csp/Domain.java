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

    public void removeIndex(int a) {
        int[] newValues = new int[this.values.length - 1];
        for (int i = 0, k = 0; i < this.values.length; i++) {
            if (i != a) {
                newValues[k] = this.values[i];
                k++;
            }
        }
    }

    public int indexOf(int j) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == j) {
                return i;
            }
        }

        return -1;
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

    public int getValue(int i) {
        return this.values[i];
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public int length() {
        return this.values.length;
    }
}
