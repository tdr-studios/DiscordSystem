package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.tdrstudios.discordsystem.api.modules.Module;

import java.util.Collection;
import java.util.List;

public class DependencySorter {
    static class Vertex {
        public String name;

        public Vertex(String name) {
            this.name = name;
        }
    }

    public static class GraphTS {
        private final int MAX_VERTS = 20;

        private Vertex vertexList[]; // list of vertices

        private int matrix[][]; // adjacency matrix

        private int numVerts; // current number of vertices

        private String sortedArray[];

        public GraphTS() {
            vertexList = new Vertex[MAX_VERTS];
            matrix = new int[MAX_VERTS][MAX_VERTS];
            numVerts = 0;
            for (int i = 0; i < MAX_VERTS; i++)
                for (int k = 0; k < MAX_VERTS; k++)
                    matrix[i][k] = 0;
            sortedArray = new String[MAX_VERTS]; // sorted vert labels
        }

        public void addVertex(String name) {
            vertexList[numVerts++] = new Vertex(name);
        }

        public void addEdge(int start, int end) {
            matrix[start][end] = 1;
        }

        public void displayVertex(int v) {
            System.out.print(vertexList[v].name);
        }

        public void topo() // toplogical sort
        {
            int orig_nVerts = numVerts;

            while (numVerts > 0) // while vertices remain,
            {
                // get a vertex with no successors, or -1
                int currentVertex = noSuccessors();
                if (currentVertex == -1) // must be a cycle
                {
                    System.out.println("ERROR: Graph has cycles");
                    return;
                }
                // insert vertex label in sorted array (start at end)
                sortedArray[numVerts - 1] = vertexList[currentVertex].name;

                deleteVertex(currentVertex); // delete vertex
            }

            // vertices all gone; display sortedArray
            System.out.print("Topologically sorted order: ");
            for (int j = 0; j < orig_nVerts; j++)
                System.out.print(sortedArray[j]);
            System.out.println("");
        }

        public int noSuccessors() // returns vert with no successors (or -1 if no such verts)
        {
            boolean isEdge; // edge from row to column in adjMat

            for (int row = 0; row < numVerts; row++) {
                isEdge = false; // check edges
                for (int col = 0; col < numVerts; col++) {
                    if (matrix[row][col] > 0) // if edge to another,
                    {
                        isEdge = true;
                        break; // this vertex has a successor try another
                    }
                }
                if (!isEdge) // if no edges, has no successors
                    return row;
            }
            return -1; // no
        }

        public void deleteVertex(int delVert) {
            if (delVert != numVerts - 1) // if not last vertex, delete from vertexList
            {
                for (int j = delVert; j < numVerts - 1; j++)
                    vertexList[j] = vertexList[j + 1];

                for (int row = delVert; row < numVerts - 1; row++)
                    moveRowUp(row, numVerts);

                for (int col = delVert; col < numVerts - 1; col++)
                    moveColLeft(col, numVerts - 1);
            }
            numVerts--; // one less vertex
        }

        private void moveRowUp(int row, int length) {
            for (int col = 0; col < length; col++)
                matrix[row][col] = matrix[row + 1][col];
        }

        private void moveColLeft(int col, int length) {
            for (int row = 0; row < length; row++)
                matrix[row][col] = matrix[row][col + 1];
        }
    }

    public static Collection<Module> sortModules(List<Module> modules) {
        BiMap<Integer, String> moduleMap = HashBiMap.create();
        GraphTS graph = new GraphTS();
        for (int i = 0; i < modules.size(); i++){
            Module module = modules.get(i);
            moduleMap.put(i, module.getName());
            graph.addVertex(module.getName().toLowerCase());
        }
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            for (String dependency : module.getSoftDependencies()) {
                dependency = dependency.toLowerCase();
                Integer depKey = moduleMap.inverse().getOrDefault(dependency, null);
                if (depKey == null) continue;
                graph.addEdge(i, depKey);
            }

            for (String dependency : module.getDependencies()) {
                dependency = dependency.toLowerCase();
                Integer depKey = moduleMap.inverse().getOrDefault(dependency, null);
                if (depKey == null) continue;
                graph.addEdge(i, depKey);
            }
        }
        graph.topo();
        return null;
    }
}
