package minesweeper.domain

sealed class Cell {
    var isOpen: Boolean = false
    abstract fun display(): Char

    class Mine : Cell() {
        override fun display(): Char {
            if (isOpen) return OPEN_CELL
            return CLOSE_CELL
        }
    }

    class Empty(val adjacentMines: Int = 0) : Cell() {
        override fun display(): Char {
            if (isOpen) return adjacentMines.digitToChar()
            return CLOSE_CELL
        }
    }

    companion object {
        private const val OPEN_CELL = '*'
        private const val CLOSE_CELL = 'C'
    }
}
