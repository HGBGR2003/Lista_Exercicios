package br.edu.ifgoiano.lista.exercicio1_2;

import java.util.List;

public class Vertice {
    private String nome;
    private List<Edge> arestas;

    public Vertice(String nome, List<Edge> arestas) {
        this.nome = nome;
        this.arestas = arestas;
    }

    public void verArestas() {
        System.out.print(nome + " -> ");
        if (arestas.isEmpty()) {
            System.out.println("-");
        } else {
            for (Edge edge : arestas) {
                System.out.print(edge.getDestino() + " ");
            }
            System.out.println();
        }
    }

}
