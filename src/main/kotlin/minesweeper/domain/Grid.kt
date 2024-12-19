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
        val tempCells = createInitialCells(minePositions) // Initial mine placement
        return mapAdjacentCounts(tempCells)
    }

    private fun createInitialCells(minePositions: Set<Int>): List<List<Cell>> {
        return (0 until dimension.height).map { row ->
            (0 until dimension.width).map { col ->
                createInitialCell(row, col, minePositions)
            }
        }
    }

    private fun createInitialCell(
        row: Int,
        col: Int,
        minePositions: Set<Int>,
    ): Cell {
        val currentPosition = row * dimension.width + col
        val isMine = currentPosition in minePositions
        return if (isMine) {
            Cell.Mine()
        } else {
            Cell.Empty(0)
        }
    }

    private fun mapAdjacentCounts(tempCells: List<List<Cell>>): List<List<Cell>> {
        return tempCells.mapIndexed { row, rowCells ->
            rowCells.mapIndexed { col, cell ->
                createAdjacentCountCell(cell, row, col, tempCells)
            }
        }
    }

    private fun createAdjacentCountCell(
        cell: Cell,
        row: Int,
        col: Int,
        tempCells: List<List<Cell>>,
    ): Cell {
        return if (cell is Cell.Mine) {
            cell
        } else {
            val count = countAdjacentMines(row, col, tempCells)
            Cell.Empty(count)
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

        return (-1..1).sumOf { dx ->
            (-1..1).count { dy ->
                val isCurrentCell = dx == 0 && dy == 0
                val adjacentRow = row + dx
                val adjacentCol = col + dy
                val isValidCell = isCurrentCell.not() && isWithinBounds(adjacentRow, adjacentCol, cells)
                val isAdjacentCellMine = isValidCell && cells[adjacentRow][adjacentCol] is Cell.Mine
                isAdjacentCellMine
            }
        }
    }

    fun openCell(row: Int, col: Int): Boolean {
        return if (cells[row][col] is Cell.Mine) {
            false
        } else {
            reveal(row, col)
            true
        }
    }

    private fun reveal(row: Int, col: Int) {
        // 탈출조건 - 열려있거나, 지뢰거나, 더이상 인접 카운트가 0이 아닐때
        if (cells[row][col].isOpen || cells[row][col] is Cell.Mine) return
        // 열려있는 상태를 변경
        cells[row][col].isOpen = true
        // 지뢰 카운트 0 인 곳이라면 재귀 탐색
        if ((cells[row][col] as Cell.Empty).adjacentMines == 0) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    val newRow = row + dx
                    val newCol = col + dy
                    if (newRow in cells.indices && newCol in cells[newRow].indices) {
                        reveal(newRow, newCol)
                    }
                }
            }
        }
    }
}
