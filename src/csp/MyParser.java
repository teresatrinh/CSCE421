package csp;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PIntensionConstraint;
import abscon.instance.tools.InstanceParser;
import java.util.ArrayList;
import java.util.Collections;

public class MyParser {

    private String name;
    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();

    public MyParser(MyParser problem) {
        this.name = problem.getName();
        this.variables = (ArrayList<Variable>) problem.getVariables().clone();
        this.constraints = (ArrayList<Constraint>) problem.getConstraints().clone();
    }

    public void print() {
        System.out.println("Instance name: " + this.name);

        System.out.println("Variables: ");
        for (Variable var : variables) {
            System.out.println(var);
        }

        System.out.println("Constraints: ");
        for (Constraint con : constraints) {
            System.out.println(con);
        }
    }

    public MyParser(String filename) {
        InstanceParser parser = new InstanceParser();
        parser.loadInstance(filename);
        parser.parse(false);

        this.name = parser.getName();

        for (int i = 0; i < parser.getVariables().length; i++) {
            Variable newVar = new Variable(parser.getVariables()[i]);
            variables.add(newVar);
        }

        for (String key : parser.getMapOfConstraints().keySet()) {
            PConstraint con = parser.getMapOfConstraints().get(key);
            Constraint newCon = new Constraint(con, variables);
            if (con instanceof PIntensionConstraint inten) {
                Intension finalCon = new Intension (newCon);
                finalCon.setRelation(inten);
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

        //this.print();

    }

    public ArrayList<Constraint> getConstraints() {
        return this.constraints;
    }

    public ArrayList<Variable> getVariables() {
        Collections.sort(this.variables);
        return this.variables;
    }

    public String getName() {
        return this.name;
    }

    public void resetDomains() {
        for (Variable var : this.variables) {
            var.resetDomain();
        }
    }

    public static void main(String[] args) {

        MyParser parser = null;
        Solver solve = null;

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-f")) {
                parser = new MyParser(args[i + 1]);
                solve = new Solver(parser);
            } else if (args[i].equals("-a")) {
                if (args[i+1].equals("ac1")) {
                    solve.AC1();
                } else if (args[i+1].equals("ac3")) {
                    solve.AC3();
                }
            }
        }

        //MyParser parser = new MyParser("input/chain4-conflicts.xml");

        //Solver solve = new Solver(parser);
        //solve.AC1();
    }


}