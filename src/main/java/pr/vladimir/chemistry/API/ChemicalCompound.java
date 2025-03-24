package pr.vladimir.chemistry.API;

import pr.vladimir.chemistry.Tiles.Carbon;
import pr.vladimir.chemistry.Tiles.Connection;
import pr.vladimir.chemistry.Tiles.FuncGroup;

import java.util.*;

public class ChemicalCompound {
    private final List<Carbon> mainChain;
    private final List<List<Carbon>> radicalList;
    private final List<String> names = List.of("мет", "эт", "проп", "бут", "пент", "гекс", "гепт", "окт", "нон", "дек");
    private final List<String> nums = List.of("ди", "три", "тетра", "пента", "гекса", "гепта", "окта", "нона", "дека");

    public ChemicalCompound(List<Carbon> mainChain, List<List<Carbon>> radicalList) {
        this.mainChain = mainChain;
        this.radicalList = radicalList;
    }

    public String getName() {
        String postfix;
        int doubleCount = 0;
        int tripleCount = 0;
        for (Connection connection : Connection.specialSet) {
            if(connection.value == 2) doubleCount++;
            else tripleCount++;
        }

        if(doubleCount == 1 && tripleCount == 0) postfix = "ен";
        else if (tripleCount == 1 && doubleCount == 0) postfix = "ин";
        else if (doubleCount == 2 && tripleCount == 0) postfix = "адиен";
        else if (doubleCount == 0 && tripleCount == 0) postfix = "ан";
        else return "";

        StringBuilder connectionIndex = new StringBuilder("-");
        List<Integer> set = new ArrayList<>(indexConnections().keySet()).stream().sorted().toList();

        for (int i = 0; i < set.size(); i++) {
            connectionIndex.append(set.get(i));
            if(i != set.size() - 1) connectionIndex.append(",");
        }

        if(connectionIndex.toString().equals("-")) connectionIndex.delete(0, connectionIndex.length());

        StringBuilder prefix = new StringBuilder();
        Map<Integer, List<Integer>> organizedRadicals = organizeRadicals();
        if(organizedRadicals == null) return "";

        int in = 0;
        for (Map.Entry<Integer, List<Integer>> entry : new TreeMap<>(organizedRadicals).entrySet()) {
            int last = 0;
            var valueList = entry.getValue().stream().sorted().toList();
            for (int i = 0; i < valueList.size(); i++) {
                StringBuilder a = new StringBuilder();
                a.append(valueList.get(i));
                if(i != valueList.size() - 1) a.append(",");
                if(last > valueList.get(i)) prefix.insert(0, a);
                else prefix.append(a);
                last = valueList.get(i);
            }
            prefix.append("-");

            if(entry.getValue().size() - 1 != 0) prefix.append(parseMult(entry.getValue().size()));
            prefix.append(parseName(entry.getKey())).append("ил");
            if(in != organizedRadicals.size()) prefix.append("-");
            in++;
        }

        if(FuncGroup.funcVar != null) {
            postfix += "ол";
        }

        int carbonCnt = mainChain.size();
        if(FuncGroup.funcVar != null && !FuncGroup.lookup.get(FuncGroup.funcVar.state).contains("C")) carbonCnt -= 1;
        if(carbonCnt > 10) return null;

        return prefix + parseName(carbonCnt) + postfix + connectionIndex;
    }

    private String parseName(int size) {
        return names.get(size - 1);
    }

    private String parseMult(int size) {
        return nums.get(size - 2);
    }

    private Map<Integer, Connection> indexConnections() {
        Map<Integer, Connection> map = new HashMap<>();
        for (int i = 0; i < mainChain.size() - 1; i++) {
            for (Connection connection : Connection.specialSet) {
                List<Carbon> arr = List.of(connection.getAdjacent());
                if(arr.contains(mainChain.get(i)) && arr.contains(mainChain.get(i+1))) {
                    map.put(i+1, connection);
                    break;
                }
            }
        }

        return map;
    }

    private Map<Integer, List<Integer>> organizeRadicals() {
        Map<Integer, List<Integer>> retMap = new HashMap<>();
        for (List<Carbon> carbons : radicalList) {
            List<Integer> b = retMap.getOrDefault(carbons.size(), new ArrayList<>());
            for (Carbon carbon : carbons) {
                if(mainChain.contains(carbon)) {
                    b.add(mainChain.indexOf(carbon));
                    retMap.put(carbons.size(), b);
                }
            }
            for (Connection connection : Connection.specialSet) {
                List<Carbon> arr = List.of(connection.getAdjacent());
                if(new HashSet<>(arr).containsAll(carbons)) return null;
            }
        }

        return retMap;
    }
}
