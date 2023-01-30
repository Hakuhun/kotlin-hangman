import model.Game
import model.game

fun main(args: Array<String>) {

    println("Üdv az AKASZTÓFA játékban")
    do {
        val result = playGame()
        println("A játék véget ért. ")
        when (result) {
            true -> print("Gratulálok, NYERTÉL!")
            else -> print("Sajnos vesztettél. :( ${System.lineSeparator()} A helyes megfejtés: ${Game.WordToGuess}. ")
        }
        println("Szeretnél újra játszani? y/n")
        val input = readLine()!!
    } while (input != "n")
}

fun playGame(): Boolean {
    Game.startNewRound()
    do {
        game {
            round {
                misses(
                    current = Game.misses,
                    max = 10
                )
                text(
                    currentText = Game.formattedWord,
                    usedCharacters = Game.alreadyGuessedCharacters
                )
            }
        }
        var input: String? = null
        try {
            input = readLine()!!
            Game.guessCharacter(input[0])
        } catch (e: Exception) {
            println("Hiba történt a bevitel során, kérlek próbáld újra!")
        }
    } while (Game.running)

    return Game.isComplete
}
