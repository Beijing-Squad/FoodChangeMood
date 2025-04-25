package presentation.service

import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import fake.mealsListWithHighCaloriesMeals
import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
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
        viewMealDetails = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        suggestMealService = SuggestionMealsService(suggestUseCase, viewMealDetails, consoleIO)
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

        every { consoleIO.readInput() } returns choiceSearchFeature

        // When
        suggestMealService.showService()

        // Then
        verify {
            suggestUseCase.suggestTenRandomMealsContainsPotato()
        }
    }

    @Test
    fun `should show an error message when throw an exception`() {
        // Given
        val choiceSearchFeature = "5"
        val errorMessage = "no meals contains potato found"
        every { consoleIO.readInput() } returns choiceSearchFeature
        every { suggestUseCase.suggestTenRandomMealsContainsPotato() } throws IllegalArgumentException(errorMessage)

        // When
        suggestMealService.showService()

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
        val choiceSearchFeature = "2"
        val firstSweet = createMeal(name = "mahalbya", description = "egyptian sweet")
        val secondSweet = createMeal(name = "Halawa", description = "Delicious and eggless")
        val thirdSweet = null
        every { suggestUseCase.suggestSweetsWithNoEggs() } returnsMany listOf(
            firstSweet,
            secondSweet,
            thirdSweet
        )
        every { consoleIO.readInput() } returns choiceSearchFeature andThen "no"

        // When
        suggestMealService.showService()

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
        val choiceSearchFeature = "2"
        val sweet = createMeal(name = "mahalbya", description = "egyptian sweet")
        every { consoleIO.readInput() } returns choiceSearchFeature andThen "yes"
        every { suggestUseCase.suggestSweetsWithNoEggs() } returns sweet

        // When
        suggestMealService.showService()

        // Then
        verify {
            viewMealDetails.displayMealDetails(sweet)
        }
    }

    @Test
    fun `should exit when user types exit`() {
        // Given
        val choiceSearchFeature = "2"
        val sweet = createMeal(name = "mahalbya", description = null)
        every { suggestUseCase.suggestSweetsWithNoEggs() } returns sweet
        every { consoleIO.readInput() } returns choiceSearchFeature andThen "exit"

        // When
        suggestMealService.showService()

        // Then
        verify { consoleIO.viewWithLine("GoodBye") }

    }

    @Test
    fun `should view meal details when user types yes with none case sensitive`() {
        // Given
        val choiceSearchFeature = "2"
        val sweet = createMeal(name = "mahalbya", description = null)
        every { suggestUseCase.suggestSweetsWithNoEggs() } returns sweet
        every { consoleIO.readInput() } returns choiceSearchFeature andThen " YES"

        // When
        suggestMealService.showService()

        // Then
        verify { viewMealDetails.displayMealDetails(sweet) }

    }

    @Test
    fun `should return massage when user types empty or anything except the three choices`() {
        // Given
        val choiceSearchFeature = "2"
        val firstSweet = createMeal(name = "mahalbya", description = null)
        val secondSweet = createMeal(name = "Halawa", description = "Delicious and eggless")
        val thirdSweet = null
        every { suggestUseCase.suggestSweetsWithNoEggs() } returnsMany listOf(
            firstSweet,
            secondSweet,
            thirdSweet
        )
        every { consoleIO.readInput() } returns choiceSearchFeature andThen ""

        // When
        suggestMealService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("Try this sweet: ${firstSweet.name}")
            consoleIO.readInput()
            consoleIO.viewWithLine("Unknown input.")
            consoleIO.viewWithLine("Try this sweet: ${secondSweet.name}")
        }
    }

    //endregion
    //region suggest meal have more than seven hundred calories
    @Test
    fun `should call launchSoThinMeals when suggest meal with more than 700 calories selected`() {
        // Given
        val choiceSuggestionFeature = "6"
        val likeMeal = "yes"
        val unLikeMeal = "no"
        every { consoleIO.readInput()?.trim()?.lowercase() } returnsMany listOf(
            choiceSuggestionFeature,
            unLikeMeal, unLikeMeal, likeMeal, likeMeal
        )
        every { suggestUseCase.suggestMealHaveMoreThanSevenHundredCalories() } returns mealsListWithHighCaloriesMeals

        // When
        suggestMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("Do You Like This Meal?")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal Or 'exit':")
        }

    }

    @Test
    fun `should show message when input invalid choice`() {
        // Given
        val invalidChoice = "qwerty"
        val finishLoop = "exit"
        every { consoleIO.readInput()?.trim()?.lowercase() } returns invalidChoice andThen finishLoop
        every { suggestUseCase.suggestMealHaveMoreThanSevenHundredCalories() } returns mealsListWithHighCaloriesMeals

        // When
        suggestMealService.launchSoThinMeals()

        // Then
        verify { consoleIO.viewWithLine("Invalid input! Please choose 'Yes','No' or 'Exit'.") }
    }

    //endregion

    //region keto meal test
    @Test
    fun `should handle multiple invalid inputs before valid input`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "invalid1", "invalid2", "yes", "0")

        // When
        suggestMealService.showService()

        // Then
        verifyOrder {
            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")

            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should handle null or empty input correctly`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "", "exit", "0")

        // When
        suggestMealService.showService()

        // Then
        verifyOrder {
            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("GoodBye")
        }
    }

    @Test
    fun `should accept uppercase and space input`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "  YES  ", "0")

        // When
        suggestMealService.showService()

        // Then
        verifyOrder {
            suggestUseCase.suggestKetoMeal(mutableSetOf())
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
        // Given
        val ketoMeal1 = createKetoMeal(1, "Keto Salad")
        val ketoMeal2 = createKetoMeal(2, "Keto Pizza")
        every { suggestUseCase.suggestKetoMeal(any()) } returnsMany listOf(ketoMeal1, ketoMeal2)
        every { consoleIO.readInput() } returnsMany listOf("1", "no", "exit", "0")

        // When
        suggestMealService.showService()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Salad")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("🔄 Okay! Let's try another one.")
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Pizza")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("GoodBye")
        }
    }

    @Test
    fun `should display keto meal details when user enter yes`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "yes", "0")

        // When
        suggestMealService.showService()

        // Then
        verify {
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            viewMealDetails.displayMealDetails(ketoMeal)
        }
    }

    @Test
    fun `should return suitable message when no more keto meals are available`() {
        // Given
        every { suggestUseCase.suggestKetoMeal(any()) } returns null
        every { consoleIO.readInput() } returnsMany listOf("1", "0")

        // When
        suggestMealService.showService()

        // Then
        verify {
            consoleIO.viewWithLine("😔 No more keto meals to suggest.")
        }
    }

    @Test
    fun `should not accept null empty and whitespace inputs`() {
        // Given
        val ketoMeal = createKetoMeal(1, "Keto Burger")
        every { suggestUseCase.suggestKetoMeal(any()) } returns ketoMeal
        every { consoleIO.readInput() } returnsMany listOf("1", "", "   ", "exit", "0")

        // When
        suggestMealService.showService()

        // Then
        verifyOrder {
            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("⚠️ Please type 'yes' or 'no'")
            suggestUseCase.suggestKetoMeal(mutableSetOf())
            consoleIO.viewWithLine("\n🥑 Keto Meal: Keto Burger")
            consoleIO.viewWithLine("Short Description: A keto-friendly meal")
            consoleIO.viewWithLine("Do you like it? ❤")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal (or type 'exit' to quit):")
            consoleIO.readInput()
            consoleIO.viewWithLine("GoodBye")
        }
    }

    private fun createKetoMeal(id: Int, name: String): Meal {
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
    //endregion
}
