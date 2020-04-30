import java.io.*;
import java.util.*;

public class Cyk {

    private static boolean parseString(TreeMap<String, LinkedHashSet<String>> grammarMap, char[] w) throws Exception {
        int n = w.length;

        // table(i,j)
        // initialize table and empty table rows
        ArrayList<ArrayList<LinkedHashSet<String>>> table = new ArrayList<>();
        for (int i=0; i<n; i++) {
            ArrayList<LinkedHashSet<String>> tableRow = new ArrayList<>();
            for (int j=0; j<n; j++) {
                LinkedHashSet<String> cell = new LinkedHashSet<>();
                tableRow.add(cell);
            }
            ArrayList<LinkedHashSet<String>> copyRow = new ArrayList<>(tableRow);
            table.add(copyRow);
            tableRow.clear();
        }

        // step 1
        if (w == null && grammarMap.firstEntry().getValue().contains("e,e")) {
            return true;
        }
        // step 2
        Set<String> grammarKeySet = grammarMap.keySet();
        for (int i=0; i<n; i++) {
            // step 3
            for (String key : grammarKeySet) {
                // step 4 and 5
                for (String prod : grammarMap.get(key)) {
                    String [] splitProd = prod.split(",");
                    String prod1 = splitProd[0];
                    String prod2 = splitProd[1];
                    // (w[b],e) case
                    if (prod1.equals(String.valueOf(w[i])) && prod2.equals("e")) {
                        ArrayList<LinkedHashSet<String>> newRow = new ArrayList<>(table.get(i));
                        LinkedHashSet<String> newCell = new LinkedHashSet<>(table.get(i).get(i));
                        // add A
                        newCell.add(key);
                        // and add production of A->be where b==w[i]
                        newCell.add(prod1);

                        newRow.set(i, newCell);
                        table.set(i, newRow);
                    } // (e,w[b]) case
                    else if (prod1.equals("e") && prod2.equals(String.valueOf(w[i]))) {
                        ArrayList<LinkedHashSet<String>> newRow = new ArrayList<>(table.get(i));
                        LinkedHashSet<String> newCell = new LinkedHashSet<>(table.get(i).get(i));
                        // add A
                        newCell.add(key);
                        // and add production of A->eb where b==w[i]
                        newCell.add(prod2);

                        newRow.set(i, newCell);
                        table.set(i, newRow);
                    } // (w[i],a) or (a,w[i]) case
                    else if (prod1.equals(String.valueOf(w[i])) || prod2.equals(String.valueOf(w[i]))) {
                        ArrayList<LinkedHashSet<String>> newRow = new ArrayList<>(table.get(i));
                        LinkedHashSet<String> newCell = new LinkedHashSet<>(table.get(i).get(i));
                        // add A
                        newCell.add(key);
                        // and add production of A->ab where b==w[i] or a==w[i]
                        newCell.add(prod1);
                        newCell.add(prod2);

                        newRow.set(i, newCell);
                        table.set(i, newRow);
                    }
                }
            }
        }
        // step 6
        for (int l=2; l<=n; l++) {
            // step 7
            for (int i=1; i<=n-l+1; i++) {
                // step 8
                int j = i+l-1;
                // step 9
                for (int k=i; k<=j-1; k++) {
                    // step 10
                    for (String A: grammarKeySet) {
                        // iterate through productions of variable
                        for (String prod : grammarMap.get(A)) {
                            String[] splitProd = prod.split(",");
                            String B = splitProd[0];
                            String C = splitProd[1];
                            // step 11
                            if (table.get(i-1).get(k-1).contains(B) && table.get(k).get(j-1).contains(C)) {
                                ArrayList<LinkedHashSet<String>> newRow = new ArrayList<>(table.get(i-1));
                                LinkedHashSet<String> newCell = new LinkedHashSet<>(newRow.get(j-1));

                                newCell.add(A);
                                newRow.set(j-1, newCell);
                                table.set(i-1, newRow);
                            }
                        }
                    }
                }
            }
        }
        return table.get(0).get(n-1).contains(grammarMap.firstKey());
    }

    /*
        Convert ArrayList to TreeMap {variable: list of productions}
     */
    private static TreeMap<String, LinkedHashSet<String>> arrListToTreeMap(ArrayList<String> grammar) throws Exception {
        TreeMap<String, LinkedHashSet<String>> grammarMap = new TreeMap<>();

        for (String rule : grammar) {

            // input validation for proper grammar format
            if (rule.indexOf(':') == -1) {
                throw new Exception("NO. '"+rule+"' is not in the proper format");
            }

            // initialize variable and its productions
            String variable, production;

            // split
            String[] splitRule = rule.split(":");
            variable = splitRule[0];
            production = splitRule[1];

            // if variable already exists, place productions under same key
            if (grammarMap.containsKey(variable.toString())) {
                grammarMap.computeIfAbsent(variable.toString(), k -> new LinkedHashSet<String>()).add(production);
            } else {
                LinkedHashSet<String> productions = new LinkedHashSet<>();
                productions.add(production);
                grammarMap.put(variable.toString(), productions);
            }
        }
        return grammarMap;
    }


    public static void main(String[] args) throws Exception {

        // open file and initialize BufferReader
        File grammarFile = new File(args[0]);
        BufferedReader reader = new BufferedReader(new FileReader(grammarFile));

        // transfer grammar from text file to ArrayList
        ArrayList<String> grammar = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            grammar.add(line);
            line = reader.readLine();
        }

        try {
            // convert ArrayList to HashMap to make life a bit easier
            TreeMap<String, LinkedHashSet<String>> grammarMap = arrListToTreeMap(grammar);

            // validate inputString from arguments
            char[] inputString = args[1].toCharArray();
            for (char c : inputString) {
                if (c!='a' && c!='b') {
                    throw new Exception("NO. characters not within {a,b}");
                }
            }

            // parse String with modified CYK Algorithm
            boolean doesGenerateString = parseString(grammarMap, inputString);
            // print result
            if (doesGenerateString) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
