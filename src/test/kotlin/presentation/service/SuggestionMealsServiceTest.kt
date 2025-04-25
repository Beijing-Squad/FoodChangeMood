package presentation.service

import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SuggestionMealsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO

class SuggestionMealsServiceTest {
    private lateinit var suggestUseCase: ManageMealsSuggestionsUseCase
    private lateinit var suggestMealService: SuggestionMealsService
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var consoleIO: ConsoleIO

    @BeforeEach
    fun setup() {
        suggestUseCase = mockk(relaxed = true)
        viewMealDetails = mockk()
        consoleIO = mockk(relaxed = true)
        suggestMealService = SuggestionMealsService(manageMealsSuggest, viewMealDetails, consoleIO)
    }

    //region handle user choice
    @Test
    fun `should show options of sub feature when suggest meal option already selected`() {
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
            suggestUseCase.suggestTenRandomMealsContainsPotato()
        }
    }

    @Test
    fun `should show an error message when throw an exception`() {
        // Given
        val errorMessage = "no meals contains potato found"
        every { suggestUseCase.suggestTenRandomMealsContainsPotato() } throws IllegalArgumentException(errorMessage)

        // When
        suggestMealService.launchTenRandomPotatoMeals()

        // Then
        verify {
            consoleIO.viewWithLine(errorMessage)
        }
    }
    //endregion

    //region sweets wit no eggs
    @Test
    fun `should call Suggest Sweets with No Eggs when it selected`() {
        // Given
        val choiceSearchFeature = "2"
        val firstSweet = createMeal(name = "mahalbya", description = "egyptian sweet")
        val secondSweet = null
        every { consoleIO.readInput() } returns choiceSearchFeature
        every { suggestUseCase.suggestSweetsWithNoEggs() } returnsMany listOf(firstSweet, secondSweet)

        // When
        suggestMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("Try this sweet: ${firstSweet.name}")
            consoleIO.viewWithLine("Description: ${firstSweet.description ?: "No description"}")
            consoleIO.view("Do you like it? (yes to view details / no to see another / exit): ")
        }
    }

    @Test
    fun `should skip sweet when user says no and suggest next one`() {
        // Given
        val firstSweet = createMeal(name = "mahalbya", description = "egyptian sweet")
        val secondSweet = createMeal(name = "Halawa", description = "Delicious and eggless")
        val thirdSweet = null
        every { suggestUseCase.suggestSweetsWithNoEggs() } returnsMany listOf(
            firstSweet,
            secondSweet,
            thirdSweet
        )
        every { consoleIO.readInput() } returns "no"

        // When
        suggestMealService.launchSweetWithoutEggs()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("Try this sweet: ${firstSweet.name}")
            consoleIO.readInput()
            consoleIO.viewWithLine("Try this sweet: ${secondSweet.name}")
        }
    }

    @Test
    fun `should view meal details when user says yes`() {
        // Given
        val sweet = createMeal(name = "mahalbya", description = "egyptian sweet")
        every { suggestUseCase.suggestSweetsWithNoEggs() } returnsMany listOf(sweet)
        every { consoleIO.readInput() } returns "yes"

        // When
        suggestMealService.launchSweetWithoutEggs()

        // Then
        verify {
            viewMealDetails.displayMealDetails(sweet)
        }
    }

    @Test
    fun `should exit when user types exit`() {
        // Given
        val sweet = createMeal(name = "mahalbya", description = null)
        every { suggestUseCase.suggestSweetsWithNoEggs() } returns sweet
        every { consoleIO.readInput() } returns "exit"

        // When
        suggestMealService.launchSweetWithoutEggs()

        // Then
        verify { consoleIO.viewWithLine("GoodBye") }

    }

    @Test
    fun `should view meal details when user types yes with none case sensitive`() {
        // Given
        val sweet = createMeal(name = "mahalbya", description = null)
        every { suggestUseCase.suggestSweetsWithNoEggs() } returns sweet
        every { consoleIO.readInput() } returns " YES"

        // When
        suggestMealService.launchSweetWithoutEggs()

        // Then
        verify { viewMealDetails.displayMealDetails(sweet) }

    }

    @Test
    fun `should return massage when user types empty or anything except the three choices`() {
        // Given
        val firstSweet = createMeal(name = "mahalbya", description = null)
        val secondSweet = createMeal(name = "Halawa", description = "Delicious and eggless")
        val thirdSweet = null
        every { suggestUseCase.suggestSweetsWithNoEggs() } returnsMany listOf(
            firstSweet,
            secondSweet,
            thirdSweet
        )
        every { consoleIO.readInput() } returns ""

        // When
        suggestMealService.launchSweetWithoutEggs()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("Try this sweet: ${firstSweet.name}")
            consoleIO.readInput()
            consoleIO.viewWithLine("Unknown input.")
            consoleIO.viewWithLine("Try this sweet: ${secondSweet.name}")
        }
    }
    //endregion

}
