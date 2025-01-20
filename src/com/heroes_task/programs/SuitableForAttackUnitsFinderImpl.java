package com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

// Итерация по колонкам: O(1)
// Поиск в колонке: O(m)
// Общая сложность: O(3*m)=O(m)

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        if (isLeftArmyTarget) {
            for (int i = unitsByRow.size() - 1; i >= 0; i--) {
                if (findUnitsInColumn(unitsByRow.get(i), suitableUnits)) break;
            }
        } else {
            for (int i = 0; i <= unitsByRow.size() - 1; i++) {
                if (findUnitsInColumn(unitsByRow.get(i), suitableUnits)) break;
            }
        }
        return suitableUnits;
    }

    private static boolean findUnitsInColumn(List<Unit> column, List<Unit> suitableUnits) {
        List<Unit> candidates = column.stream().filter(Unit::isAlive).toList();
        if (!candidates.isEmpty()) {
            suitableUnits.addAll(candidates);
            return true;
        }
        return false;
    }
}