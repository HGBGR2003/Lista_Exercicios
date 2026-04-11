package br.edu.ifgoiano.lista.exercicio5;

import java.util.*;

public class CustoUniforme {

    public static int[] custo_uniforme(Map<String, Map<String, Integer>> grafo,
            String inicio, String objetivo) {
        PriorityQueue<Object[]> fila = new PriorityQueue<>(
                Comparator.comparingInt(a -> (int) a[0]));

        fila.add(new Object[] { 0, inicio, new ArrayList<>(List.of(inicio)) });
        Set<String> visitados = new HashSet<>();

        while (!fila.isEmpty()) {
            Object[] atual = fila.poll();
            int custo = (int) atual[0];
            String noAtual = (String) atual[1];
            List<String> caminho = (List<String>) atual[2];

            if (visitados.contains(noAtual))
                continue;
            visitados.add(noAtual);

            if (noAtual.equals(objetivo)) {
                System.out.println("Menor custo: " + custo);
                System.out.println("Caminho: " + String.join(" -> ", caminho));
                return new int[] { custo };
            }

            Map<String, Integer> vizinhos = grafo.getOrDefault(noAtual, new HashMap<>());
            for (Map.Entry<String, Integer> entry : vizinhos.entrySet()) {
                String vizinho = entry.getKey();
                int peso = entry.getValue();

                if (!visitados.contains(vizinho)) {
                    List<String> novoCaminho = new ArrayList<>(caminho);
                    novoCaminho.add(vizinho);
                    fila.add(new Object[] { custo + peso, vizinho, novoCaminho });
                }
            }
        }

        System.out.println("Nenhum caminho encontrado.");
        return new int[] { Integer.MAX_VALUE };
    }

    public static void main(String[] args) {
        Map<String, Map<String, Integer>> grafo = new HashMap<>();

        grafo.put("A", new HashMap<>(Map.of("B", 2, "C", 4)));
        grafo.put("B", new HashMap<>(Map.of("D", 3, "E", 1)));
        grafo.put("C", new HashMap<>(Map.of("F", 5)));
        grafo.put("D", new HashMap<>());
        grafo.put("E", new HashMap<>(Map.of("G", 2)));
        grafo.put("F", new HashMap<>(Map.of("G", 1)));
        grafo.put("G", new HashMap<>());

        custo_uniforme(grafo, "A", "G");
    }
}
