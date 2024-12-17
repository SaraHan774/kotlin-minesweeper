package minesweeper.controller

import minesweeper.view.ResultView
import minesweeper.domain.Dimension
import minesweeper.domain.Grid
import minesweeper.domain.MineCount
import minesweeper.view.InputView

object MineSweeperController {
    fun start() {
        val height = InputView.readHeight()
        val width = InputView.readWidth()
        val mineCount = InputView.readMineCount()
        val grid = Grid(
            dimension = Dimension(height, width),
            mineCount = MineCount(mineCount)
        )
        ResultView.renderBoard(grid)
    }
}