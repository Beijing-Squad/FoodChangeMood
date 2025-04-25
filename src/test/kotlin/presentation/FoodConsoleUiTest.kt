package presentation

import io.mockk.*
import org.beijing.presentation.FoodConsoleUi
import org.beijing.presentation.service.*
import presentation.view_read.ConsoleIO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FoodConsoleUiTest {

    private lateinit var searchMealService: SearchMealService
    private lateinit var viewMealsService: ViewMealsService
    private lateinit var gameMealsService: GameMealsService
    private lateinit var suggestionMealsService: SuggestionMealsService
    private lateinit var consoleIO: ConsoleIO
    private lateinit var foodConsoleUi: FoodConsoleUi

    @BeforeEach
    fun setup() {
        searchMealService = mockk(relaxed = true)
        viewMealsService = mockk(relaxed = true)
        gameMealsService = mockk(relaxed = true)
        suggestionMealsService = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        foodConsoleUi = FoodConsoleUi(
            searchMealService,
            viewMealsService,
            gameMealsService,
            suggestionMealsService,
            consoleIO
        )
    }

    @Test
    fun `should show welcome message and options when start is called`() {
        // When
        foodConsoleUi.start()

        // Then
        verify { consoleIO.view("Welcome to Food Change Mood\uD83E\uDD6A ") }
        verify { consoleIO.viewWithLine("===Please enter one of the numbers listed below===") }
        verify { consoleIO.viewWithLine("1. Suggestion Meal \uD83E\uDD14") }
        verify { consoleIO.viewWithLine("2. Search Meal \uD83D\uDD0E") }
        verify { consoleIO.viewWithLine("3. Game Meal \uD83C\uDFAE") }
        verify { consoleIO.viewWithLine("4. View Meal \uD83E\uDD63") }
        verify { consoleIO.viewWithLine("0. Exit") }
    }

    @Test
    fun `should call SuggestionMealsService when input is 1`() {
        // Given
        every { consoleIO.readInput() } returns "1"

        // When
        foodConsoleUi.start()

        // Then
        verify { suggestionMealsService.showService() }
    }

    @Test
    fun `should call SearchMealService when input is 2`() {
        // Given
        every { consoleIO.readInput() } returns "2"

        // When
        foodConsoleUi.start()

        // Then
        verify { searchMealService.showService() }
    }

    @Test
    fun `should call GameMealsService when input is 3`() {
        // Given
        every { consoleIO.readInput() } returns "3"

        // When
        foodConsoleUi.start()

        // Then
        verify { gameMealsService.showService() }
    }

    @Test
    fun `should call ViewMealsService when input is 4`() {
        // Given
        every { consoleIO.readInput() } returns "4"

        // When
        foodConsoleUi.start()

        // Then
        verify { viewMealsService.showService() }
    }

    @Test
    fun `should return on exit when input is 0`() {
        // Given
        every { consoleIO.readInput() } returns "0"

        // When
        foodConsoleUi.start()

        // Then
        verify(exactly = 0) { suggestionMealsService.showService() }
        verify(exactly = 0) { searchMealService.showService() }
        verify(exactly = 0) { gameMealsService.showService() }
        verify(exactly = 0) { viewMealsService.showService() }
    }

    @Test
    fun `should show error message when invalid input is entered`() {
        // Given
        every { consoleIO.readInput() } returns "invalid"

        // When
        foodConsoleUi.start()

        // Then
        verify { consoleIO.viewWithLine("❌ Invalid input! Please enter a number between 0 and 4") }
    }
}