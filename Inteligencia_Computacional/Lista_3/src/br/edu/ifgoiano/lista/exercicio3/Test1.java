package br.edu.ifgoiano.lista.exercicio3;

import java.util.*;

public class Test1 {
    record State(int a, int b) {
        @Override
        public String toString() {
            return String.format("(4L=%d, 3L=%d)", a, b);
        }
    }

    record Step(State state, String operation) {}

    static final int CAP_A   = 4;
    static final int CAP_B   = 3;
    static final int GOAL    = 2;

    static List<Step> bfs(State start) {
        Map<State, Step> visited = new HashMap<>();
        Queue<State> queue = new LinkedList<>();

        visited.put(start, new Step(start, "Estado inicial"));
        queue.add(start);

        while (!queue.isEmpty()) {
            State cur = queue.poll();

            if (cur.a() == GOAL || cur.b() == GOAL) {
                return buildPath(cur, visited);
            }

            for (State[] transition : successors(cur)) {
                State next = transition[0];
                String op   = operationName(cur, next);
                if (!visited.containsKey(next)) {
                    visited.put(next, new Step(cur, op));
                    queue.add(next);
                }
            }
        }
        return null;
    }

    static List<State[]> successors(State s) {
        int a = s.a(), b = s.b();
        List<State[]> result = new ArrayList<>();

        // 1. Encher A
        if (a < CAP_A) result.add(new State[]{ new State(CAP_A, b) });
        // 2. Encher B
        if (b < CAP_B) result.add(new State[]{ new State(a, CAP_B) });
        // 3. Esvaziar A
        if (a > 0)     result.add(new State[]{ new State(0, b) });
        // 4. Esvaziar B
        if (b > 0)     result.add(new State[]{ new State(a, 0) });
        // 5. Transferir A → B
        if (a > 0 && b < CAP_B) {
            int moved = Math.min(a, CAP_B - b);
            result.add(new State[]{ new State(a - moved, b + moved) });
        }
        // 6. Transferir B → A
        if (b > 0 && a < CAP_A) {
            int moved = Math.min(b, CAP_A - a);
            result.add(new State[]{ new State(a + moved, b - moved) });
        }

        return result;
    }

    static List<Step> buildPath(State goal, Map<State, Step> visited) {
        LinkedList<Step> path = new LinkedList<>();
        State cur = goal;

        while (true) {
            Step step = visited.get(cur);
            path.addFirst(new Step(cur, step.operation()));
            if (step.state().equals(cur)) break;
            cur = step.state();
        }
        return path;
    }

    static String operationName(State from, State to) {
        if (to.a() == CAP_A && to.b() == from.b())    return "Encher jarro 4L";
        if (to.b() == CAP_B && to.a() == from.a())    return "Encher jarro 3L";
        if (to.a() == 0     && to.b() == from.b())    return "Esvaziar jarro 4L";
        if (to.b() == 0     && to.a() == from.a())    return "Esvaziar jarro 3L";
        if (to.a() < from.a())                         return "Transferir 4L → 3L";
        if (to.b() < from.b())                         return "Transferir 3L → 4L";
        return "?";
    }

    static void printSolution(List<Step> steps) {
        if (steps == null) {
            System.out.println("Sem solução.");
            return;
        }

        int width = 45;
        String sep = "─".repeat(width);

        System.out.println("\n┌" + sep + "┐");
        System.out.printf( "│  %-43s│%n", "Solução — Problema dos Jarros");
        System.out.println("├" + sep + "┤");
        System.out.printf( "│  %-20s %-22s│%n", "Operação", "Estado (4L, 3L)");
        System.out.println("├" + sep + "┤");

        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            String marker = (step.state().a() == GOAL || step.state().b() == GOAL)
                    ? " ✓" : "  ";
            System.out.printf("│%s %-20s %-20s│%n",
                    marker, step.operation(), step.state());
        }

        System.out.println("├" + sep + "┤");
        System.out.printf( "│  Total de passos: %-24d│%n", steps.size() - 1);
        System.out.println("└" + sep + "┘\n");
    }

    public static void main(String[] args) {
        State start = new State(0, 0);
        List<Step> solution = bfs(start);
        printSolution(solution);
    }
}
