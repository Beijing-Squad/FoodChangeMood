package presentation.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SuggestionMealsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO

class SuggestionMealsServiceTest {
    private lateinit var manageMealsSuggest: ManageMealsSuggestionsUseCase
    private lateinit var suggestMealService: SuggestionMealsService
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var consoleIO: ConsoleIO

    @BeforeEach
    fun setup() {
        manageMealsSuggest = mockk(relaxed = true)
        viewMealDetails = mockk()
        consoleIO = mockk(relaxed = true)
        suggestMealService = SuggestionMealsService(manageMealsSuggest, viewMealDetails, consoleIO)
    }

    //region handle user choice
    @Test
    fun `should show option of sub feature when suggest meal selected`() {
        // When
        suggestMealService.showOptionService()

        // Then
        verify {
            consoleIO.viewWithLine("\n\n===Please enter one of the numbers listed below===\n")
            consoleIO.viewWithLine("1. Suggest a Keto Meal \uD83E\uDD51 ")
            consoleIO.viewWithLine("2. Suggest Sweets with No Eggs \uD83C\uDF70")
            consoleIO.viewWithLine("3. Suggest Easy Food \uD83C\uDF2D")
            consoleIO.viewWithLine("4. Suggest Italian Meals for Large Groups \uD83C\uDF55")
            consoleIO.viewWithLine("5. Suggest Ten Meals Contains Potato In Ingredients \uD83E\uDD54")
            consoleIO.viewWithLine("6. Suggest Meal With more than 700 calories \uD83C\uDF54")
            consoleIO.viewWithLine("0. Exit")
        }
    }
    //endregion

    //region ten random potato meals ui test
    @Test
    fun `should call get ten random potato meals when selected`() {
        // Given
        val choiceSearchFeature = "5"
        val exitInput = "0"

        every { consoleIO.readInput() } returns choiceSearchFeature andThen exitInput

        // When
        suggestMealService.handleUserChoice()

        // Then
        verify {
            manageMealsSuggest.suggestTenRandomMealsContainsPotato()
        }
    }

    @Test
    fun `should show the error message when throw an exception`() {
        // Given
        val errorMessage = "no meals contains potato found"

        every { manageMealsSuggest.suggestTenRandomMealsContainsPotato() } throws IllegalArgumentException(errorMessage)

        // When
        suggestMealService.launchTenRandomPotatoMeals()

        // Then
        verify {
            consoleIO.viewWithLine(errorMessage)
        }
    }
    //endregion
}