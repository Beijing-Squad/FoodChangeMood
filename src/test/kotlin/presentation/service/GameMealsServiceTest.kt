package org.beijing.presentation.service

import io.mockk.*
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.IngredientGameRound
import org.beijing.model.IngredientGameState
import org.junit.jupiter.api.BeforeEach
import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.GameRound
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.presentation.service.GameMealsService
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
    //endregion

    //region Preparation Time Guess Game
    @Test
    fun `should complete game immediately when user guesses correctly`() {
        // Given
        val meal = createMeal(name = "Kebab", minutes = 25)
        val round1 = GameRound(meal, attemptsLeft = 3, isCompleted = false, lastFeedBack = null)
        val round2 = round1.copy(
            attemptsLeft = 2,
            isCompleted = true,
            lastFeedBack = "Correct!! The preparation time is indeed 25 minutes."
        )

        every { gamesMealsUseCase.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("25")
        every { gamesMealsUseCase.makeGuess(round1, 25) } returns round2

        // When
        gameMealsService.launchGuessGame()

        // Then
        verify {
            consoleIO.viewWithLine("\n🎯 Guess the Preparation Time for: **Kebab** (in minutes)")
            consoleIO.viewWithLine("You have 3 attempts!")
            consoleIO.view("Your guess: ")
            consoleIO.viewWithLine("Correct!! The preparation time is indeed 25 minutes.")
            consoleIO.viewWithLine("🎮 Game Over! Returning to main menu.\n")
        }
    }

    @Test
    fun `should prompt again on non-numeric input and continue game`() {
        // Given
        val meal = createMeal(name = "Soup", minutes = 15)
        val round1 = GameRound(meal, 3, false, null)
        val round2 = round1.copy(
            attemptsLeft = 2,
            isCompleted = true,
            lastFeedBack = "Correct!! The preparation time is indeed 15 minutes."
        )

        every { gamesMealsUseCase.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("abc", "15")
        every { gamesMealsUseCase.makeGuess(round1, 15) } returns round2

        // When
        gameMealsService.launchGuessGame()

        // Then
        verify {
            consoleIO.viewWithLine("Please enter a valid number.")
            consoleIO.viewWithLine("Correct!! The preparation time is indeed 15 minutes.")
        }
    }

    @Test
    fun `should allow multiple wrong guesses then end game`() {
        // Given
        val meal = createMeal(name = "Burger", minutes = 10)
        val round1 = GameRound(meal, 3, false, null)
        val round2 = GameRound(meal, 2, false, "Too low! Try a higher number.")
        val round3 = GameRound(meal, 1, false, "Too high! Try a lower number.")
        val round4 = GameRound(
            meal,
            0,
            true,
            "Too low! Try a higher number.\nGameOver! The actual preparation time is 10 minutes."
        )

        every { gamesMealsUseCase.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("5", "15", "7")
        every { gamesMealsUseCase.makeGuess(round1, 5) } returns round2
        every { gamesMealsUseCase.makeGuess(round2, 15) } returns round3
        every { gamesMealsUseCase.makeGuess(round3, 7) } returns round4

        // When
        gameMealsService.launchGuessGame()

        // Then
        verify {
            consoleIO.viewWithLine("Too low! Try a higher number.")
            consoleIO.viewWithLine("Too high! Try a lower number.")
            consoleIO.viewWithLine("Too low! Try a higher number.\nGameOver! The actual preparation time is 10 minutes.")
            consoleIO.viewWithLine("🎮 Game Over! Returning to main menu.\n")
        }
    }

    @Test
    fun `should display message if round is already completed`() {
        // Given
        val meal = createMeal(name = "Pasta", minutes = 12)
        val round1 = GameRound(meal, 3, false, null)
        val round2 =
            round1.copy(isCompleted = true, lastFeedBack = "This round is already Completed, Start A new Round.")

        every { gamesMealsUseCase.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("12")
        every { gamesMealsUseCase.makeGuess(round1, 12) } returns round2

        // When
        gameMealsService.launchGuessGame()

        // Then
        verify {
            consoleIO.viewWithLine("This round is already Completed, Start A new Round.")
        }
    }

    @Test
    fun `should win on last attempt without triggering game over message`() {
        // Given
        val meal = createMeal(name = "Tacos", minutes = 18)
        val round1 = GameRound(meal, 1, false, null)
        val round2 = round1.copy(
            attemptsLeft = 0,
            isCompleted = true,
            lastFeedBack = "Correct!! The preparation time is indeed 18 minutes."
        )

        every { gamesMealsUseCase.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("18")
        every { gamesMealsUseCase.makeGuess(round1, 18) } returns round2

        // When
        gameMealsService.launchGuessGame()

        // Then
        verify {
            consoleIO.viewWithLine("Correct!! The preparation time is indeed 18 minutes.")
            consoleIO.viewWithLine("🎮 Game Over! Returning to main menu.\n")
        }
    }

    @Test
    fun `should handle guess with negative numbers`() {
        // Given
        val meal = createMeal(name = "Salad", minutes = 8)
        val round1 = GameRound(meal, 3, false, null)
        val round2 = round1.copy(
            attemptsLeft = 2,
            isCompleted = true,
            lastFeedBack = "Too low! Try a higher number."
        )

        every { gamesMealsUseCase.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("-5")
        every { gamesMealsUseCase.makeGuess(round1, -5) } returns round2

        // When
        gameMealsService.launchGuessGame()

        // Then
        verify {
            consoleIO.viewWithLine("Too low! Try a higher number.")
        }
    }

    @Test
    fun `should skip multiple invalid inputs before valid guess`() {
        //Given
        val meal = createMeal(name = "Curry", minutes = 30)
        val round1 = GameRound(meal, 3, false, null)
        val round2 = round1.copy(
            attemptsLeft = 2,
            isCompleted = true,
            lastFeedBack = "Correct!! The preparation time is indeed 30 minutes."
        )

        every { gamesMealsUseCase.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("hello", "?", "30")
        every { gamesMealsUseCase.makeGuess(round1, 30) } returns round2
        //When
        gameMealsService.launchGuessGame()
        //Then
        verify {
            consoleIO.viewWithLine("Please enter a valid number.")
            consoleIO.viewWithLine("Correct!! The preparation time is indeed 30 minutes.")
        }
    }
    //endregion
}