package csp;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PIntensionConstraint;
import abscon.instance.tools.InstanceParser;
import java.util.ArrayList;

public class MyParser {

    private String name;
    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();

    public MyParser(String filename) {
        InstanceParser parser = new InstanceParser();
        parser.loadInstance(filename);
        parser.parse(false);

        this.name = parser.getName();

        System.out.println("Instance name: " + this.name);

        for (int i = 0; i < parser.getVariables().length; i++) {
            Variable newVar = new Variable(parser.getVariables()[i]);
            variables.add(newVar);
        }

        for (String key : parser.getMapOfConstraints().keySet()) {
            PConstraint con = parser.getMapOfConstraints().get(key);
            Constraint newCon = new Constraint(con, variables);
            if (con instanceof PIntensionConstraint inten) {
                Intension finalCon = new Intension (newCon);
                finalCon.setRelation(inten.getFunction().getName());
                constraints.add(finalCon);
            } else if (con instanceof PExtensionConstraint exten) {
                Extension finalCon = new Extension (newCon);
                finalCon.setDefinition(exten.getRelation().getSemantics());
                finalCon.setTuples(exten.getRelation().getTuples().clone());
                constraints.add(finalCon);
            } else {
                System.out.println("ERROR adding " + con.getName());
            }
        }

        for (Constraint con : constraints) {
            for (Variable var : con.getScope()) {
                var.addConstraint(con);
                for (Variable var2 : con.getScope()) {
                    if (!var.equals(var2)) {
                        var.addNeighbor(var2);
                    }
                }
            }
        }

        System.out.println("Variables: ");
        for (Variable var : variables) {
            System.out.println(var);
        }

        System.out.println("Constraints: ");
        for (Constraint con : constraints) {
            System.out.println(con);
        }

    }

    public static void main(String[] args) {
        //Hardcoded now... but should read in the file through the arguments, -f <XML-NAME>

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-f")) {
                MyParser parser = new MyParser(args[i + 1]);
            }
        }
    }


}