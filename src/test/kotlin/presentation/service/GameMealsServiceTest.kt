package presentation.service

import io.mockk.*
import org.beijing.logic.usecases.ManageMealsGamesUseCase
import org.beijing.model.*
import org.beijing.presentation.service.GameMealsService
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

    //region Ingredient Game Tests
    @Test
    fun `should handle successful ingredient game round when user provides correct answer`() {
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
    fun `should handle game over when user reaches maximum score`() {
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
    fun `should handle wrong answer when user provides incorrect option`() {
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
    fun `should handle invalid input when user provides out of range number`() {
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
    fun `should handle non-numeric input when user provides text instead of number`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returns "a"
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
    fun `should handle null input when user provides no input`() {
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

    @Test
    fun `should handle failure when game fails to start`() {
        // Given
        val errorMessage = "Test error message"
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.failure(Exception(errorMessage))
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("⚠ $errorMessage") })
            consoleIO.viewWithLine(match { it.contains("Final Score: 0") })
        }
    }

    @Test
    fun `should handle empty input when user provides only spaces`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        
        every { gamesMealsUseCase.isGameOver(any()) } returns false
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { consoleIO.readInput() } returns ""
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        // When
        gameMealsService.launchIngredientGame()

        // Then
        verify {
            consoleIO.viewWithLine(match { it.contains("Invalid input") })
            consoleIO.viewWithLine(match { it.contains("Game Over") })
        }
    }

    @Test
    fun `should handle valid numeric input when user provides correct number`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        val updatedState = IngredientGameState(score = 1000, correctAnswers = 1)
        
        every { gamesMealsUseCase.isGameOver(any()) } returnsMany listOf(false, true)
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { gamesMealsUseCase.checkAnswer(1, round, state) } returns (true to updatedState)
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
    fun `should handle input with spaces when user provides number with leading and trailing spaces`() {
        // Given
        val round = createIngredientRound("Pizza", "Cheese")
        val state = IngredientGameState()
        val updatedState = IngredientGameState(score = 1000, correctAnswers = 1)
        
        every { gamesMealsUseCase.isGameOver(any()) } returnsMany listOf(false, true)
        every { gamesMealsUseCase.startIngredientGame(any()) } returns Result.success(round to state)
        every { gamesMealsUseCase.checkAnswer(1, round, state) } returns (true to updatedState)
        every { consoleIO.readInput() } returns "  1  "
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

}