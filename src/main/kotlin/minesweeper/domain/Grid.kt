package minesweeper.domain

class Grid(
    private val dimension: Dimension,
    private val mineGenerator: MineGenerator,
) {
    val cells: List<List<Cell>> = initializeCells()

    private fun initializeCells(): List<List<Cell>> {
        val totalCells = dimension.totalCells()
        val minePositions = mineGenerator.generateMinePositions(totalCells)
        val tempCells = createInitialCells(minePositions)
        return calculateAdjacentMinesAndGet(tempCells)
    }

    private fun createInitialCells(minePositions: Set<Int>): List<List<Cell>> {
        return (0 until dimension.height).map { row ->
            (0 until dimension.width).map { col ->
                val index = row * dimension.width + col
                createCell(isIndexMine = index in minePositions)
            }
        }
    }

    private fun createCell(isIndexMine: Boolean): Cell {
        return if (isIndexMine) Cell(Mine()) else Cell(Empty())
    }

    private fun calculateAdjacentMinesAndGet(tempCells: List<List<Cell>>): List<List<Cell>> {
        return tempCells.mapIndexed { row, rowCells ->
            rowCells.mapIndexed { col, colCell ->
                if (colCell.isMine().not()) {
                    val mineCount = countAdjacentMines(row, col, tempCells)
                    Cell(Empty(mineCount))
                } else {
                    colCell
                }
            }
        }
    }

    private fun countAdjacentMines(
        row: Int,
        col: Int,
        cells: List<List<Cell>>,
    ): Int {
        return (-1..1).flatMap { dx ->
            (-1..1).mapNotNull { dy ->
                if (dx == 0 && dy == 0) {
                    null
                } else {
                    checkAndCountMine(row + dx, col + dy, cells)
                }
            }
        }.count { it }
    }

    private fun checkAndCountMine(
        row: Int,
        col: Int,
        cells: List<List<Cell>>,
    ): Boolean {
        val isRowInBounds = row in 0 until dimension.height
        val isColInBounds = col in 0 until dimension.width
        return isRowInBounds && isColInBounds && cells[row][col].isMine()
    }
}
