package presentation.service

import io.mockk.*
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SuggestionMealsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO
import kotlinx.datetime.LocalDate

class SuggestionMealsServiceTest {

    private lateinit var suggestionMealsUseCase: ManageMealsSuggestionsUseCase
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var consoleIO: ConsoleIO
    private lateinit var suggestionMealsService: SuggestionMealsService

    @BeforeEach
    fun setUp() {
        suggestionMealsUseCase = mockk(relaxed = true)
        viewMealDetails = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        suggestionMealsService = SuggestionMealsService(
            suggestionMeals = suggestionMealsUseCase,
            viewMealDetails = viewMealDetails,
            consoleIO = consoleIO
        )
    }

    @Test
    fun `should handle multiple invalid inputs before valid input`() {
        val ketoMeal = createMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("invalid1", "invalid2", "yes")

        suggestionMealsService.launchKetoMealHelper()

        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should handle null input correctly`() {
        val ketoMeal = createMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf(null, "exit")

        suggestionMealsService.launchKetoMealHelper()

        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
        }
    }

    @Test
    fun `should accept uppercase and space input `() {
        val ketoMeal = createMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returns "  YES  "

        suggestionMealsService.launchKetoMealHelper()

        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should show another meal when user input no`() {
        val ketoMeal1 = createMeal(1, "Keto Salad")
        val ketoMeal2 = createMeal(2, "Keto Pizza")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returnsMany listOf(ketoMeal1, ketoMeal2)
        every { consoleIO.readInput() } returnsMany listOf("no", "exit")

        suggestionMealsService.launchKetoMealHelper()

        verifyOrder {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Salad")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("🔄 Okay! Let's try another one.")
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Pizza")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
        }
    }

    @Test
    fun `should display keto meal details when user enter yes`() {
        val ketoMeal = createMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returns "yes"

        suggestionMealsService.launchKetoMealHelper()

        verify {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should return suitable message when no more keto meals are available`() {
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns null

        suggestionMealsService.launchKetoMealHelper()

        verify {
            consoleIO.viewWithLine("😔 No more keto meals to suggest.")
        }
    }

    @Test
    fun `should not accept null empty and whitespace inputs`() {
        val ketoMeal = createMeal(1, "Keto Burger")
        every { suggestionMealsUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf(null, "", "   ", "exit")

        suggestionMealsService.launchKetoMealHelper()

        verifyOrder {
            suggestionMealsUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
        }
    }

    private fun createMeal(id: Int, name: String): Meal {
        return Meal(
            name = name,
            id = id,
            minutes = 30,
            contributorId = 1,
            submitted = LocalDate(2005, 9, 16),
            tags = listOf("keto"),
            nutrition = Nutrition(
                caloriesKcal = 500.0,
                totalFatGrams = 40.0,
                sugarGrams = 2.0,
                sodiumGrams = 1.0,
                proteinGrams = 20.0,
                saturatedFatGrams = 15.0,
                carbohydratesGrams = 10.0
            ),
            nSteps = 5,
            steps = listOf("Step 1", "Step 2"),
            description = "A keto-friendly meal",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            nIngredients = 2
        )
    }
}