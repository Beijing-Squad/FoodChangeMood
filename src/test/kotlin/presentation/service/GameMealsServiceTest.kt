package presentation.service

import com.google.common.base.Verify.verify
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

    private lateinit var gamesMeals: ManageMealsGamesUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var service: GameMealsService

    @BeforeEach
    fun setup() {
        gamesMeals = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        service = GameMealsService(gamesMeals, consoleIO)
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

        every { gamesMeals.startNewRound() } returns round1
        every { consoleIO.readInput() } returnsMany listOf("25")
        every { gamesMeals.makeGuess(round1, 25) } returns round2

        // When
        service.launchGuessGame()

        // Then
        verify {
            consoleIO.viewWithLine("\n🎯 Guess the Preparation Time for: **Kebab** (in minutes)")
            consoleIO.viewWithLine("You have 3 attempts!")
            consoleIO.view("Your guess: ")
            consoleIO.viewWithLine("Correct!! The preparation time is indeed 25 minutes.")
            consoleIO.viewWithLine("🎮 Game Over! Returning to main menu.\n")
        }
    }

    //endregion
}