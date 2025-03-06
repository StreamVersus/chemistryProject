package pr.vladimir.demo1.API;

import pr.vladimir.demo1.Tiles.Carbon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {
    private final Map<Integer, List<Integer>> adjacencyMap = new HashMap<>();

    public void addEdge(Carbon u, Carbon... v) {
        for (Carbon value : v) {
            adjacencyMap.computeIfAbsent(u.id, _ -> new ArrayList<>()).add(value.id);
            adjacencyMap.computeIfAbsent(value.id, _ -> new ArrayList<>()).add(u.id);
        }
    }

    public void removeVertex(Carbon u) {
        List<Integer> removedList = adjacencyMap.remove(u.id);
        if(removedList == null) return;

        for (int i : removedList) {
            List<Integer> adjList = adjacencyMap.get(i);
            adjList.remove((Integer) u.id);
        }
    }

    public static List<Carbon> findPathWithClosestPoint(List<List<Carbon>> pathsCarb, List<Carbon> pointsCarb) {
        if(pathsCarb == null) return null;

        List<List<Integer>> paths = new ArrayList<>();
        for (List<Carbon> carbons : pathsCarb) {
            paths.add(carbons.stream().map(carb -> carb.id).toList());
        }

        List<Integer> points = pointsCarb.stream().map(carb -> carb.id).toList();

        int minDistance = Integer.MAX_VALUE;
        List<Integer> closestPath = null;

        for (List<Integer> path : paths) {
            int currentMinDistance = Integer.MAX_VALUE;

            for (Integer point : points) {
                if (path.contains(point)) {
                    int index = path.indexOf(point);
                    int distanceToEnd = path.size() - 1 - index;
                    if(distanceToEnd > index) path = path.reversed();

                    currentMinDistance = Math.min(currentMinDistance, path.indexOf(point));
                }
            }

            if (currentMinDistance < minDistance) {
                minDistance = currentMinDistance;
                closestPath = path;
            }
        }
        if(points.isEmpty()) return pathsCarb.getFirst();
        if(closestPath == null) throw new RuntimeException("idk");
        return closestPath.stream().map(Carbon::getByID).toList();
    }

    public List<Carbon> findCrosssections() {
        List<Carbon> retList = new ArrayList<>();
        adjacencyMap.keySet().forEach(i -> {
            List<Integer> list = adjacencyMap.get(i);
            if(list.size() >= 3) retList.add(Carbon.getByID(i));
        });

        return retList;
    }

    public List<List<Carbon>> findAllLongestPaths() {
        if(adjacencyMap.isEmpty()) return null;
        int startNode = adjacencyMap.keySet().iterator().next();

        List<Integer> firstLongestPath = new ArrayList<>();
        findFarthestNode(startNode, -1, new ArrayList<>(), firstLongestPath);

        int farthestNode = firstLongestPath.getLast();
        int maxDistance = findMaxDistance(farthestNode, -1, 0);

        List<List<Integer>> longestPaths = new ArrayList<>();
        dfs(farthestNode, -1, new ArrayList<>(), longestPaths, maxDistance);
        System.out.println(longestPaths);
        List<List<Carbon>> longestInCarbon = new ArrayList<>();
        for (List<Integer> longestPath : longestPaths) {
            longestInCarbon.add(longestPath.stream().map(Carbon::getByID).toList());
        }


        return longestInCarbon;
    }

    private void dfs(int node, int parent, List<Integer> currentPath, List<List<Integer>> longestPaths, int maxDistance) {
        currentPath.add(node);

        if (adjacencyMap.getOrDefault(node, new ArrayList<>()).size() == 1 && node != parent) {
            if (currentPath.size() == maxDistance + 1) {
                longestPaths.add(new ArrayList<>(currentPath));
            }
        }

        for (int neighbor : adjacencyMap.getOrDefault(node, new ArrayList<>())) {
            if (neighbor != parent) {
                dfs(neighbor, node, currentPath, longestPaths, maxDistance);
            }
        }

        currentPath.removeLast();
    }

    private int findMaxDistance(int node, int parent, int distance) {
        int maxDistance = distance;

        for (int neighbor : adjacencyMap.getOrDefault(node, new ArrayList<>())) {
            if (neighbor != parent) {
                maxDistance = Math.max(maxDistance, findMaxDistance(neighbor, node, distance + 1));
            }
        }

        return maxDistance;
    }

    private void findFarthestNode(int node, int parent, List<Integer> currentPath, List<Integer> farthestPath) {
        currentPath.add(node);

        if (currentPath.size() > farthestPath.size()) {
            farthestPath.clear();
            farthestPath.addAll(new ArrayList<>(currentPath));
        }

        for (int neighbor : adjacencyMap.getOrDefault(node, new ArrayList<>())) {
            if (neighbor != parent) {
                findFarthestNode(neighbor, node, currentPath, farthestPath);
            }
        }

        currentPath.removeLast();
    }
}