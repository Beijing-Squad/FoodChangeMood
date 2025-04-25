package presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.beijing.presentation.FoodConsoleUi
import org.beijing.presentation.FoodUi
import org.beijing.presentation.service.GameMealsService
import org.beijing.presentation.service.SearchMealService
import org.beijing.presentation.service.SuggestionMealsService
import org.beijing.presentation.service.ViewMealsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import presentation.view_read.ConsoleIO

class FoodConsoleUiTest {

    private lateinit var foodConsoleUi: FoodConsoleUi
    private lateinit var searchMealService: SearchMealService
    private lateinit var gameMealsService: GameMealsService
    private lateinit var viewMealsService: ViewMealsService
    private lateinit var suggestionMealsService: SuggestionMealsService
    private lateinit var consoleIO: ConsoleIO

    @BeforeEach
    fun setup() {
        searchMealService = mockk(relaxed = true)
        gameMealsService = mockk(relaxed = true)
        viewMealsService = mockk(relaxed = true)
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
    fun `should show message welcome when start app`() {
        // Given
        val inputChoice = "0"
        every { consoleIO.readInput() } returns inputChoice
        val messageWelcome = "Welcome to Food Change Mood\uD83E\uDD6A "

        // When
        foodConsoleUi.start()

        // When && Then
        verify { consoleIO.view(messageWelcome) }


    }

    @Test
    fun `should present suggestion meals when choice is one`() {
        // Given
        val inputChoice = "1"
        val titleOfFeature = "1. Suggestion Meal 🤔"
        every { consoleIO.readInput() } returns inputChoice

        // When
        foodConsoleUi.start()

        // Then
        verify { suggestionMealsService.showService() }
        verify { consoleIO.viewWithLine(titleOfFeature) }
    }

    @Test
    fun `should present search meals when choice is two`() {
        // Given
        val inputChoice = "2"
        val titleOfFeature = "2. Search Meal \uD83D\uDD0E"
        every { consoleIO.readInput() } returns inputChoice

        // When
        foodConsoleUi.start()

        // Then
        verify { searchMealService.showService() }
        verify { consoleIO.viewWithLine(titleOfFeature) }
    }

    @Test
    fun `should present game meals when choice is three`() {
        // Given
        val inputChoice = "3"
        val titleOfFeature = "3. Game Meal \uD83C\uDFAE"
        every { consoleIO.readInput() } returns inputChoice

        // When
        foodConsoleUi.start()

        // Then
        verify { gameMealsService.showService() }
        verify { consoleIO.viewWithLine(titleOfFeature) }
    }

    @Test
    fun `should present view meals when choice is four`() {
        // Given
        val inputChoice = "4"
        val titleOfFeature = "4. View Meal \uD83E\uDD63"
        every { consoleIO.readInput() } returns inputChoice

        // When
        foodConsoleUi.start()

        // Then
        verify { viewMealsService.showService() }
        verify { consoleIO.viewWithLine(titleOfFeature) }
    }

    @ParameterizedTest
    @CsvSource(
        "1a2b",
        " -",
        "@"
    )
    fun `should view message when write invalid option`(inputChoice: String) {
        // Given
        val errorMessage = "❌ Invalid input! Please enter a number between 0 and 4"
        every { consoleIO.readInput() } returns inputChoice

        // When
        foodConsoleUi.start()

        // Then
        verify { consoleIO.viewWithLine(errorMessage)}
    }

    @Test
    fun `should exit when choice zero from menu`() {
        // Given
        val inputChoice = "0"
        every { consoleIO.readInput() } returns inputChoice

        // When
        foodConsoleUi.start()

        // Then
        verify(exactly = 0) { suggestionMealsService.showService() }
        verify(exactly = 0) { searchMealService.showService() }
        verify(exactly = 0) { gameMealsService.showService() }
        verify(exactly = 0) { viewMealsService.showService() }
    }

    @Test
    fun `should handle exception when an unknown error occurred`() {
        // Given
        every { consoleIO.readInput() } throws Exception("An unknown error occurred")

        // When
        foodConsoleUi.start()

        // Then
        verify { consoleIO.viewWithLine("An unknown error occurred") }
    }

}
