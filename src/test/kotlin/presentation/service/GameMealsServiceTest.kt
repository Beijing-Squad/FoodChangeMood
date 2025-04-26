package presentation.service

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

    //region handle user choice
    @Test
    fun handleUserChoice() {
    }
    //endregion

    //region Preparation Time
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