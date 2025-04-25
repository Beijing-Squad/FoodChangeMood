package logic.usecases

import com.google.common.truth.Truth.assertThat
import fake.createMeal
import io.mockk.every
import helper.mealWithTags
import io.mockk.every
import io.mockk.mockk
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
    fun `should Return Meals With Both Tags when Meals Contain Italian And Large Group Tags`() {
        val meals = listOf(
            mealWithTags(1, listOf("italian", "for-large-groups")),
            mealWithTags(2, listOf("italian")),
            mealWithTags(3, listOf("for-large-groups")),
            mealWithTags(4, listOf("dessert"))
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        assert(result.size == 1&&result.first().id==1)
    }
    @Test
    fun `should Return EmptyList when No Meal Matches Both Tags`() {
        val meals = listOf(
            mealWithTags(1, listOf("italian")),
            mealWithTags(2, listOf("for-large-groups")),
            mealWithTags(3, listOf("dessert"))
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        Assertions.assertTrue(result.isEmpty())
    }
    @Test
    fun `should Match Tags Case Insensitively when Tags Have Different Cases`() {
        val meals = listOf(
            mealWithTags(1, listOf("ITALIAN", "FOR-LARGE-GROUPS")),
            mealWithTags(2, listOf("Italian", "For-Large-Groups")),
            mealWithTags(3, listOf("ITALIAN")),
            mealWithTags(4, listOf("for-large-groups"))
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        Assertions.assertEquals(2, result.size)
        }
    @Test
    fun `should Return Meals With Extra Tags when They Still Contain Required Tags`() {
        val meals = listOf(
            mealWithTags(1, listOf("italian", "for-large-groups", "holiday")),
            mealWithTags(2, listOf("italian", "for-large-groups"))
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        Assertions.assertEquals(2, result.size)
    }
    @Test
    fun `should Not Include Duplicate Meals when Repository Returns Duplicates`() {
        val meal = mealWithTags(1, listOf("italian", "for-large-groups"))

        every { mealRepository.getAllMeals() } returns listOf(meal, meal)

        val result = useCase.suggestItalianLargeGroupsMeals()

        Assertions.assertEquals(1, result.distinctBy { it.id }.size)
    }
    @Test
    fun ` should Ignore Meals With Empty Or Null Tags when Filtering For Required Tags`() {
        val meals = listOf(
            mealWithTags(1, listOf("italian", "for-large-groups")),
            mealWithTags(2, emptyList()),
            mealWithTags(3, listOf())
        )

        every { mealRepository.getAllMeals() } returns meals

        val result = useCase.suggestItalianLargeGroupsMeals()

        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(1, result.first().id)
    }
//endregion

    //region suggest keto meal
    @Test
    fun suggestKetoMeal() {
    }
//endregion

    //region suggest easy prepared meal
    @Test
    fun suggestEasyPreparedMeal() {
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