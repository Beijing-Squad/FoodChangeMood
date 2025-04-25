package logic.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import fake.createMeal
import io.mockk.every
import io.mockk.mockk
import fake.mealsListWithHighCaloriesMeals
import fake.mealsWithHighCalories
import fake.mealsWithNoHighCalories
import kotlinx.datetime.LocalDate
import org.beijing.logic.MealRepository
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.model.Meal
import org.beijing.model.Nutrition
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
    private val easyMeals = listOf(
        Meal("Meal A", 1, 20, 1, LocalDate(2023, 5, 1), listOf("easy"),
            Nutrition(100.0, 5.0, 5.0, 200.0, 10.0, 2.0, 20.0),
            3, listOf("Step1", "Step2", "Step3"), "Tasty A", listOf("ing1", "ing2"), 2),

        Meal("Meal B", 2, 25, 1, LocalDate(2023, 5, 1), listOf("easy"),
            Nutrition(200.0, 8.0, 10.0, 250.0, 15.0, 3.0, 30.0),
            2, listOf("Step1", "Step2"), "Tasty B", listOf("ing1", "ing2", "ing3"), 3),

        Meal("Meal C", 3, 15, 1, LocalDate(2023, 5, 1), listOf("easy"),
            Nutrition(150.0, 7.0, 9.0, 220.0, 12.0, 3.5, 25.0),
            1, listOf("Step1"), "Tasty C", listOf("ing1"), 1),

        Meal("Hard Meal", 4, 40, 1, LocalDate(2023, 5, 1), listOf("not-easy"),
            Nutrition(300.0, 10.0, 15.0, 300.0, 18.0, 5.0, 35.0),
            6, listOf("Step1"), "Too hard", List(7) { "ingredient" }, 7)
    )
    @Test
    fun `should return top N_EASY_MEAL sorted by minutes then steps then ingredients`() {
        // Given
        every { mealRepository.getAllMeals() } returns easyMeals

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 3)
        assert(result[0].name == "Meal C")
        assert(result[1].name == "Meal A")
        assert(result[2].name == "Meal B")
    }
    @Test
    fun `should return empty list if no meals match the easy filter`() {
        // Given
        val hardOnly = listOf(easyMeals.last())
        every { mealRepository.getAllMeals() } returns hardOnly

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.isEmpty())
    }
    @Test
    fun `should return less than N_EASY_MEAL if not enough easy meals exist`() {
        // Given
        val onlyOne = listOf(easyMeals.first(), easyMeals.last())
        every { mealRepository.getAllMeals() } returns onlyOne

        // When
        val result = useCase.suggestEasyPreparedMeal()

        // Then
        assert(result.size == 1)
        assert(result[0].name == "Meal A")
    }


    //endregion

    //region suggest meal have more than seven hundred calories
    @Test
    fun `should return meals with high calories When give list of meals`() {
        //Given
        every { mealRepository.getAllMeals() } returns mealsListWithHighCaloriesMeals
        //When
        val result = useCase.suggestMealHaveMoreThanSevenHundredCalories()
        //Then
        assertThat(result).containsExactlyElementsIn(mealsWithHighCalories)
    }

    @Test
    fun `should return empty list When give empty list`() {
        //Given
        every { mealRepository.getAllMeals() } returns listOf()
        //When
        val result = useCase.suggestMealHaveMoreThanSevenHundredCalories()
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list When give list of meals have low calories`() {
        //Given
        every { mealRepository.getAllMeals() } returns mealsWithNoHighCalories
        //When
        val result = useCase.suggestMealHaveMoreThanSevenHundredCalories()
        //Then
        assertThat(result).isEmpty()
    }
    //endregion

    //region check meal calories content
    @Test
    fun checkMealCaloriesContent() {
    }
    //endregion
}