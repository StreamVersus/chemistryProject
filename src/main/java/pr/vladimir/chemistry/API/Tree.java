package pr.vladimir.chemistry.API;

import pr.vladimir.chemistry.Backend;
import pr.vladimir.chemistry.Tiles.Carbon;
import pr.vladimir.chemistry.Tiles.FuncGroup;

import java.util.*;

public class Tree {
    private final Map<Integer, List<Integer>> adjacencyMap = new HashMap<>();

    public void addEdge(Carbon u, Carbon... v) {
        for (Carbon value : v) {
            adjacencyMap.computeIfAbsent(u.id, _ -> new ArrayList<>()).add(value.id);
            adjacencyMap.computeIfAbsent(value.id, _ -> new ArrayList<>()).add(u.id);
        }
    }

    public List<Integer> getVertex(Carbon c) {
        List<Integer> ret = adjacencyMap.get(c.id);
        if(ret == null) return new ArrayList<>();
        return ret;
    }

    public void removeEdge(Carbon u, Carbon v) {
        adjacencyMap.get(u.id).remove((Integer) v.id);
        adjacencyMap.get(v.id).remove((Integer) u.id);
    }

    public void removeVertex(Carbon u) {
        List<Integer> removedList = adjacencyMap.remove(u.id);
        if(removedList == null) return;

        for (int i : removedList) {
            List<Integer> adjList = adjacencyMap.get(i);
            adjList.remove((Integer) u.id);
        }
    }

    public List<Carbon> findLongestPath(List<List<Carbon>> pathsCarb, List<Carbon> pointsCarb) {
        if(pathsCarb == null) return null;
        if(adjacencyMap.size() == 1) return List.of(Carbon.getByID(adjacencyMap.keySet().stream().toList().getFirst()));

        List<List<Integer>> paths = new ArrayList<>();
        for (List<Carbon> carbons : pathsCarb) {
            paths.add(carbons.stream().map(carb -> carb.id).toList());
        }

        List<Integer> points = new ArrayList<>(pointsCarb.stream().map(carb -> carb.id).toList());

        int minDistance = Integer.MAX_VALUE;
        List<Integer> closestPath = null;

        for (List<Integer> path : paths) {
            int currentMinDistance = Integer.MAX_VALUE;
            boolean corrected = false;
            for (Integer point : points) {
                if (path.contains(point)) {
                    int index = path.indexOf(point);
                    int distanceToEnd = path.size() - 1 - index;
                    if(distanceToEnd > index) {
                        path = path.reversed();
                        corrected = true;
                    }
                    currentMinDistance = Math.min(currentMinDistance, path.indexOf(point));
                }
            }
            if(!corrected && Carbon.getByID(path.getFirst()).getBoxVec().getX() > Carbon.getByID(path.getLast()).getBoxVec().getX()) path = path.reversed();
            if (currentMinDistance < minDistance) {
                minDistance = currentMinDistance;
                closestPath = path;
            }
        }
        if(points.isEmpty()) return pathsCarb.getFirst();
        if(closestPath == null) throw new RuntimeException("idc");
        return closestPath.stream().map(Carbon::getByID).toList();
    }

    public List<Carbon> findCrossections() {
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
        FuncGroup workaround = null;
        if(FuncGroup.funcVar != null && FuncGroup.funcVar.state == 1) {
            workaround = FuncGroup.funcVar;
            Backend.setMatrix(workaround.getBoxVec(), null);
        }

        List<Integer> firstLongestPath = new ArrayList<>();
        findFarthestNode(startNode, -1, new ArrayList<>(), firstLongestPath);

        int farthestNode = firstLongestPath.getLast();
        int maxDistance = findMaxDistance(farthestNode, -1, 0);

        List<List<Integer>> longestPaths = new ArrayList<>();
        dfs(farthestNode, -1, new ArrayList<>(), longestPaths, maxDistance);

        List<List<Carbon>> longestInCarbon = new ArrayList<>();
        for (List<Integer> longestPath : longestPaths) {
            longestInCarbon.add(longestPath.stream().map(Carbon::getByID).toList());
        }

        if(FuncGroup.funcVar != null && FuncGroup.funcVar.state == 1) {
            assert workaround != null;
            Backend.setMatrix(workaround.getBoxVec(), workaround);
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
                if(Carbon.getByID(node) instanceof FuncGroup && !FuncGroup.lookup.get(FuncGroup.funcVar.state).contains("C")) return;
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

    public List<List<Carbon>> returnRadicals(List<Carbon> longestPath) {
        List<List<Carbon>> retList = new ArrayList<>();
        for (Carbon crossection : findCrossections()) {
            List<Integer> temp = new ArrayList<>(adjacencyMap.get(crossection.id));
            try {
                temp.remove((Integer) longestPath.get(longestPath.indexOf(crossection) - 1).id);
                temp.remove((Integer) longestPath.get(longestPath.indexOf(crossection) + 1).id);
            } catch (Exception _) {
                return new ArrayList<>();
            }

            List<Carbon> radical = new ArrayList<>(temp.stream().map(Carbon::getByID).toList());

            Carbon cursor = radical.getFirst();
            Carbon head = crossection;
            while (adjacencyMap.get(cursor.id).size() != 1) {
                for (Integer i : adjacencyMap.get(cursor.id)) {
                    if (i != head.id) {
                        radical.add(Carbon.getByID(i));
                        head = cursor;
                        cursor = Carbon.getByID(i);
                    }
                }
            }

            radical.add(crossection);
            retList.add(radical);
        }

        return retList;
    }
}