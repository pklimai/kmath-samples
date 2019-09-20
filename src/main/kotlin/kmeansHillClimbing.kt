import scientifik.kmath.real.plus
import scientifik.kmath.real.realMatrix
import scientifik.kmath.real.toMatrix
import scientifik.kmath.structures.Matrix
import java.net.URL
import kotlin.math.pow
import kotlin.math.sqrt

// Initially from: https://github.com/thomasnield/numky/blob/master/src/test/kotlin/org/nield/numky/KMeansTwoVariablesHillClimbing.kt
// Desmos graph: https://www.desmos.com/calculator/pb4ewmqdvy

fun main() {
    val points = URL("https://tinyurl.com/y25lvxug")
        .readText().split(Regex("\\r?\\n"))
        .asSequence()
        .drop(1)
        .filter { it.isNotEmpty() }
        .map { it.split(",").map { it.toDouble() }.toDoubleArray() }
        .toMatrix()

    val k = 4
    var centroids: Matrix<Double> = realMatrix(k, 2) { _, _ -> 0.0 }
    var bestLoss = Double.MAX_VALUE

    repeat(100_000) {
        val prevCentroids = centroids.copy()
        val randomAdjust = centroids.templateRandomAdjust()
        centroids += randomAdjust
        val newLoss = points.asSequence().toList()
            .map{ pt -> centroids.asSequence().toList().map{ distanceBetween(it, pt) }.min()!!.pow(2) }.sum()
        if (newLoss < bestLoss) {
            bestLoss = newLoss
            print("Updated centroids")
            println("  NewLoss = $newLoss, BestLoss=$bestLoss")
        }
        else {
            // println("No improvement   ")
            centroids = prevCentroids
        }
    }
    println("Result: ")
    println(centroids)
}

fun Matrix<Double>.templateRandomAdjust(): Matrix<Double> {
    val randomRow = randomInt(0..rowNum)
    val randomCol = randomInt(0..colNum)

    return realMatrix(rowNum, colNum) { row, col ->
        if (row == randomRow && col == randomCol) randomNormal() else 0.0
    }
}

fun distanceBetween(da1: DoubleArray, da2: DoubleArray) =
    sqrt(da1.zip(da2).map { (it.first - it.second).pow(2) }.sum())
