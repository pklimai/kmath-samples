import org.ojalgo.random.Normal
import scientifik.kmath.real.extractColumns
import scientifik.kmath.structures.Matrix
import java.lang.StringBuilder
import java.util.concurrent.ThreadLocalRandom

fun printMatrix(m: Matrix<Double>) {
    for (i in 0 until m.shape[0]) {
        for (j in 0 until m.shape[1]) {
            print(java.lang.String.format("%6.1f", m[i, j]))
        }
        println()
    }
}

fun DoubleArray.str(): String {
    val sb = StringBuilder("[")
    for (i in 0 until this.size) {
        sb.append(this[i])
        sb.append(", ")
    }
    sb.append("]")
    return sb.toString()
}

fun Matrix<Double>.extractRow(row: Int): DoubleArray {
    return kotlin.DoubleArray(this.shape[1]) { this[row, it] }
}

fun Matrix<Double>.asSequence(): Sequence<DoubleArray> {
    var row = 0
    return kotlin.sequences.generateSequence {
        if (row == this.shape[0]) null
        else {
            row++
            this.extractRow(row - 1)
        }
    }
}

fun Matrix<Double>.copy(): Matrix<Double> = extractColumns(0..(this.shape[1]-1))

val normal = Normal(0.0, 1.0)

fun randomNormal() = normal.get()

fun randomInt(intRange: IntRange) = ThreadLocalRandom.current().nextInt(intRange.start, intRange.endInclusive)

fun randomDouble(doubleRange: ClosedRange<Double>) =
    ThreadLocalRandom.current().nextDouble(doubleRange.start, doubleRange.endInclusive)
