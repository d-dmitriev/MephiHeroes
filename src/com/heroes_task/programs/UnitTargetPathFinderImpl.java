package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
import com.heroes_task.records.Coordinates;

import java.util.*;

// Инициализация занятых клеток: O(U)
// Алгоритм нахождения кратчайшего пути: O(W * H * log(W * H))
// Построение пути: O(W * H)
// Общая сложность: O(U)+O(W*H*log(W*H))+O(W*H)=O(W*H*log(W*H))

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {1, 1}, {-1, 1}, {1, -1}};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Set<Coordinates> occupiedCells = getOccupiedCells(existingUnitList, attackUnit, targetUnit);

        // Мапы с расстояниями
        Map<Coordinates, Integer> distances = new HashMap<>();
        Map<Coordinates, Coordinates> previous = new HashMap<>();
        PriorityQueue<Coordinates> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Стартовый узел
        Coordinates startNode = new Coordinates(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        distances.put(startNode, 0);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Coordinates current = queue.poll();

            // Если достигли целевой точки, прерываем цикл
            if (current.x() == targetUnit.getxCoordinate() && current.y() == targetUnit.getyCoordinate()) break;

            for (int[] dir : DIRECTIONS) {
                Coordinates neighbor = new Coordinates(current.x() + dir[0], current.y() + dir[1]);
                if (isValid(neighbor, occupiedCells)) {
                    int newDistance = distances.getOrDefault(current, Integer.MAX_VALUE) + 1;
                    if (newDistance < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return createPath(previous, targetUnit);
    }

    private Set<Coordinates> getOccupiedCells(List<Unit> existingUnitList, Unit attackUnit, Unit targetUnit) {
        Set<Coordinates> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && !unit.equals(attackUnit) && !unit.equals(targetUnit)) {
                occupiedCells.add(new Coordinates(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }
        return occupiedCells;
    }

    private boolean isValid(Coordinates neighbor, Set<Coordinates> occupiedCells) {
        return neighbor.x() >= 0 && neighbor.x() < WIDTH && neighbor.y() >= 0 && neighbor.y() < HEIGHT && !occupiedCells.contains(neighbor);
    }

    private List<Edge> createPath(Map<Coordinates, Coordinates> previous, Unit targetUnit) {
        List<Edge> edges = new ArrayList<>();
        Coordinates coordinates = new Coordinates(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        while (coordinates != null) {
            edges.add(new Edge(coordinates.x(), coordinates.y()));
            coordinates = previous.get(coordinates);
        }

        Collections.reverse(edges);
        return edges;
    }
}