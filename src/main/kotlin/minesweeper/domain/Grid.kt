package minesweeper.domain

class Grid(
    private val dimension: Dimension,
    private val mineCount: MineCount,
    private val mineGenerator: MineGenerator,
) {
    val cells: List<List<Cell>> = initializeCells()

    private fun initializeCells(): List<List<Cell>> {
        val totalCells = dimension.totalCells()
        val minePositions = mineGenerator.generateMinePositions(totalCells, mineCount.count)
        val initialCells = createInitialCells(minePositions) // Initial mine placement
        return mapAdjacentCounts(initialCells)
    }

    private fun createInitialCells(minePositions: Set<Int>): List<List<Cell>> {
        return (0 until dimension.height).map { row ->
            (0 until dimension.width).map { col ->
                val currentPosition = row * dimension.width + col
                val isMine = currentPosition in minePositions
                Cell.from(isMine)
            }
        }
    }

    private fun mapAdjacentCounts(tempCells: List<List<Cell>>): List<List<Cell>> {
        return tempCells.mapIndexed { row, rowCells ->
            rowCells.mapIndexed { col, cell ->
                Cell.from(cell.isMine(), countAdjacentMines(row, col, tempCells))
            }
        }
    }

    private fun countAdjacentMines(
        row: Int,
        col: Int,
        cells: List<List<Cell>>,
    ): Int {
        fun isWithinBounds(
            row: Int,
            col: Int,
            cells: List<List<Cell>>,
        ) = row in cells.indices && col in cells[row].indices

        return CoordinateOffset.X.offsets.sumOf { dx ->
            CoordinateOffset.Y.offsets.count { dy ->
                val isCurrentCell = dx == 0 && dy == 0
                val adjacentRow = row + dx
                val adjacentCol = col + dy
                val isValidCell = isCurrentCell.not() && isWithinBounds(adjacentRow, adjacentCol, cells)
                val isAdjacentCellMine = isValidCell && cells[adjacentRow][adjacentCol].isMine()
                isAdjacentCellMine
            }
        }
    }

    enum class CoordinateOffset(vararg val offsets: Int) {
        X(-1, 0, 1),
        Y(-1, 0, 1)
    }
}
