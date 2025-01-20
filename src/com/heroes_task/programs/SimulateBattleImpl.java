package com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

// Проверка живых юнитов: O(1).
// Инициализация приоритетных очередей: O(NlogN+MlogM).
// Симуляция раунда: O(P log P)
// Удаление мертвых юнитов: общая сложность O(P log P)
// Итого общая сложность O(P^2 log P)

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        // Получаем юниты обеих армий
        Set<Unit> playerUnits = new HashSet<>(playerArmy.getUnits());
        Set<Unit> computerUnits = new HashSet<>(computerArmy.getUnits());

        int round = 1;
        // Пока есть живые юниты в обеих армиях
        while (true) {
            // Симулируем раунд
            System.out.println("Start round " + round + " player " + playerUnits.size() + " computer " + computerUnits.size());
            Queue<Unit> player = new PriorityQueue<>(Comparator.comparingInt(Unit::getBaseAttack).reversed());
            Queue<Unit> computer = new PriorityQueue<>(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            // Добавляем всех живых юнитов в очередь ходов
            player.addAll(playerUnits);
            computer.addAll(computerUnits);

            while (true) {
                Unit currentUnit = player.poll();
                if (currentUnit == null || computerUnits.isEmpty()) break;
                unitAttack(computerUnits, computer, currentUnit, true);

                currentUnit = computer.poll();
                if (currentUnit == null || playerUnits.isEmpty()) break;
                unitAttack(playerUnits, player, currentUnit, false);
            }
            System.out.println("End round " + round);
            if (playerUnits.isEmpty() || computerUnits.isEmpty()) break;
            round++;
        }

        // Проверяем результат боя
        if (computerUnits.isEmpty()) {
            System.out.println("Player wins!");
        } else {
            System.out.println("Computer wins!");
        }
    }

    private void unitAttack(Set<Unit> targetUnits, Queue<Unit> targetQueue, Unit currentUnit, boolean isPlayer) throws InterruptedException {
        System.out.println((isPlayer ? "Player " : "Computer ") + currentUnit.getUnitType() + " run");
        // Определяем цель атаки
        Unit target = currentUnit.getProgram().attack();
        if (target != null && !target.isAlive()) {
            // Если цель атаки существует и не жива
            targetUnits.remove(target);
            targetQueue.remove(target);
            System.out.println((isPlayer ? "Computer " : "Player ") + target.getUnitType() + " not alive");
        } else if (target != null) {
            System.out.println((isPlayer ? "Computer " : "Player ") + target.getUnitType() + " health " + target.getHealth());
        } else {
            System.out.println("Empty target!!!!!");
        }
        // Логируем атаку
        printBattleLog.printBattleLog(currentUnit, target);
    }
}