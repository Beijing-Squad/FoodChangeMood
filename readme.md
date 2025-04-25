# Food Change Mood App 🍔🍽️

Welcome to the **Food Change Mood App**! This app allows users to interact with various meal-related features. The application uses meal data from a CSV file and provides multiple functionalities related to meal suggestions, searching, games, and much more. The app also leverages **Koin** for **Dependency Injection** to manage dependencies across different components.

## Features 🍳

The app includes the following main categories and features:

### **Main Categories (Available in CLI Menu)**

These are the top-level categories that appear when the app starts in the CLI. Each category will allow you to choose specific actions or functionalities.

1. **Meal Suggestions 🍲**
2. **Meal Search 🔍**
3. **Meal Games 🎮**
4. **Meal Viewing 🍴**

### **Sub Features**

After selecting a category, you will be presented with more specific features. Here's a breakdown of what each category offers:

#### **Meal Suggestions 🍲**
- **Sweets with No Eggs 🍩**: Suggest one sweet that contains no eggs for users who are allergic to eggs. Users can like or dislike the suggestion to get a new one.
- **Keto Diet Meal Helper 🥗**: Suggest keto-friendly meals based on nutritional information.
- **I Love Potato 🥔**: Suggest meals that contain potatoes.
- **So Thin Problem 🍽️**: Suggest meals with more than 700 calories.
- **Large Group Italian Meal Helper 🇮🇹** :Suggest original Italian dishes suitable for large groups — ideal for friends traveling together.
- **Easy Food Suggestion 🎯** :this feature suggests meals that are easy to prepare.

#### **Meal Search 🔍**
- **Search Meals by Name**: Search for meals based on their name. It includes fuzzy search functionality to handle typos.
- **Search Foods by Date 📅**: Search meals by the date entered by user.
- **Gym Helper 💪**: Suggest meals based on desired calorie and protein intake.
- **Explore Other Countries' Food Culture 🌍** :Search for meals from a specific country by name and return up to 20 random matching meals.

#### **Meal Games 🎮**
- **Guess the Meal Preparation Time ⏳**: Guess how long it takes to prepare a random meal. You have 3 attempts to get it right.
- **Ingredient Game 🧩**: A fun game where the user guesses the correct ingredient in a meal.

#### **Meal Viewing 🍴**
- **Suggest a Healthy Meal**: Get meal suggestions based on various criteria (e.g., calories, fat content, etc.).
- **Seafood Meals Sorted by Protein Content 🦞**: Lists seafood meals sorted by protein content.

## Structure Overview 🏗️

### **Main Components**

- **MealRepository 📦**: Interface that defines methods for accessing meal data.
- **CsvMealRepository 📊**: Implementation of `MealRepository` that reads and parses meal data from a CSV file.
- **FoodConsoleUi 🖥️**: Provides the user interface, allowing interaction with the app's features.
- **GamesMealsUseCases 🎮**: Handles the game logic where users guess meal preparation times.
- **ViewMealsUseCases 🍴**: Manages viewing details of meals.
- **SuggestionMealsUseCases 🍽️**: Manages meal suggestions.
- **SearchMealsUseCases 🔍**: Handles meal search logic.

### **Meal Data Handling 🍽️**

The app retrieves meal data from a CSV file and provides an efficient caching mechanism using Kotlin's `lazy` initialization. The `CsvMealRepository` class handles reading the CSV file and parsing it into a list of `Meal` objects.

### **Dependency Injection with Koin ⚡**

**Koin** is a lightweight dependency injection framework for Kotlin. It allows us to manage the application's dependencies in a simple, easy-to-understand manner, reducing the boilerplate code associated with traditional dependency injection.

#### **Why Use Koin? 🤔**

Koin simplifies the process of managing dependencies in Kotlin-based applications. It is particularly useful for:

- **Decoupling components**: Dependencies are injected rather than manually created, which makes the components independent of one another.
- **Easy to set up**: Unlike other dependency injection frameworks, Koin doesn't require annotation processors or complex setup steps.
- **Lightweight**: Koin's size and learning curve are minimal, making it a great choice for small and medium-sized Kotlin applications.

#### **How Koin is Used in the Project 🔧**

In this project, **Koin** is used to manage dependencies between the different components, such as the user interface, business logic, and data handling. Koin makes it easy to inject dependencies without having to manually create them in each class.

#### **Koin Modules 📦**

The app uses the following Koin modules to manage the dependencies:

- **AppModule 🗂️**: This is the main module that aggregates all the other modules and provides dependency injection throughout the app.
- **Data Module 🗂️**: Handles the creation of the repository and services responsible for meal data fetching and parsing.
- **Logic Module 🧠**: Handles the use cases and business logic, like meal suggestions, searching, and games.
- **UI Module 🖥️**: Provides the user interface components (like the `FoodConsoleUi` which is responsible for displaying the app's features to the user).
