package presentation.service

import io.mockk.mockk
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
}