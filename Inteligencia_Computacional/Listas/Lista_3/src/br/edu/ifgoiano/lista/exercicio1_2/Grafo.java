package br.edu.ifgoiano.lista.exercicio1_2;

import java.util.*;

public class Grafo {
    private Map<String, List<Edge>> listaAdjacencia;
    private boolean direcao;

    public Grafo(boolean direcao) {
        this.direcao = direcao;
        this.listaAdjacencia = new HashMap<>();
    }

    public void addNo(String node) {
        listaAdjacencia.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(String origem, String destino, int peso) {
        addNo(origem);
        addNo(destino);

        listaAdjacencia.get(origem).add(new Edge(destino, peso));

        if (!direcao) {
            listaAdjacencia.get(destino).add(new Edge(origem, peso));
        }
    }

    public List<Edge> getListaAdjacenci(String node) {
        return listaAdjacencia.getOrDefault(node, Collections.emptyList());
    }

    public List<String> bfs(String inicio) {
        Set<String> visitados = new LinkedHashSet<>();
        Queue<String> fila = new LinkedList<>();

        fila.add(inicio);
        visitados.add(inicio);

        while (!fila.isEmpty()) {
            String no = fila.poll();
            for (Edge edge : getListaAdjacenci(no)) {
                if (!visitados.contains(edge.getDestino())) {
                    visitados.add(edge.getDestino());
                    fila.add(edge.getDestino());
                }
            }
        }
        return new ArrayList<>(visitados);
    }

    public List<String> dfs(String inicio) {
        Set<String> visitados = new LinkedHashSet<>();
        dfsRecursivo(inicio, visitados);
        return new ArrayList<>(visitados);
    }

    private void dfsRecursivo(String no, Set<String> visitados) {
        visitados.add(no);
        for (Edge edge : getListaAdjacenci(no)) {
            if (!visitados.contains(edge.getDestino())) {
                dfsRecursivo(edge.getDestino(), visitados);
            }
        }
    }

    public List<Vertice> getVertices() {
        List<Vertice> vertices = new ArrayList<>();
        for (Map.Entry<String, List<Edge>> entry : listaAdjacencia.entrySet()) {
            vertices.add(new Vertice(entry.getKey(), entry.getValue()));
        }
        return vertices;
    }

    // Resposta Questão 2

    public List<String> bfsCaminho(String inicio, String objetivo) {
        Map<String, String> predecessores = new LinkedHashMap<>();
        Queue<String> fila = new LinkedList<>();
        Set<String> visitados = new LinkedHashSet<>();

        fila.add(inicio);
        visitados.add(inicio);
        predecessores.put(inicio, null);

        while (!fila.isEmpty()) {
            String no = fila.poll();
            if (no.equals(objetivo))
                break;

            for (Edge edge : getListaAdjacenci(no)) {
                String destino = edge.getDestino();
                if (!visitados.contains(destino)) {
                    visitados.add(destino);
                    fila.add(destino);
                    predecessores.put(destino, no);
                }
            }
        }

        List<String> caminho = new ArrayList<>();
        String atual = objetivo;
        while (atual != null) {
            caminho.add(0, atual);
            atual = predecessores.get(atual);
        }
        return caminho.get(0).equals(inicio) ? caminho : Collections.emptyList();
    }

    public int bfsNiveis(String inicio, String objetivo) {
        Map<String, Integer> niveis = new HashMap<>();
        Queue<String> fila = new LinkedList<>();

        fila.add(inicio);
        niveis.put(inicio, 0);

        while (!fila.isEmpty()) {
            String no = fila.poll();
            if (no.equals(objetivo))
                return niveis.get(no);

            for (Edge edge : getListaAdjacenci(no)) {
                String destino = edge.getDestino();
                if (!niveis.containsKey(destino)) {
                    niveis.put(destino, niveis.get(no) + 1);
                    fila.add(destino);
                }
            }
        }
        return -1;
    }

    public List<String> dfsCaminho(String inicio, String objetivo) {
        List<String> caminho = new ArrayList<>();
        Set<String> visitados = new LinkedHashSet<>();
        dfsCaminhoRecursivo(inicio, objetivo, visitados, caminho);
        return caminho;
    }

    private boolean dfsCaminhoRecursivo(String no, String objetivo, Set<String> visitados, List<String> caminho) {
        visitados.add(no);
        caminho.add(no);
        if (no.equals(objetivo))
            return true;

        for (Edge edge : getListaAdjacenci(no)) {
            String destino = edge.getDestino();
            if (!visitados.contains(destino)) {
                if (dfsCaminhoRecursivo(destino, objetivo, visitados, caminho))
                    return true;
            }
        }
        caminho.remove(caminho.size() - 1);
        return false;
    }

}
