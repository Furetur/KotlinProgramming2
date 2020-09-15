package homeworks.hw1.graph

import homeworks.hw1.SegmentNetworkGenerator
import homeworks.hw1.SegmentWindowsNetworkGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GraphTest {
    private data class MatrixWithCalculatedNeighbours(
        val matrix: List<List<Int>>,
        val neighbours: List<Int>,
        val targetVertexId: Int
    )

    private fun generateMatrixWithAnswer(
        size: Int,
        vertexId: Int,
        step: Int,
        withPollutedColumn: Boolean = false
    ): MatrixWithCalculatedNeighbours {
        val neighbours = mutableListOf<Int>()
        val matrix = MutableList(size) { MutableList(size) { 0 } }
        for (i in 0 until size step step) {
            matrix[vertexId][i] = 1
            neighbours.add(i)
        }
        if (withPollutedColumn) {
            for (i in 1 until size step step) {
                if (i != vertexId) {
                    matrix[i][vertexId] = 1
                }
            }
        }
        return MatrixWithCalculatedNeighbours(matrix, neighbours, vertexId)
    }

    private fun testOnMatrix(size: Int, vertexId: Int, step: Int, withPollutedColumn: Boolean) {
        val matrixWithAnswer = generateMatrixWithAnswer(size, vertexId, step, withPollutedColumn)
        val graph = Graph(matrixWithAnswer.matrix)
        assertEquals(matrixWithAnswer.neighbours, graph.vertices[vertexId].getNeighbours().map { it.id })
    }

    @Test
    fun `graphs with same matrices should be equal`() {
        val matrix1 = SegmentNetworkGenerator.makeMatrix(100)
        val graph1 = Graph(matrix1)
        val matrix2 = SegmentNetworkGenerator.makeMatrix(100)
        val graph2 = Graph(matrix2)
        assertEquals(graph1, graph2)
    }

    @Test
    fun `graphs with different matrices should not be equal`() {
        val matrix1 = SegmentNetworkGenerator.makeMatrix(100)
        val graph1 = Graph(matrix1)
        val matrix2 = SegmentNetworkGenerator.makeMatrix(10)
        val graph2 = Graph(matrix2)
        assertNotEquals(graph1, graph2)
    }

    @Test
    fun `should throw if matrix is not of square shape`() {
        val notSquare = listOf(
            listOf(0, 1, 1),
            listOf(0, 1),
        )
        assertThrows(Graph.IllegalMatrixShape::class.java) {
            Graph(notSquare)
        }
    }

    @Test
    fun `should calculate neighbours correctly in simple matrix`() {
        val matrix = listOf(
            listOf(0, 0),
            listOf(1, 0)
        )
        val graph = Graph(matrix)
        assertEquals(listOf(0), graph.vertices[1].getNeighbours().map { it.id })
    }

    @Test
    fun `should calculate neighbours correctly in simple matrix with polluted column`() {
        val matrix = listOf(
            listOf(0, 0, 0),
            listOf(1, 0, 0),
            listOf(0, 1, 0)
        )
        val graph = Graph(matrix)
        assertEquals(listOf(0), graph.vertices[1].getNeighbours().map { it.id })
    }

    @Test
    fun `should calculate neighbours correctly for big matrix`() {
        testOnMatrix(100, 3, 5, false)
    }

    @Test
    fun `should calculate neighbours correctly in big matrix with polluted column`() {
        testOnMatrix(100, 3, 5, true)
    }

    @Test
    fun `should calculate neighbours correctly in huge matrix with polluted column`() {
        testOnMatrix(10000, 500, 17, true)
    }
}
