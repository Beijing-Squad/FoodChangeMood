package presentation.service

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.*
import org.beijing.presentation.service.GameMealsService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO

class GameMealsServiceTest {
    private lateinit var gamesMealsUseCase: ManageMealsGamesUseCase
    private lateinit var consoleIO: ConsoleIO
    private lateinit var gameMealsService: GameMealsService

    @BeforeEach
    fun setUp() {
        gamesMealsUseCase = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        gameMealsService = GameMealsService(gamesMealsUseCase, consoleIO)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //region Ingredient Game Tests
    @Test
    fun `should handle successful ingredient game round`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val initialState = IngredientGameState()
        val updatedState = IngredientGameState(score = 1000, correctAnswers = 1)
        
        every { gamesMealsUseCase.isGameOver(any()) } returnsMany listOf(false, true)
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to initialState)
        every { gamesMealsUseCase.checkAnswer(1, round, initialState) } returns (true to updatedState)
        every { consoleIO.readInput() } returns "1"
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Correct!") })
            consoleIO.viewWithLine(match { it.contains("Score: 1000") })
        }
    }

    @Test
    fun `should handle game over with maximum score`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val gameOverState = IngredientGameState(correctAnswers = 15, score = 15000)
        
        every { gamesMealsUseCase.isGameOver(gameOverState) } returns true
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to gameOverState)
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Congratulations! You win") })
            consoleIO.viewWithLine(match { it.contains("Final Score: 15000") })
        }
    }

    @Test
    fun `should handle wrong answer in ingredient game`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { gamesMealsUseCase.checkAnswer(2, round, state) } returns (false to state)
        every { consoleIO.readInput() } returns "2"
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Wrong!") })
            consoleIO.viewWithLine(match { it.contains("Game Over!") })
        }
    }

    @Test
    fun `should handle invalid numeric input in ingredient game`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returns "4"
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should handle non-numeric input in ingredient game`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returns "abc"
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Invalid input") })
        }
    }

    @Test
    fun `should handle null input in ingredient game`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returns null
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Invalid input") })
        }
    }
    //endregion

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