package pr.vladimir.demo1.API;

import pr.vladimir.demo1.Tiles.Carbon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {
    private final Map<Integer, List<Integer>> adjacencyList = new HashMap<>();

    public void addEdge(Carbon u, Carbon... v) {
        for (Carbon value : v) {
            adjacencyList.computeIfAbsent(u.id, _ -> new ArrayList<>()).add(value.id);
            adjacencyList.computeIfAbsent(value.id, _ -> new ArrayList<>()).add(u.id);
        }
        System.out.println(adjacencyList);
    }

    public void removeVertex(Carbon u) {
        List<Integer> removedList = adjacencyList.remove(u.id);
        if(removedList == null) return;

        for (int i : removedList) {
            List<Integer> adjList = adjacencyList.get(i);
            adjList.remove((Integer) u.id);
        }
        System.out.println(adjacencyList);
    }

    public List<Carbon> getEdges(Carbon key) {
        return adjacencyList.get(key.id)
                .stream()
                .map(Carbon::getByID)
                .toList();
    }

    public List<Carbon> findLongestPath() {
        int farthestNode = dfs(0, -1, new ArrayList<>(), new ArrayList<>()).getLast();
        return dfs(farthestNode, -1, new ArrayList<>(), new ArrayList<>())
                .stream()
                .map(Carbon::getByID)
                .toList()
                .reversed();
    }

    private List<Integer> dfs(int node, int parent, List<Integer> currentPath, List<Integer> longestPath) {
        currentPath.add(node);

        if (currentPath.size() > longestPath.size()) {
            longestPath.clear();
            longestPath.addAll(new ArrayList<>(currentPath));
        }

        for (int neighbor : adjacencyList.getOrDefault(node, new ArrayList<>())) {
            if (neighbor != parent) {
                dfs(neighbor, node, currentPath, longestPath);
            }
        }

        currentPath.removeLast();
        return longestPath;
    }
}