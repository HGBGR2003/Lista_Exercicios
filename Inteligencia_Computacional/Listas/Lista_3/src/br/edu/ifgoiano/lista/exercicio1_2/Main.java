package br.edu.ifgoiano.lista.exercicio1_2;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Grafo g = new Grafo(true);

        g.addEdge("A", "B", 1);
        g.addEdge("A", "C", 1);
        g.addEdge("B", "D", 1);
        g.addEdge("B", "E", 1);
        g.addEdge("C", "F", 1);
        g.addEdge("E", "G", 1);
        g.addEdge("F", "G", 1);
        g.addNo("D");
        g.addNo("G");

        List<Vertice> vertices = g.getVertices();

        for (Vertice vertice : vertices) {
            vertice.verArestas();
        }

        System.out.println("\nExercício 1 – BFS");
        System.out.println("Ordem de expansão (BFS): " + g.bfs("A"));
        System.out.println("Caminho até G (BFS): " + g.bfsCaminho("A", "G"));
        System.out.println("Níveis até G: " + g.bfsNiveis("A", "G"));

        System.out.println("\nExercício 2 – DFS");
        System.out.println("Ordem de expansão (DFS): " + g.dfs("A"));
        System.out.println("Caminho até G (DFS): " + g.dfsCaminho("A", "G"));

        List<String> caminhoBFS = g.bfsCaminho("A", "G");
        List<String> caminhoDFS = g.dfsCaminho("A", "G");
        System.out.println("Caminho BFS == DFS? " + caminhoBFS.equals(caminhoDFS));
    }

}