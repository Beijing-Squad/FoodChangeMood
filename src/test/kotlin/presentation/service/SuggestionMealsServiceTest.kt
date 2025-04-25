package presentation.service

import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import fake.mealsListWithHighCaloriesMeals
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

    //region Italian Dishes For Large Groups
    @Test
    fun `should print Italian large group meals when such meals exist`() {
        // Given
        val meals = listOf(
            createMeal(name = "Italian Pasta", minutes = 40),
            createMeal(name = "Lasagna", minutes = 60)
        )
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns meals

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verify { consoleIO.viewWithLine("🍝 Meals from Italy suitable for large groups:\n") }
        verify { consoleIO.viewWithLine("1. Italian Pasta | 🕒 40 minutes |") }
        verify { consoleIO.viewWithLine("2. Lasagna | 🕒 60 minutes |") }
    }

    @Test
    fun `should print no meals found message when no Italian large group meals exist`() {
        // Given
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns emptyList()

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verify { consoleIO.viewWithLine("❌ No Italian meals found for large groups.") }
    }

    @Test
    fun `should handle meals with empty name`() {
        // Given
        val unnamedMeal = createMeal(name = "")
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns listOf(unnamedMeal)

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verify { consoleIO.viewWithLine("1.  | 🕒 ${unnamedMeal.minutes} minutes |") }
    }

    @Test
    fun `should only show meals that are both italian and for large groups`() {
        // Given
        val meals = listOf(
            createMeal(name = "Italian Pizza", tags = listOf("italian", "for-large-groups")),
            createMeal(name = "Solo Spaghetti", tags = listOf("italian")),
            createMeal(name = "Party Platter", tags = listOf("for-large-groups")),
            createMeal(name = "Tiramisu", tags = listOf("dessert"))
        )
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns listOf(meals[0])

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verify(exactly = 1) { consoleIO.viewWithLine("1. Italian Pizza | 🕒 ${meals[0].minutes} minutes |") }
        verify(exactly = 0) { consoleIO.viewWithLine(match { it.contains("Solo Spaghetti") }) }
        verify(exactly = 0) { consoleIO.viewWithLine(match { it.contains("Party Platter") }) }
    }
    @Test
    fun `should ignore meals with empty tags`() {
        // Given
        val noTagsMeal = createMeal(name = "Unknown Dish", tags = emptyList())
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns listOf()

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verify { consoleIO.viewWithLine("❌ No Italian meals found for large groups.") }
    }
    @Test
    fun `should handle meals with null or malformed tags`() {
        // Given
        val malformedMeal = createMeal(name = "Glitch Dish", tags = listOf("Itali@n", "FOR_LARGE_GROUP"))
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns listOf()

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verify { consoleIO.viewWithLine("❌ No Italian meals found for large groups.") }
    }
    @Test
    fun `should match tags regardless of casing`() {
        // Given
        val meal = createMeal(name = "Caprese", tags = listOf("ITALIAN", "For_Large_Group"))
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns listOf(meal)

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verify { consoleIO.viewWithLine("1. Caprese | 🕒 ${meal.minutes} minutes |") }
    }
    @Test
    fun `should correctly number multiple meals`() {
        // Given
        val meal1 = createMeal(name = "Gnocchi", minutes = 20)
        val meal2 = createMeal(name = "Risotto", minutes = 45)
        every { suggestUseCase.suggestItalianLargeGroupsMeals() } returns listOf(meal1, meal2)

        // When
        suggestMealService.launchItalianLargeGroupMeals()

        // Then
        verifyOrder {
            consoleIO.viewWithLine("🍝 Meals from Italy suitable for large groups:\n")
            consoleIO.viewWithLine("1. Gnocchi | 🕒 20 minutes |")
            consoleIO.viewWithLine("2. Risotto | 🕒 45 minutes |")
        }
    }
    //endregion

}
