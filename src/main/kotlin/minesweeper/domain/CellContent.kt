package minesweeper.domain

sealed class Cell {
    var isOpen: Boolean = false
    abstract fun display(): Char

    class Mine : Cell() {
        override fun display(): Char {
            return if (isOpen) OPEN_CELL else CLOSE_CELL
        }
    }

    class Empty(val adjacentMines: Int) : Cell() {
        override fun display(): Char {
            return if (isOpen) adjacentMines.digitToChar() else CLOSE_CELL
        }
    }

    companion object {
        private const val OPEN_CELL = '*'
        private const val CLOSE_CELL = 'C'
    }
}
