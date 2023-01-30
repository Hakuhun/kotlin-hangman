package model

class GameSettings {
    companion object {
        private const val PATH: String = "/words.txt"
        const val MAX_NUMBER_OF_MISSES = 10
    }

    val words: List<String>?
        get() = this::class.java.getResourceAsStream(PATH)?.bufferedReader()?.readLines()
}

object Game {
    private val words: List<String>? = GameSettings().words
    private var wordToGuess: String? = null
    private val characterGuessed = mutableSetOf<Char>()
    private val correctCharacters = mutableListOf<Char>()
    var misses: Int = 0

    fun startNewRound() {
        WordToGuess = words?.random()
        misses = 0
        characterGuessed.clear()
        correctCharacters.clear()
    }

    var WordToGuess: String?
        get() = wordToGuess ?: ""
        private set(value) {
            wordToGuess = value
        }

    val running: Boolean
        get() {
            if (isComplete) return false
            return misses <= GameSettings.MAX_NUMBER_OF_MISSES
        }

    val isComplete: Boolean
        get() = WordToGuess == unformattedWord

    val formattedWord: String
        get() = completeWord(CharArray(WordToGuess?.length!!) { '_' })

    private val unformattedWord: String
        get() = completeWord(CharArray(WordToGuess?.length!!))

    val alreadyGuessedCharacters: String
        get() = characterGuessed.joinToString(separator = ", ")

    fun guessCharacter(character: Char) {
        when {
            WordToGuess?.containsIgnoreCase(character)!! && characterGuessed.containsIgnoreCase(character) -> misses++
            WordToGuess?.containsIgnoreCase(character)!! -> correctCharacters.add(character)
            else -> misses++
        }
        characterGuessed.add(character)
    }

    private fun completeWord(displayArray: CharArray): String {
        WordToGuess?.forEachIndexed { index, character ->
            if (correctCharacters.containsIgnoreCase(character)) {
                displayArray[index] = character
            }
        }

        return displayArray.concatToString()
    }

}

fun String?.containsIgnoreCase(character: Char): Boolean {
    if (this == null) {
        return false
    }
    return (character.lowercase() in this) || (character.uppercase() in this)
}

fun Collection<Char>?.containsIgnoreCase(character: Char): Boolean {
    if ((this == null) || isEmpty()) {
        return false
    }
    return (character.lowercaseChar() in this) || (character.uppercaseChar() in this)
}
