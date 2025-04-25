package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ManageMealsSuggestionsUseCaseTest {

    private lateinit var mealRepository: MealRepository
    private lateinit var useCase: ManageMealsSuggestionsUseCase

    @BeforeEach
    fun setup() {
        mealRepository = mockk()
        useCase = ManageMealsSuggestionsUseCase(mealRepository)
    }

    //region suggest sweets with no eggs
    @Test
    fun `suggestSweetsWithNoEggs should return correct sweet meal without eggs`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Egg Tart",
                id = 1,
                tags = listOf("sweet", "dessert"),
                ingredients = listOf("egg", "milk", "sugar")
            ),
            createMeal(
                name = "Chocolate Cake",
                id = 2,
                tags = listOf("sweet", "dessert"),
                ingredients = listOf("flour", "milk", "sugar")
            )
        )

        // When
        val suggestedMeal = useCase.suggestSweetsWithNoEggs()

        // Then
        assertThat(suggestedMeal?.name).isEqualTo("Chocolate Cake")
    }

    @Test
    fun `suggestSweetsWithNoEggs should not return sweet meal when the meals is null`() {
        // Given
        every { mealRepository.getAllMeals() } returns emptyList()

        // When
        val suggestedMeal = useCase.suggestSweetsWithNoEggs()

        // Then
        assertThat(suggestedMeal).isNull()
    }

    @Test
    fun `suggestSweetsWithNoEggs should return correct sweet meal when it's not seen before`() {
        // Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Chocolate Cake",
                id = 1, tags = listOf("sweet", "dessert"),
                ingredients = listOf("flour", "milk", "sugar")
            ),
            createMeal(
                name = "Tart", id = 2,
                tags = listOf("sweet", "dessert"),
                ingredients = listOf("flour", "milk", "sugar")
            )
        )

        // When
        val firstCall = useCase.suggestSweetsWithNoEggs()
        val secondCall = useCase.suggestSweetsWithNoEggs()

        // Then
        assertThat(firstCall?.id).isNotEqualTo(secondCall?.id)
    }

    @Test
    fun `suggestSweetsWithNoEggs should return correct sweet meal when tags has a capitalize letters`() {
        // Given

        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(
                name = "Chocolate Cake",
                id = 1, tags = listOf("SwEeT", "dessert"),
                ingredients = listOf("wheat", "milk", "sugar")
            )
        )

        // When
        val suggestedMeal = useCase.suggestSweetsWithNoEggs()

        // Then
        assertThat(suggestedMeal?.name).isEqualTo("Chocolate Cake")
    }

    @Test
    fun `suggestSweetsWithNoEggs should return null when no sweet tags or there is egg ingredient`() {
        // Given
        createMeal(
            name = "Grilled Fish",
            id = 1, tags = listOf("main dish"),
            ingredients = listOf("egg", "fish", "wheat")
        )
        every { mealRepository.getAllMeals() } returns listOf(createMeal())

        // When
        val suggestedMeal = useCase.suggestSweetsWithNoEggs()

        // Then
        assertThat(suggestedMeal).isNull()
    }
//endregion

    //region suggest ten random meals contains potato test
    @Test
    fun `should return a list of meals containing potato when have it`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(name = "Potato Onion Delight", ingredients = listOf("potato", "onion", "garlic")),
            createMeal(name = "Potato Chicken Stew", ingredients = listOf("potato", "carrot", "chicken")),
            createMeal(name = "Cheesy Potato Bake", ingredients = listOf("potato", "cheese", "cream")),
            createMeal(name = "Beef Potato Casserole", ingredients = listOf("potato", "tomato", "beef")),
            createMeal(name = "Spinach Potato Omelette", ingredients = listOf("potato", "spinach", "egg")),
            createMeal(name = "Potato Rice Mix", ingredients = listOf("potato", "rice", "peas")),
            createMeal(name = "Creamy Corn Potato", ingredients = listOf("potato", "corn", "butter")),
            createMeal(name = "Lemon Fish & Potato", ingredients = listOf("potato", "fish", "lemon")),
            createMeal(name = "Broccoli Potato Gratin", ingredients = listOf("potato", "broccoli", "cheddar")),
            createMeal(name = "Mushroom Potato Stir", ingredients = listOf("potato", "mushroom", "onion")),
            createMeal(name = "Spicy Beef Potato", ingredients = listOf("potato", "beef", "pepper"))
        )
        //When
        val result = useCase.suggestTenRandomMealsContainsPotato()

        //Then
        result.forEach { meal ->
            assertThat(meal.ingredients).contains("potato")
        }

    }

    @Test
    fun `should return ten random meals when passed a list of meals contains potato`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(name = "Potato Onion Delight", ingredients = listOf("potato", "onion", "garlic")),
            createMeal(name = "Potato Chicken Stew", ingredients = listOf("potato", "carrot", "chicken")),
            createMeal(name = "Cheesy Potato Bake", ingredients = listOf("potato", "cheese", "cream")),
            createMeal(name = "Beef Potato Casserole", ingredients = listOf("potato", "tomato", "beef")),
            createMeal(name = "Spinach Potato Omelette", ingredients = listOf("potato", "spinach", "egg")),
            createMeal(name = "Potato Rice Mix", ingredients = listOf("potato", "rice", "peas")),
            createMeal(name = "Creamy Corn Potato", ingredients = listOf("potato", "corn", "butter")),
            createMeal(name = "Lemon Fish & Potato", ingredients = listOf("potato", "fish", "lemon")),
            createMeal(name = "Broccoli Potato Gratin", ingredients = listOf("potato", "broccoli", "cheddar")),
            createMeal(name = "Mushroom Potato Stir", ingredients = listOf("potato", "mushroom", "onion")),
            createMeal(name = "Spicy Beef Potato", ingredients = listOf("potato", "beef", "pepper")),
            createMeal(name = "Potato Egg Toast", ingredients = listOf("potato", "egg", "toast")),
            createMeal(name = "Potato Veggie Wrap", ingredients = listOf("potato", "lettuce", "tomato")),
            createMeal(name = "Potato Lentil Soup", ingredients = listOf("potato", "lentils", "carrot")),
            createMeal(name = "Potato Tuna Bake", ingredients = listOf("potato", "tuna", "cheese")),
            createMeal(name = "Potato BBQ Chicken", ingredients = listOf("potato", "bbq sauce", "chicken")),
            createMeal(name = "Herb Roasted Potato", ingredients = listOf("potato", "rosemary", "olive oil")),
            createMeal(name = "Sweet & Sour Potato", ingredients = listOf("potato", "vinegar", "sugar")),
            createMeal(name = "Potato and Beans Bowl", ingredients = listOf("potato", "beans", "avocado")),
            createMeal(name = "Crispy Potato Skins", ingredients = listOf("potato", "bacon", "cheddar"))
        )

        //When
        val result = useCase.suggestTenRandomMealsContainsPotato()

        //Then
        assertThat(result.size).isEqualTo(10)

    }

    @Test
    fun `should throw exception when have a list of meals does not contain potato`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(name = "Chicken Alfredo", ingredients = listOf("chicken", "pasta", "cream")),
            createMeal(name = "Beef Stir Fry", ingredients = listOf("beef", "broccoli", "soy sauce")),
            createMeal(name = "Grilled Cheese", ingredients = listOf("bread", "cheese", "butter")),
            createMeal(name = "Tomato Basil Soup", ingredients = listOf("tomato", "basil", "onion")),
            createMeal(name = "Shrimp Tacos", ingredients = listOf("shrimp", "lettuce", "sour cream")),
            createMeal(name = "Egg Fried Rice", ingredients = listOf("rice", "egg", "green peas")),
            createMeal(name = "Mushroom Risotto", ingredients = listOf("mushroom", "parmesan", "rice")),
            createMeal(name = "Lamb Kofta", ingredients = listOf("lamb", "spices", "onion")),
            createMeal(name = "Vegan Burger", ingredients = listOf("beans", "lettuce", "tomato")),
            createMeal(name = "Caesar Salad", ingredients = listOf("lettuce", "croutons", "parmesan")),
            createMeal(name = "Tuna Pasta", ingredients = listOf("tuna", "pasta", "mayonnaise"))
        )

        //When
        val exception = assertFailsWith<IllegalArgumentException> {
            useCase.suggestTenRandomMealsContainsPotato()
        }

        //Then
        assertEquals("There are no meals that contain potato.", exception.message)
    }

    @Test
    fun `suggestTenRandomMealsContainsPotato should throw exception when a list with size smaller than ten`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf(
            createMeal(name = "Potato Onion Delight", ingredients = listOf("potato", "onion", "garlic")),
            createMeal(name = "Potato Chicken Stew", ingredients = listOf("potato", "carrot", "chicken")),
            createMeal(name = "Cheesy Potato Bake", ingredients = listOf("potato", "cheese", "cream")),
        )

        //When
        val exception = assertFailsWith<IllegalArgumentException> {
            useCase.suggestTenRandomMealsContainsPotato()
        }

        //Then
        assertEquals("There are not enough meals containing potato to suggest, try another service.", exception.message)
    }

//endregion

    //region suggest italian large group meals
    @Test
    fun suggestItalianLargeGroupsMeals() {
    }
//endregion

    //region suggest keto meal
    @Test
    fun suggestKetoMeal() {
    }
//endregion

    //region suggest easy prepared meal
    @Test
    fun `should return sorted easy meals based on criteria`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Simple Salad", tags = listOf("Healthy"), minutes = 10, nSteps = 3, nIngredients = 4, contributorId = 1),
            createMeal(id = 2, name = "Quick Pasta", tags = listOf("Italian"), minutes = 15, nSteps = 4, nIngredients = 5, contributorId = 2),
            createMeal(id = 3, name = "Fruit Bowl", tags = listOf("Healthy"), minutes = 5, nSteps = 2, nIngredients = 3, contributorId = 3),
            createMeal(id = 4, name = "Rice Dish", tags = listOf("Iraqi"), minutes = 20, nSteps = 5, nIngredients = 7, contributorId = 4)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 3)
        assert(result[0].name == "Fruit Bowl")
        assert(result[1].name == "Simple Salad")
        assert(result[2].name == "Quick Pasta")
    }

    @Test
    fun `should return empty list when no meals meet easy meal criteria`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Hard Meal", tags = listOf("Complex"), minutes = 60, nSteps = 10, nIngredients = 12, contributorId = 1)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.isEmpty())
    }

    @Test
    fun `should return only meals with valid steps, ingredients, and time limit`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Easy Breakfast", tags = listOf("Breakfast"), minutes = 15, nSteps = 3, nIngredients = 4, contributorId = 1),
            createMeal(id = 2, name = "Gourmet Meal", tags = listOf("Dinner"), minutes = 40, nSteps = 7, nIngredients = 10, contributorId = 2)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 1)
        assert(result[0].name == "Easy Breakfast")
    }

    @Test
    fun `should handle edge case with exactly matching meal count`() {
        // Given
        val meals = listOf(
            createMeal(id = 1, name = "Breakfast Smoothie", tags = listOf("Smoothie"), minutes = 5, nSteps = 2, nIngredients = 3, contributorId = 1),
            createMeal(id = 2, name = "Light Salad", tags = listOf("Salad"), minutes = 10, nSteps = 3, nIngredients = 4, contributorId = 2),
            createMeal(id = 3, name = "Quick Wrap", tags = listOf("Wrap"), minutes = 10, nSteps = 3, nIngredients = 5, contributorId = 3)
        )
        every { mealRepository.getAllMeals() } returns meals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 3)
        assert(result[0].name == "Breakfast Smoothie")
        assert(result[1].name == "Light Salad")
        assert(result[2].name == "Quick Wrap")
    }
    //endregion

    //region suggest meal have more than seven hundred calories
    @Test
    fun suggestMealHaveMoreThanSevenHundredCalories() {
    }
//endregion

    //region check meal calories content
    @Test
    fun checkMealCaloriesContent() {
    }
    //endregion
}