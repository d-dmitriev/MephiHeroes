package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;
import com.heroes_task.records.Coordinates;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    // Сортировка: O(n log n)
    // Генерация координат: O(m)
    // Основной цикл: O(m)
    // Общая сложность O(n log n) + O(m) + O(m) = O(n log n) + O(m)
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Размер позиций для установки юнитов
        final int WIDTH = 3, HEIGHT = 21;
        // Максимальное число юнитов одного типа
        final int MAX_UNITS_OF_TYPE = 11;
        // Массив для накопления юнитов
        List<Unit> selectedUnits = new ArrayList<>();
        // Набранные поинты
        int currentPoints = 0;
        // Сортируем юнитов по эффективность атаки к стоимости и здоровья к стоимости
        unitList.sort(Comparator.comparingDouble(
                unit -> -(unit.getBaseAttack() / (double) unit.getCost()
                        + unit.getHealth() / (double) unit.getCost())));

        Random random = new Random();
        Set<Coordinates> occupiedCoordinates = new HashSet<>();
        for (int i = 0; i < unitList.size() * MAX_UNITS_OF_TYPE; i++) {
            // Генерация уникальной случайной координаты
            int x, y;
            String coordinateKey;
            do {
                x = random.nextInt(WIDTH);
                y = random.nextInt(HEIGHT);
            } while (occupiedCoordinates.contains(new Coordinates(x, y)));
            occupiedCoordinates.add(new Coordinates(x, y));
        }
        Coordinates[] coordinates = occupiedCoordinates.toArray(new Coordinates[0]);
        //Идем по циклу индексов от 0 до "количество типов юнитов" * "максимальное количество юнитов одного типа" и проверяем, что поинты не привышены
        for (int i = 0; i < unitList.size() * MAX_UNITS_OF_TYPE && currentPoints < maxPoints; i++) {
            // Получаем тип юнита по индексу
            Unit unit = unitList.get(Math.floorDiv(i, MAX_UNITS_OF_TYPE));
            // Клонируем юнита с новой уникальной координатой
            Unit newUnit = new Unit(
                    unit.getUnitType() + " " + (i % MAX_UNITS_OF_TYPE + 1),
                    unit.getUnitType(),
                    unit.getHealth(),
                    unit.getBaseAttack(),
                    unit.getCost(),
                    unit.getAttackType(),
                    new HashMap<>(unit.getAttackBonuses()),
                    new HashMap<>(unit.getDefenceBonuses()),
                    coordinates[i].x(),
                    coordinates[i].y());
            // Добавляем в массив
            selectedUnits.add(newUnit);
            // Накапливаем поинты
            currentPoints += newUnit.getCost();
        }
        // Создаем новый объект армии
        Army computerArmy = new Army(selectedUnits);
        // Устанавливаем очки в армию ?
        computerArmy.setPoints(currentPoints);
        return computerArmy;
    }
}