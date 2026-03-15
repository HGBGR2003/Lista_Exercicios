package br.edu.ifgoiano.lista.exercicio4;

import java.util.*;

public class Questao4 {
        static Map<String, List<int[]>> grafo = new HashMap<>();

        static {
            grafo.put("A", Arrays.asList(new int[]{'B', 2}, new int[]{'C', 4}));
            grafo.put("B", Arrays.asList(new int[]{'D', 3}, new int[]{'E', 1}));
            grafo.put("C", Arrays.asList(new int[]{'F', 5}));
            grafo.put("E", Arrays.asList(new int[]{'G', 2}));
            grafo.put("F", Arrays.asList(new int[]{'G', 1}));
        }

        static String bfs(String inicio, String objetivo) {
            Queue<List<String>> fila = new LinkedList<>();
            fila.add(Arrays.asList(inicio));

            while (!fila.isEmpty()) {
                List<String> caminho = fila.poll();
                String atual = caminho.get(caminho.size() - 1);

                if (atual.equals(objetivo)) return String.join(" -> ", caminho);

                List<int[]> vizinhos = grafo.getOrDefault(atual, Collections.emptyList());
                for (int[] vizinho : vizinhos) {
                    String proximo = String.valueOf((char) vizinho[0]);
                    if (!caminho.contains(proximo)) {
                        List<String> novoCaminho = new ArrayList<>(caminho);
                        novoCaminho.add(proximo);
                        fila.add(novoCaminho);
                    }
                }
            }
            return "Caminho não encontrado";
        }

        static String dfs(String inicio, String objetivo) {
            Deque<List<String>> pilha = new ArrayDeque<>();
            pilha.push(Arrays.asList(inicio));

            while (!pilha.isEmpty()) {
                List<String> caminho = pilha.pop();
                String atual = caminho.get(caminho.size() - 1);

                if (atual.equals(objetivo)) return String.join(" -> ", caminho);

                List<int[]> vizinhos = grafo.getOrDefault(atual, Collections.emptyList());
                for (int[] vizinho : vizinhos) {
                    String proximo = String.valueOf((char) vizinho[0]);
                    if (!caminho.contains(proximo)) {
                        List<String> novoCaminho = new ArrayList<>(caminho);
                        novoCaminho.add(proximo);
                        pilha.push(novoCaminho);
                    }
                }
            }
            return "Caminho não encontrado";
        }

        static String buscaCustoUniforme(String inicio, String objetivo) {
            PriorityQueue<Object[]> filaPrioridade = new PriorityQueue<>(
                    Comparator.comparingInt(a -> (int) a[0])
            );
            filaPrioridade.add(new Object[]{0, Arrays.asList(inicio)});

            while (!filaPrioridade.isEmpty()) {
                Object[] entrada = filaPrioridade.poll();
                int custo = (int) entrada[0];
                List<String> caminho = (List<String>) entrada[1];
                String atual = caminho.get(caminho.size() - 1);

                if (atual.equals(objetivo))
                    return String.join(" -> ", caminho) + " (custo total: " + custo + ")";

                List<int[]> vizinhos = grafo.getOrDefault(atual, Collections.emptyList());
                for (int[] vizinho : vizinhos) {
                    String proximo = String.valueOf((char) vizinho[0]);
                    if (!caminho.contains(proximo)) {
                        List<String> novoCaminho = new ArrayList<>(caminho);
                        novoCaminho.add(proximo);
                        filaPrioridade.add(new Object[]{custo + vizinho[1], novoCaminho});
                    }
                }
            }
            return "Caminho não encontrado";
        }

        public static void questao4() {
            System.out.println("BFS encontrou: "              + bfs("A", "G"));
            System.out.println("DFS encontrou: "              + dfs("A", "G"));
            System.out.println("Custo Uniforme encontrou: "   + buscaCustoUniforme("A", "G"));
            System.out.println("Algoritmo mais adequado: Busca de Custo Uniforme");
            System.out.println(" -> BFS e DFS ignoram pesos: nao garantem o menor custo.");
            System.out.println(" -> Custo Uniforme usa fila de prioridade e garante o caminho mais barato.");
        }
    }

