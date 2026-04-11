package br.edu.ifgoiano.lista.exercicio3;

import java.util.*;

public class JarroDeAgua {
    static class Vertice {
        String nome;
        List<Vertice> arestas = new ArrayList<>();

        Vertice(String nome) {
            this.nome = nome;
        }

        void novaAresta(Vertice destino) {
            if (destino != null && !destino.equals(this) && !arestas.contains(destino)) {
                arestas.add(destino);
            }
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    static class Grafo {
        List<Vertice> vertices = new ArrayList<>();

        void novaVertice(String nome) {
            vertices.add(new Vertice(nome));
        }

        Vertice buscaVertice(String nome) {
            for (Vertice v : vertices) {
                if (v.nome.equals(nome))
                    return v;
            }
            return null;
        }
    }

    Grafo grafo;

    public void jarroDeAgua() {
        System.out.println("\nQuestão 3:");
        this.grafo = new Grafo();

        System.out.println("Estado inicial : 0L no jarro de 4L e 0L no jarro de 3L");
        System.out.println("Objetivo       : 2L no jarro de 4L e 0L no jarro de 3L");
        System.out.println("Operadores     : encher 4L | encher 3L | esvaziar 4L |"
                + " esvaziar 3L | transferir 4L→3L | transferir 3L→4L");
        System.out.println("Total de estados possíveis: 5 (0–4L) × 4 (0–3L) = 20 estados\n");

        for (int j4 = 0; j4 <= 4; j4++) {
            for (int j3 = 0; j3 <= 3; j3++) {
                grafo.novaVertice(j4 + "," + j3);
            }
        }

        for (Vertice v : grafo.vertices) {
            String[] partes = v.nome.split(",");
            int j4 = Integer.parseInt(partes[0]);
            int j3 = Integer.parseInt(partes[1]);

            v.novaAresta(grafo.buscaVertice("4," + j3));

            v.novaAresta(grafo.buscaVertice(j4 + ",3"));

            v.novaAresta(grafo.buscaVertice("0," + j3));

            v.novaAresta(grafo.buscaVertice(j4 + ",0"));

            int paraJ3 = Math.min(j4, 3 - j3);
            v.novaAresta(grafo.buscaVertice((j4 - paraJ3) + "," + (j3 + paraJ3)));

            int paraJ4 = Math.min(j3, 4 - j4);
            v.novaAresta(grafo.buscaVertice((j4 + paraJ4) + "," + (j3 - paraJ4)));
        }

        Vertice inicio = grafo.buscaVertice("0,0");
        Vertice objetivo = grafo.buscaVertice("2,0");

        System.out.println("Buscando solução para obter 2 litros no jarro de 4L (estado 2,0):");
        buscaBfs(inicio, objetivo);
    }

    void buscaBfs(Vertice inicio, Vertice objetivo) {
        Map<Vertice, Vertice> predecessores = new LinkedHashMap<>();

        Map<Vertice, String> operacoes = new LinkedHashMap<>();

        Queue<Vertice> fila = new LinkedList<>();
        predecessores.put(inicio, null);
        fila.add(inicio);

        while (!fila.isEmpty()) {
            Vertice atual = fila.poll();

            if (atual.equals(objetivo)) {
                imprimirCaminho(objetivo, predecessores, operacoes);
                return;
            }

            String[] partesAtual = atual.nome.split(",");
            int j4Atual = Integer.parseInt(partesAtual[0]);
            int j3Atual = Integer.parseInt(partesAtual[1]);

            for (Vertice vizinho : atual.arestas) {
                if (!predecessores.containsKey(vizinho)) {
                    predecessores.put(vizinho, atual);
                    operacoes.put(vizinho, nomeOperacao(j4Atual, j3Atual, vizinho));
                    fila.add(vizinho);
                }
            }
        }

        System.out.println("Sem solução encontrada.");
    }

    String nomeOperacao(int j4De, int j3De, Vertice para) {
        String[] partes = para.nome.split(",");
        int j4Para = Integer.parseInt(partes[0]);
        int j3Para = Integer.parseInt(partes[1]);

        if (j4Para == 4 && j3Para == j3De)
            return "Encher jarro 4L";
        if (j3Para == 3 && j4Para == j4De)
            return "Encher jarro 3L";
        if (j4Para == 0 && j3Para == j3De)
            return "Esvaziar jarro 4L";
        if (j3Para == 0 && j4Para == j4De)
            return "Esvaziar jarro 3L";
        if (j4Para < j4De)
            return "Transferir 4L → 3L";
        if (j3Para < j3De)
            return "Transferir 3L → 4L";
        return "?";
    }

    void imprimirCaminho(Vertice objetivo,
            Map<Vertice, Vertice> predecessores,
            Map<Vertice, String> operacoes) {

        LinkedList<Vertice> caminho = new LinkedList<>();
        Vertice cur = objetivo;
        while (cur != null) {
            caminho.addFirst(cur);
            cur = predecessores.get(cur);
        }

        int width = 50;
        String sep = "─".repeat(width);

        System.out.println("\n┌" + sep + "┐");
        System.out.printf("│  %-48s│%n", "Solução — Problema dos Jarros (BFS)");
        System.out.println("├" + sep + "┤");
        System.out.printf("│  %-5s %-24s %-17s│%n", "Passo", "Operação", "Estado (4L, 3L)");
        System.out.println("├" + sep + "┤");

        for (int i = 0; i < caminho.size(); i++) {
            Vertice v = caminho.get(i);
            String op = (i == 0) ? "Estado inicial" : operacoes.get(v);
            String estado = "(" + v.nome.replace(",", "L, ") + "L)";
            String marker = v.equals(objetivo) && i > 0 ? "✓" : " ";
            System.out.printf("│%s %-5d %-24s %-17s│%n", marker, i, op, estado);
        }

        System.out.println("├" + sep + "┤");
        System.out.printf("│  Total de passos: %-30d│%n", caminho.size() - 1);
        System.out.println("└" + sep + "┘\n");
    }
}
