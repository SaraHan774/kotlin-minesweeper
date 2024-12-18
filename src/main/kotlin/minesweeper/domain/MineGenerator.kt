package minesweeper.domain

interface MineGenerator {
    val mineCount: MineCount

    fun generateMinePositions(totalCells: Int): Set<Int>
}

class RandomMineGenerator(override val mineCount: MineCount) : MineGenerator {
    override fun generateMinePositions(totalCells: Int): Set<Int> {
        require(mineCount.count <= totalCells) { "지뢰 개수는 전체 Cell 개수보다 적거나 같아야 합니다" }
        return (0 until totalCells).shuffled().take(mineCount.count).toSet()
    }
}
