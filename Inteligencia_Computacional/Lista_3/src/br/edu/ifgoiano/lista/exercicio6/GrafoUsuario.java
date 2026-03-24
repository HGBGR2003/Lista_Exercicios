package br.edu.ifgoiano.lista.exercicio6;

import java.util.*;

public class GrafoUsuario {
    public static void buscaLargura(Map<String, List<String>> grafo, String inicio, String objetivo) { 
        Queue<List<String>> fila = new LinkedList<>(); //Armazena os caminhos inteiros
        Set<String> visitados = new LinkedHashSet<>(); //Guarda os Nós, usando uma lista hash.

        fila.add(new ArrayList<>(List.of(inicio)));
        visitados.add(inicio);

        while (!fila.isEmpty()) {
            List<String> caminho = fila.poll(); //Retira o primeiro caminho da fila.
            String noAtual = caminho.get(caminho.size() - 1); //Pega o ultimo nó da fila.

            if (noAtual.equals(objetivo)) {
                System.out.println("\nRESULTADO");
                System.out.println("Caminho encontrado : " + String.join(" -> ", caminho));
                System.out.println("Nós visitados      : " + visitados.size());
                System.out.println("Ordem de visita    : " + visitados);
                return;
            }

            List<String> vizinhos = grafo.getOrDefault(noAtual, new ArrayList<>());

            for (String vizinho : vizinhos) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    List<String> novoCaminho = new ArrayList<>(caminho);
                    novoCaminho.add(vizinho);
                    fila.add(novoCaminho); //Método de verificação para ver se existe nós duplicados, e coloca para um novo caminho.
                }
            }
        }

        System.out.println("\nNenhum caminho encontrado entre '" + inicio + "' e '" + objetivo + "'.");
        System.out.println("Nós visitados: " + visitados.size());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, List<String>> grafo = new HashMap<>();

        System.out.println("BFS - Busca em Largura\n");
        System.out.println("Digite as arestas no formato:  NÓ DE ORIGEM NÓ DE DESTINO");
        System.out.println("(Digite 'fim' para encerrar a entrada)\n");

        while (true) {
            System.out.print("Aresta: ");
            String linha = sc.nextLine().trim();

            if (linha.equalsIgnoreCase("fim"))
                break;

            String[] partes = linha.split("\\s+");
            if (partes.length != 2) {
                System.out.println("  Formato inválido! Use: NÓ DE ORIGEM NÓ DE DESTINO");
                continue;
            }

            String origem = partes[0];
            String destino = partes[1];

            grafo.computeIfAbsent(origem, k -> new ArrayList<>()).add(destino);
            grafo.computeIfAbsent(destino, k -> new ArrayList<>()); //Cria o grafo para o caminho de origem até o caminho final.
        }

        if (grafo.isEmpty()) {
            System.out.println("Grafo vazio. Encerrando.");
            return; 
        }

        System.out.println("\nNós disponíveis: " + grafo.keySet());

        System.out.print("\nNó inicial  : ");
        String inicio = sc.nextLine().trim();

        System.out.print("Nó objetivo : ");
        String objetivo = sc.nextLine().trim();

        if (!grafo.containsKey(inicio)) {
            System.out.println("Nó inicial '" + inicio + "' não existe no grafo.");
            return;
        }
        if (!grafo.containsKey(objetivo)) {
            System.out.println("Nó objetivo '" + objetivo + "' não existe no grafo.");
            return; //Método de verificação.
        }

        buscaLargura(grafo, inicio, objetivo);

        sc.close();
    }
}

