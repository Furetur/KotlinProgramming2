package homeworks.hw1.graph

import java.lang.IllegalArgumentException

class Graph(matrix: List<List<Int>>) {

    companion object {
        const val DEFAULT_PRIME = 31
    }

    val matrix = AdjacencyMatrix(matrix)
    val vertices = List(matrix.size) { Vertex(it) }

    override fun equals(other: Any?): Boolean = other is Graph && other.matrix == matrix

    override fun hashCode(): Int {
        var result = matrix.hashCode()
        result = DEFAULT_PRIME * result + vertices.hashCode()
        return result
    }

    class IllegalMatrixShape : IllegalArgumentException("Matrix must be of square size")

    class AdjacencyMatrix(private val matrix: List<List<Int>>) {
        val size = matrix.size

        class IllegalVertexId(id: Int, size: Int) :
            IllegalArgumentException("Vertex id=$id is illegal in adjacency matrix of size $size x $size")

        init {
            val size = matrix.size
            for (row in matrix) {
                if (row.size != size) {
                    throw IllegalMatrixShape()
                }
            }
        }

        fun getNeighbours(vertexId: Int): List<Int> {
            if (!matrix.indices.contains(vertexId)) {
                throw IllegalVertexId(
                    vertexId,
                    size
                )
            }

            return matrix[vertexId].mapIndexedNotNull { index, value -> if (value != 0) index else null }
        }

        fun toList(): List<List<Int>> {
            return matrix
        }

        override fun equals(other: Any?): Boolean {
            return other is AdjacencyMatrix && other.matrix == matrix
        }

        override fun hashCode(): Int {
            var result = matrix.hashCode()
            result = DEFAULT_PRIME * result + size
            return result
        }
    }

    inner class Vertex(val id: Int) {
        fun getNeighbours(): List<Vertex> {
            return matrix.getNeighbours(id).map { vertices[it] }
        }

        override fun toString(): String {
            return "Vertex(id=$id)"
        }
    }
}
