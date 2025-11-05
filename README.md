# 🛸 RickMortyPedia

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=for-the-badge&logo=android" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue?style=for-the-badge&logo=kotlin" alt="Language">
  <img src="https://img.shields.io/badge/Architecture-Feature--based%20Clean%20MVI-orange?style=for-the-badge" alt="Architecture">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-green?style=for-the-badge&logo=jetpackcompose" alt="UI">
  <img src="https://img.shields.io/badge/Mode-Dark%20Mode%20Supported-black?style=for-the-badge&logo=android" alt="Dark Mode">
</div>

<div align="center">

### 🌌 Welcome to the Universe of Rick and Morty! 🌌
*The ultimate encyclopedia of every character across the most interdimensional series in the galaxy.*

</div>

---

## 📱 About the Project

**RickMortyPedia** is the ultimate portal to explore the vast Rick and Morty multiverse.  
From the genius scientist **Rick Sanchez** to his anxious grandson **Morty Smith**, discover a galaxy full of interdimensional creatures, aliens, and bizarre lifeforms.

### ✨ What Makes RickMortyPedia Special?

- 🔍 **Complete Exploration** – Browse a rich, dynamic character database
- 🎨 **Modern Interface** – Built entirely with Jetpack Compose for a smooth and intuitive UX
- 🧩 **Feature-based Clean MVI Architecture** – Modular, testable, and reactive
- 🌗 **Dark Mode Support** – Automatically adapts to your system theme
- 🚀 **Reactive Performance** – Powered by Kotlin Coroutines and Flow
- 📱 **Native Experience** – Fully written in Kotlin for Android

---

## 📸 Screenshots

<div align="center">
  <img src="https://github.com/user-attachments/assets/bb21bdd8-bb2e-4c51-b8e5-18ca36ea3e5a" width="250" alt="Main Screen"> &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/20f6475f-1154-4a5a-bcfb-1efd7606cd58" width="250" alt="Character Detail"> &nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/7b0a629a-df6c-4156-b407-f743aa5c9640" width="250" alt="Character Detail"> &nbsp;&nbsp;
</div>

---

## 🛠️ Tech Stack

### 🏗️ **Architecture**
- **Feature-based Clean Architecture** – Modular, maintainable, and scalable
- **MVI Pattern (Model–View–Intent)** – Unidirectional data flow and state management
- **Separation of Concerns** – Domain, data, and presentation layers

### 💉 **Dependency Injection**
- **Koin** – Lightweight and pragmatic dependency injection framework for Kotlin

### 🌊 **Asynchronous Programming**
- **Kotlin Coroutines** – Structured concurrency and non-blocking background tasks
- **Flow / StateFlow / SharedFlow** – Reactive data streams and event-driven UI updates

### 🌐 **Networking & Data**
- **Ktor Client** – Modern and asynchronous HTTP client for consuming APIs
- **Room Database** – Local persistence and smart caching mechanism

### 🎨 **User Interface**
- **Jetpack Compose** – Declarative UI toolkit for building native Android apps
- **Material Design 3** – Clean and cohesive design system
- **Compose Navigation** – Type-safe navigation between screens
- 🌗 **Dark Mode** – Seamless switch between light and dark themes

### 🧪 **Testing**
- **JUnit 4** – Core unit testing framework
- **Mockito** – Mocking framework for unit and integration tests
- **Turbine** – Easy testing for Flows and coroutines

---

## 🗺️ Project Structure

RickMortyPedia follows a **feature-based modular structure**, aligned with Clean and MVI architectural principles:

```plaintext
📦 app/
 ┣ 📂 manifests/
 ┃ ┗ 📜 AndroidManifest.xml
 ┣ 📂 kotlin+java/
 ┃ ┗ 📂 dev/
 ┃   ┗ 📂 sergiosabater/
 ┃     ┗ 📂 rickmortypedia/
 ┃       ┣ 📂 core/
 ┃       ┃ ┣ 📂 common/
 ┃       ┃ ┃ ┣ 📂 error/
 ┃       ┃ ┃ ┃ ┗ 📜 DomainError.kt
 ┃       ┃ ┃ ┗ 📜 Extensions.kt
 ┃       ┃ ┣ 📂 data/
 ┃       ┃ ┃ ┗ 📂 database/
 ┃       ┃ ┃   ┗ 📜 AppDatabase.kt
 ┃       ┃ ┣ 📂 di/
 ┃       ┃ ┃ ┣ 📜 CoilModule.kt
 ┃       ┃ ┃ ┣ 📜 DatabaseModule.kt
 ┃       ┃ ┃ ┣ 📜 NavigationModule.kt
 ┃       ┃ ┃ ┣ 📜 NetworkModule.kt
 ┃       ┃ ┃ ┣ 📜 RepositoryModule.kt
 ┃       ┃ ┃ ┣ 📜 UIModule.kt
 ┃       ┃ ┃ ┣ 📜 UseCaseModule.kt
 ┃       ┃ ┃ ┗ 📜 ViewModelModule.kt
 ┃       ┃ ┣ 📂 navigation/
 ┃       ┃ ┃ ┣ 📜 AppNavHost.kt
 ┃       ┃ ┃ ┣ 📜 AppNavigator.kt
 ┃       ┃ ┃ ┗ 📜 NavigationRoute.kt
 ┃       ┃ ┣ 📂 network/
 ┃       ┃ ┃ ┣ 📜 ApiConstants.kt
 ┃       ┃ ┃ ┗ 📜 RickAndMortyApiService.kt
 ┃       ┃ ┗ 📂 ui/
 ┃       ┃   ┗ 📂 theme/
 ┃       ┃     ┣ 🎨 Color.kt
 ┃       ┃     ┣ 🌓 Theme.kt
 ┃       ┃     ┣ ⚙️ ThemeManager.kt
 ┃       ┃     ┣ 💾 ThemePreferences.kt
 ┃       ┃     ┗ ✍️ Type.kt
 ┃       ┣ 📂 features/
 ┃       ┃ ┣ 📂 character/
 ┃       ┃ ┃ ┣ 📂 data/
 ┃       ┃ ┃ ┃ ┣ 📂 local/
 ┃       ┃ ┃ ┃ ┃ ┣ 📜 CharacterDao.kt
 ┃       ┃ ┃ ┃ ┃ ┣ 📜 CharacterEntity.kt
 ┃       ┃ ┃ ┃ ┃ ┣ 🔄 CharacterEntityMapper.kt
 ┃       ┃ ┃ ┃ ┃ ┣ 📜 PaginationInfoDao.kt
 ┃       ┃ ┃ ┃ ┃ ┗ 📜 PaginationInfoEntity.kt
 ┃       ┃ ┃ ┃ ┣ 📂 remote/
 ┃       ┃ ┃ ┃ ┃ ┣ 📜 CharacterDto.kt
 ┃       ┃ ┃ ┃ ┃ ┣ 🔁 CharacterMapper.kt
 ┃       ┃ ┃ ┃ ┃ ┗ 📜 CharacterRepositoryImpl.kt
 ┃       ┃ ┃ ┣ 📂 domain/
 ┃       ┃ ┃ ┃ ┣ 📂 model/
 ┃       ┃ ┃ ┃ ┃ ┗ 👤 Character.kt
 ┃       ┃ ┃ ┃ ┣ 📂 repository/
 ┃       ┃ ┃ ┃ ┃ ┗ 📜 CharacterRepository.kt
 ┃       ┃ ┃ ┃ ┗ 📂 usecase/
 ┃       ┃ ┃ ┃   ┣ 📜 GetCharacterByIdUseCase.kt
 ┃       ┃ ┃ ┃   ┣ 📜 GetCharactersUseCase.kt
 ┃       ┃ ┃ ┃   ┗ 🔍 SearchCharactersUseCase.kt
 ┃       ┃ ┃ ┣ 📂 presentation/
 ┃       ┃ ┃ ┃ ┣ 📂 detail/
 ┃       ┃ ┃ ┃ ┃ ┣ 🖼 CharacterDetailScreen.kt
 ┃       ┃ ┃ ┃ ┃ ┗ 📜 CharacterDetailViewModel.kt
 ┃       ┃ ┃ ┃ ┣ 📂 list/
 ┃       ┃ ┃ ┃ ┃ ┣ 📂 components/
 ┃       ┃ ┃ ┃ ┃ ┃ ┗ 🖼️ CharacterListScreen.kt
 ┃       ┃ ┃ ┃ ┃ ┗ 📜 CharactersListViewModel.kt
 ┃       ┃ ┃ ┗ 📂 error/
 ┃       ┃ ┃   ┗ 📂 presentation/
 ┃       ┃ ┃     ┗ ⚠️ ErrorScreen.kt
 ┃       ┃ ┗ 📂 splash/
 ┃       ┃   ┗ 📂 presentation/
 ┃       ┃     ┣ 🖼 SplashScreen.kt
 ┃       ┃     ┗ 📜 SplashViewModel.kt
 ┃       ┣ 🏠 MainActivity.kt
 ┃       ┗ 🌍 RickMortyPediaApp.kt

```

Each feature contains:
- **UI Layer (Compose + MVI State)**
- **Domain Layer (UseCases, Models)**
- **Data Layer (Repositories, DTOs, API)**

---

## 📝 Roadmap

### 🎯 Upcoming Features

- 🌍 **Location Explorer** – Discover every planet and dimension
- 📺 **Episode Catalog** – Complete information on all episodes
- ❤️ **Favorites System** – Save and manage your favorite characters
- 🔔 **Notifications** – Get alerts about new content and updates

---

<div align="center">

### 🚀 Don’t miss this interdimensional adventure! 🚀
*Built with ❤️, Kotlin, and a touch of mad science.*

<img src="https://media.giphy.com/media/XreQmk7ETCak0/giphy.gif" width="200" alt="Rick and Morty">

<br><br>

**⭐ Give it a star if you like the project! ⭐**

</div>
