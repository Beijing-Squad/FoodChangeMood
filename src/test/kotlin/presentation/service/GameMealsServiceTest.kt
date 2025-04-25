package org.beijing.presentation.service

import io.mockk.*
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO

class GameMealsServiceTest {
    private lateinit var gamesMealsUseCase: ManageMealsGamesUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var gameMealsService: GameMealsService

    @BeforeEach
    fun setup() {
        gamesMealsUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        gameMealsService = GameMealsService(gamesMealsUseCase, consoleIO)
    }

    // region ingredient game


    @Test
    fun `should handle successful ingredient game round when user provides correct answer`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val initialState = IngredientGameState()
        val updatedState = IngredientGameState(score = 1000, correctAnswers = 1)

        every { gamesMealsUseCase.isGameOver(any()) } returnsMany listOf(false, true)
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to initialState)
        every { gamesMealsUseCase.checkAnswer(1, round, initialState) } returns (true to updatedState)
        every { consoleIO.readInput() } returnsMany listOf("2", "1", "0") // Select option 2, answer 1, exit
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.showService()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Correct!") })
            consoleIO.viewWithLine(match { it.contains("Score: 1000") })
        }
    }

    @Test
    fun `should handle wrong answer when user provides incorrect option`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()

        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { gamesMealsUseCase.checkAnswer(2, round, state) } returns (false to state)
        every { consoleIO.readInput() } returnsMany listOf("2", "2", "0") // Select option 2, answer 2, exit

        // When
        gameMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
            consoleIO.viewWithLine("Guess the correct ingredient for each meal. One wrong answer ends the game!")
            consoleIO.viewWithLine("\n🍽 Meal: Pizza")
            consoleIO.viewWithLine("Which of the following is one of its ingredients? 🤔")
            consoleIO.viewWithLine("1. Cheese")
            consoleIO.viewWithLine("2. Wrong1")
            consoleIO.viewWithLine("3. Wrong2")
            consoleIO.view("Select an option (1-3): ")
            consoleIO.viewWithLine("❌ Wrong! The correct answer was: Cheese")
            consoleIO.viewWithLine("\n\uD83C\uDFAF Final Score: 0")
            consoleIO.viewWithLine("👿 Game Over!")
        }
    }

    @Test
    fun `should handle non-numeric input when user provides text instead of number`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()

        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returnsMany listOf("2", "a", "0") // Select option 2, answer "a", exit
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
            consoleIO.viewWithLine("Guess the correct ingredient for each meal. One wrong answer ends the game!")
            consoleIO.viewWithLine("\n🍽 Meal: Pizza")
            consoleIO.viewWithLine("Which of the following is one of its ingredients? 🤔")
            consoleIO.viewWithLine("1. Cheese")
            consoleIO.viewWithLine("2. Wrong1")
            consoleIO.viewWithLine("3. Wrong2")
            consoleIO.view("Select an option (1-3): ")
            consoleIO.viewWithLine(match { it.contains("Invalid input") })
            consoleIO.viewWithLine("\n\uD83C\uDFAF Final Score: 0")
            consoleIO.viewWithLine("👿 Game Over!")
        }
    }

    @Test
    fun `should handle null or empty input when user provides no input`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()

        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returnsMany listOf("2", "", "0")
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
            consoleIO.viewWithLine("Guess the correct ingredient for each meal. One wrong answer ends the game!")
            consoleIO.viewWithLine("\n🍽 Meal: Pizza")
            consoleIO.viewWithLine("Which of the following is one of its ingredients? 🤔")
            consoleIO.viewWithLine("1. Cheese")
            consoleIO.viewWithLine("2. Wrong1")
            consoleIO.viewWithLine("3. Wrong2")
            consoleIO.view("Select an option (1-3): ")
            consoleIO.viewWithLine(match { it.contains("Invalid input") })
            consoleIO.viewWithLine("\n\uD83C\uDFAF Final Score: 0")
            consoleIO.viewWithLine("👿 Game Over!")
        }
    }

    @Test
    fun `should handle failure when game fails to start`() {
        // Given
        val errorMessage = "Test error message"
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.failure(Exception(errorMessage))
        every { consoleIO.readInput() } returnsMany listOf("2", "0")
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
            consoleIO.viewWithLine("Guess the correct ingredient for each meal. One wrong answer ends the game!")
            consoleIO.viewWithLine(match { it.contains("⚠ $errorMessage") })
            consoleIO.viewWithLine(match { it.contains("Final Score: 0") })
            consoleIO.viewWithLine("👿 Game Over!")
        }
    }

    @Test
    fun `should handle empty input when user provides only spaces`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()

        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returnsMany listOf("2", "   ", "0") // Select option 2, spaces input, exit
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
            consoleIO.viewWithLine("Guess the correct ingredient for each meal. One wrong answer ends the game!")
            consoleIO.viewWithLine("\n🍽 Meal: Pizza")
            consoleIO.viewWithLine("Which of the following is one of its ingredients? 🤔")
            consoleIO.viewWithLine("1. Cheese")
            consoleIO.viewWithLine("2. Wrong1")
            consoleIO.viewWithLine("3. Wrong2")
            consoleIO.view("Select an option (1-3): ")
            consoleIO.viewWithLine(match { it.contains("Invalid input") })
            consoleIO.viewWithLine("\n\uD83C\uDFAF Final Score: 0")
            consoleIO.viewWithLine("👿 Game Over!")
        }
    }

    @Test
    fun `should accept input when user enter input with spaces `() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        val updatedState = IngredientGameState(score = 1000, correctAnswers = 1)

        every { gamesMealsUseCase.isGameOver(any()) } returnsMany listOf(false, true)
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { gamesMealsUseCase.checkAnswer(1, round, state) } returns (true to updatedState)
        every { consoleIO.readInput() } returnsMany listOf("2", "  1  ", "0") // Select option 2, answer "  1  ", exit

        // When
        gameMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
            consoleIO.viewWithLine("Guess the correct ingredient for each meal. One wrong answer ends the game!")
            consoleIO.viewWithLine("\n🍽 Meal: Pizza")
            consoleIO.viewWithLine("Which of the following is one of its ingredients? 🤔")
            consoleIO.viewWithLine("1. Cheese")
            consoleIO.viewWithLine("2. Wrong1")
            consoleIO.viewWithLine("3. Wrong2")
            consoleIO.view("Select an option (1-3): ")
            consoleIO.viewWithLine("✅ Correct!")
            consoleIO.viewWithLine("\uD83C\uDFAF Score: 1000")
            consoleIO.viewWithLine("\n\uD83C\uDFAF Final Score: 1000")
            consoleIO.viewWithLine("\uD83C\uDFC6 Congratulations! You win 🎉")
        }
    }

    @Test
    fun `should handle game over when user selects option 2 and reaches maximum score`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val gameOverState = IngredientGameState(correctAnswers = 15, score = 15000)

        every { gamesMealsUseCase.isGameOver(gameOverState) } returns true
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to gameOverState)
        every { consoleIO.readInput() } returnsMany listOf("2", "0") // Select option 2, exit

        // When
        gameMealsService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\uD83D\uDC69\u200D\uD83C\uDF73 Welcome to the Ingredient Game!")
            consoleIO.viewWithLine("Guess the correct ingredient for each meal. One wrong answer ends the game!")
            consoleIO.viewWithLine("\n\uD83C\uDFAF Final Score: 15000")
            consoleIO.viewWithLine("\uD83C\uDFC6 Congratulations! You win 🎉")
        }
    }
    // endregion

    private fun createIngredientRound(
        mealName: String,
        correctAnswer: String,
        options: List<String> = listOf(correctAnswer, "Wrong1", "Wrong2")
    ): IngredientGameRound {
        return IngredientGameRound(
            mealName = mealName,
            correctAnswer = correctAnswer,
            options = options
        )
    }
}