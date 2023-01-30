package model

class MissStateBuilder(
    private var misses: Int = 0,
    private var max: Int = 10
) {
    companion object {
        // Reset
        const val RESET = "\u001b[0m" // Text Reset

        // Regular Colors
        const val RED = "\u001b[0;31m" // RED
        const val BLUE = "\u001b[0;34m" // BLUE
        const val GREEN = "\u001b[0;32m" // GREEN
        const val YELLOW = "\u001b[0;33m" // YELLOW
    }

    private val quarter: Double
        get() = max * 0.25

    private val half: Double
        get() = max * 0.5

    private val threeQuarter: Double
        get() = max * 0.75

    private val dangerZoneColor: String
        get() = when (misses.toDouble()) {
            in 0.0..quarter -> GREEN
            in quarter..half -> BLUE
            in half..threeQuarter -> YELLOW
            else -> RED
        }

    fun render(): String = "$dangerZoneColor $misses / 10 $RESET"
}

class TextStateBuilder(
    var currentText: String,
    var usedCharacters: String
){
    fun render() : String = "$currentText | Eddig felhasznált betük: $usedCharacters |"
}

class GameBuilder() {
    private val rounds = mutableListOf<Round>()
    operator fun Round.unaryPlus() {
        rounds += this
    }

    fun round(setup: RoundBuilder.() -> Unit = {}) {
        val roundBuilder = RoundBuilder()
        roundBuilder.setup()
        rounds += roundBuilder.build()
    }

    fun render(){
        rounds.forEach { println(it.render()) }
    }
}

class RoundBuilder() {
    private var textsState = String()
    private var missState = String()

    fun build(): Round {
        return Round(textsState, missState)
    }

    fun text(currentText: String = "", usedCharacters: String, setup: TextStateBuilder.() -> Unit = {}) {
        val textStateBuilder = TextStateBuilder(currentText, usedCharacters)
        textStateBuilder.setup()
        textsState = textStateBuilder.render()
    }

    fun misses(current: Int, max: Int, setup: MissStateBuilder.() -> Unit = {}) {
        val missStateBuilder = MissStateBuilder(current, max)
        missStateBuilder.setup()
        missState = missStateBuilder.render()
    }
}

data class Round(val text: String, val misses: String) {
    fun render() : String = "$text $misses"
}

fun game(setup: GameBuilder.() -> Unit): Unit {
    val gameBuilder = GameBuilder()
    gameBuilder.setup()
    gameBuilder.render()
}