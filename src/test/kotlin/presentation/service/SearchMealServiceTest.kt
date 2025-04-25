package presentation.service

import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.beijing.logic.usecases.ManageMealsSearchUseCase
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SearchMealService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

    //region search meal by country
    @Test
    fun `should show meals when valid country name is entered`() {
        // Given
        val country = "Italy"
        val meals = listOf(
            createMeal(name = "Pizza", minutes = 15, ingredients = listOf("cheese", "tomato"), steps = listOf("prep", "bake")),
            createMeal(name = "Pasta", minutes = 20, ingredients = listOf("noodles", "sauce"), steps = listOf("boil", "mix"))
        )
        every { consoleIO.readInput() } returnsMany listOf("4", country, "exit")
        every { manageMealsSearch.getMealByCountry(country) } returns meals

        // When
        searchMealService.showService()

        // Then
        verify {
            consoleIO.viewWithLine("🍽️ Found ${meals.size} meal(s) related to '$country':\n")
            consoleIO.viewWithLine("1. Pizza • ⏱️ 15 mins • 🧂 2 ingredients • 🔧 2 steps")
            consoleIO.viewWithLine("2. Pasta • ⏱️ 20 mins • 🧂 2 ingredients • 🔧 2 steps")
        }
    }

    @Test
    fun `should show warning when empty country input is entered`() {
        // Given
        every { consoleIO.readInput() } returnsMany listOf("4", "", "exit")

        // When
        searchMealService.showService()

        // Then
        verify { consoleIO.viewWithLine("⚠️ Please enter a country name with at least 4 characters.") }
    }

    @Test
    fun `should show warning when short country name is entered`() {
        // Given
        every { consoleIO.readInput() } returnsMany listOf("4", "It", "exit")

        // When
        searchMealService.showService()

        // Then
        verify { consoleIO.viewWithLine("⚠️ Please enter a country name with at least 4 characters.") }
    }

    @Test
    fun `should show warning when numeric country input is entered`() {
        // Given
        every { consoleIO.readInput() } returnsMany listOf("4", "1234", "exit")


        // When
        searchMealService.showService()

        // Then
        verify { consoleIO.viewWithLine("🚫 Please enter a valid name, not just numbers.") }
    }

    @Test
    fun `should show no meals found message when country has no meals`() {
        // Given
        val country = "Atlantis"
        every { consoleIO.readInput() } returnsMany listOf("4", country, "exit")

        every { manageMealsSearch.getMealByCountry(country) } returns emptyList()

        // When
        searchMealService.showService()

        // Then
        verify { consoleIO.viewWithLine("😔 Sorry, no meals found for '$country'. Try another country!") }
    }
    //endregion region search meal by country
}