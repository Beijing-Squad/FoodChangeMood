package presentation.service

import fake.createMeal
import io.mockk.*
import kotlinx.datetime.LocalDate
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SearchMealService
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import presentation.view_read.ConsoleIO

class SearchMealServiceTest {
    private lateinit var manageMealsSearch: ManageMealsSearchUseCase
    private lateinit var searchMealService: SearchMealService
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var consoleIO: ConsoleIO

    @BeforeEach
    fun setup() {
        manageMealsSearch = mockk(relaxed = true)
        viewMealDetails = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        searchMealService = SearchMealService(manageMealsSearch, viewMealDetails, consoleIO)
    }

    //region handle user choice
    @Test
    fun `should show option of sub feature when search meal selected`() {
        // When
        searchMealService.showOptionService()

        // Then
        verify {
            consoleIO.viewWithLine("\n\n===Please enter one of the numbers listed below===\n")
            consoleIO.viewWithLine("1. Gym Helper")
            consoleIO.viewWithLine("2. Search by name of meal")
            consoleIO.viewWithLine("3. Search By Date And See Meal Details")
            consoleIO.viewWithLine("4. Explore Country Meals")
            consoleIO.viewWithLine("5. Iraqi Meals")
            consoleIO.viewWithLine("0. Exit")
        }

    }
    //endregion

    //region gym helper
    @Test
    fun `should call gym helper when gym helper selected`() {
        // Given
        val choiceSearchFeature = "1"
        val exitInput = "0"
        every { consoleIO.readInput() } returns choiceSearchFeature andThen exitInput

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.view("enter target of Protein: ")
        }
    }

    @Test
    fun `should call gym helper when valid input selected`() {
        // Given
        val targetCalories = 500.0
        val targetProtein = 30.0
        val meal = createMeal("egg", 1, targetCalories, targetProtein)
        every {
            consoleIO.readInput()
        } returns "1" andThen "$targetCalories" andThen "$targetProtein"

        every {
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(targetCalories, targetProtein)
        } returns listOf(meal)

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.view("enter target of Protein: ")
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(targetCalories, targetProtein)
            consoleIO.viewWithLine(any())
        }
    }

    @Test
    fun `should show message no meals found  when invalid input`() {
        // Given
        val targetCalories = -100.0
        val targetProtein = 100.0
        val choiceSearchFeature = "1"

        every {
            consoleIO.readInput()
        } returns choiceSearchFeature andThen "$targetCalories" andThen "$targetProtein"

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.view("enter target of Protein: ")
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(-100.0, 100.0)
            consoleIO.viewWithLine("\n⚠️ No meals found!\n🍽️ Try searching again or check your filters.\n")
        }
    }

    @Test
    fun `should recursively call itself when throw exception`() {
        // Given
        val errorMessage = "Invalid meal data"
        every {
            consoleIO.readInput()
        } returns "1" andThen "500.0" andThen "30.0" andThen "400.0" andThen "25.0" andThen "0"

        every {
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(500.0, 30.0)
        } throws Exception(errorMessage)

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("enter target of Calories: ")
            consoleIO.readInput()
            consoleIO.view("enter target of Protein: ")
            consoleIO.readInput()
            manageMealsSearch.getGymHelperMealsByCaloriesAndProtein(500.0, 30.0)
            consoleIO.viewWithLine(errorMessage)

        }
    }
    //endregion

    // region search by date
    @Test
    fun `should call get meals by date when search by date selected`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-03-10"
        val expectedMeals = listOf(
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10))
        )
        every { consoleIO.readInput() } returnsMany listOf(searchByDateFeature, date)
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs
        every { manageMealsSearch.getMealsByDate(date) } returns expectedMeals
        every { manageMealsSearch.getMealById(any()) } returns expectedMeals.first()
        every { consoleIO.readInput() } returnsMany listOf(searchByDateFeature, date, "no")

        // When
        searchMealService.handleUserChoice()

        // Then
        verify { manageMealsSearch.getMealsByDate(date) }
    }

    @Test
    fun `should display error message when no meals found on the date`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-01-05"
        val errorMessage = "❌ No Meals Found For The Date [$date]."
        every { consoleIO.readInput() } returnsMany listOf(searchByDateFeature, date)
        every { consoleIO.view(any()) } just Runs
        every { consoleIO.viewWithLine(any()) } just Runs
        every { manageMealsSearch.getMealsByDate(date) } returns emptyList()

        // When
        searchMealService.handleUserChoice()

        // Then
        verify { consoleIO.viewWithLine(errorMessage) }
    }

    @ParameterizedTest
    @CsvSource(
        "2023 05 10",
        "2023@05@10",
        "20230615",
        "2023-600-01",
        "2023/01/02",
        "2023--32",
    )
    fun `should display error and re-ask date until valid`(invalidDate: String) {
        // Given
        every { consoleIO.readInput() } returnsMany listOf(invalidDate, "2023-04-25")
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        every { manageMealsSearch.getMealsByDate(invalidDate) } throws IllegalArgumentException("❌ Invalid date format. Please use (YYYY-MM-DD).")
        every { manageMealsSearch.getMealsByDate("2023-04-25") } returns listOf(mockk(relaxed = true))

        // When
        val result = searchMealService.getValidDate()

        // Then
        Assertions.assertEquals("2023-04-25", result)
        verify { consoleIO.viewWithLine("❌ Invalid date format. Please use (YYYY-MM-DD).") }
    }

    @Test
    fun `should view meals when found meals on the date`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-03-10"
        every { consoleIO.readInput() } returns searchByDateFeature andThen date
        every { manageMealsSearch.getMealsByDate(date) } returns listOf(
            createMeal(submitted = LocalDate(2025, 2, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 3, 10)),
            createMeal(submitted = LocalDate(2025, 4, 10)),
        )

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine(any())
        }
    }

    @Test
    fun `should display meal details when user selects to see details of a meal`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-03-10"
        val mealId = 1
        val meal = createMeal(id = mealId, submitted = LocalDate(2025, 3, 10))
        val mealsOnDate = listOf(meal)
        val wantsToSeeDetails = "yes"

        every { consoleIO.readInput() } returnsMany listOf(
            searchByDateFeature,
            date,
            wantsToSeeDetails,
            mealId.toString()
        )
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs
        every { manageMealsSearch.getMealsByDate(date) } returns mealsOnDate
        every { manageMealsSearch.getMealById(mealId) } returns meal
        every { viewMealDetails.displayMealDetails(meal) } just Runs

        // When
        searchMealService.handleUserChoice()

        // Then
        verify { viewMealDetails.displayMealDetails(meal) }
    }

    @Test
    fun `should display error message when meal ID not found on the date`() {
        // Given
        val searchByDateFeature = "3"
        val date = "2025-03-10"
        val invalidMealId = 999
        val errorMessage = "❌ Meal with ID [$invalidMealId] Not Found On That Date."
        val mealsOnDate = listOf(createMeal(id = 1, submitted = LocalDate(2025, 3, 10)))

        every { consoleIO.readInput() } returnsMany listOf(
            searchByDateFeature,
            date,
            "yes",
            invalidMealId.toString()
        )
        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs
        every { manageMealsSearch.getMealsByDate(date) } returns mealsOnDate

        // When
        searchMealService.handleUserChoice()

        // Then
        verify { consoleIO.viewWithLine(errorMessage) }
    }

    @Test
    fun `should display error if user chooses to view meal details and meal not in mealsOnDate`() {
        // Given
        val mealsOnDate = listOf(createMeal(id = 1, submitted = LocalDate.parse("2025-04-25")))
        val date = "2025-04-25"
        val notFoundId = 99
        val errorMessage = "❌ Meal with ID [$notFoundId] Not Found On That Date."

        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        every { consoleIO.readInput() } returnsMany listOf(date, "yes", notFoundId.toString())

        every { manageMealsSearch.getMealsByDate("2025-04-25") } returns mealsOnDate
        every { manageMealsSearch.getMealById(99) } returns createMeal(id = 99)

        // When
        searchMealService.launchMealsByDate()

        // Then
        verify { consoleIO.viewWithLine(errorMessage) }
    }

    @Test
    fun `should display error if user chooses to view meal details and id meal not found in meals repository`() {
        // Given
        val mealsOnDate = listOf(createMeal(id = 1, submitted = LocalDate.parse("2025-04-25")))
        val date = "2025-04-25"
        val notFoundId = 99
        val errorMessage = "❌ Meal with ID [$notFoundId] Not Found On That Date."

        every { consoleIO.viewWithLine(any()) } just Runs
        every { consoleIO.view(any()) } just Runs

        every { consoleIO.readInput() } returnsMany listOf(date, "yes", notFoundId.toString())

        every { manageMealsSearch.getMealsByDate("2025-04-25") } returns mealsOnDate
        every { manageMealsSearch.getMealById(99) } returns null

        // When
        searchMealService.launchMealsByDate()

        // Then
        verify { consoleIO.viewWithLine(errorMessage) }
    }

    @Test
    fun `should display error when meal ID is invalid format`() {
        // Given
        val errorMessage = "❌ Invalid ID Format, Please Use A Number."
        every { consoleIO.viewWithLine("Please Enter The Meal ID") } just Runs
        every { consoleIO.view("Enter Meal ID: ") } just Runs
        every { consoleIO.viewWithLine(errorMessage) } just Runs

        every { consoleIO.readInput() } returnsMany listOf("abc", "1")

        // When
        val id = searchMealService.getIdInput()

        // Then
        assertEquals(1, id)
        verify { consoleIO.viewWithLine(errorMessage) }
    }

    // endregion

    //region search by name
    @Test
    fun `should call launchSearchMealByName when choice 2 selected`() {
        // Given
        every { consoleIO.readInput() } returns "2" andThen "egg"
        every { manageMealsSearch.getMealByName("egg") } returns listOf(createMeal("egg"))

        // When
        searchMealService.handleUserChoice()

        // Then
        verify {
            consoleIO.view("Enter meal name to search: ")
            manageMealsSearch.getMealByName("egg")
            consoleIO.viewWithLine("Meals found:")
            consoleIO.viewWithLine("egg")
        }
    }

    @Test
    fun `should show error when meal name input is null`() {
        // Given
        every { consoleIO.readInput() } returns "2" andThen null

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("Enter meal name to search: ")
            consoleIO.viewWithLine("❌ Meal name input cannot be null.")
        }
    }

    @Test
    fun `should show error when meal name input is empty`() {
        // Given
        every { consoleIO.readInput() } returns "2" andThen " "

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("Enter meal name to search: ")
            consoleIO.viewWithLine("❌ Meal name input cannot be empty.")
        }
    }

    @Test
    fun `should show error when meal name has numbers`() {
        // Given
        every { consoleIO.readInput() } returns "2" andThen "eg9"

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("Enter meal name to search: ")
            consoleIO.viewWithLine("❌ Meal name must contain only letters and spaces.")
        }
    }

    @Test
    fun `should show message when no meals found by name`() {
        // Given
        every { consoleIO.readInput() } returns "2" andThen "pizza hut"

        every { manageMealsSearch.getMealByName("pizza hut") } returns emptyList()

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("Enter meal name to search: ")
            manageMealsSearch.getMealByName("pizza hut")
            consoleIO.viewWithLine("No meals found matching \"pizza hut\".")
        }
    }

    @Test
    fun `should handle exception thrown from use case while searching by name`() {
        // Given
        val errorMessage = "Something went wrong"
        every { consoleIO.readInput() } returns "2" andThen "egg"

        every {
            manageMealsSearch.getMealByName("egg")
        } throws Exception(errorMessage)

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.view("Enter meal name to search: ")
            consoleIO.viewWithLine("❌ $errorMessage")
        }
    }
    //endregion

}