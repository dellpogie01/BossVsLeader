# BossVsLeader Android Application

BossVsLeader is a modern micro-learning application built with Kotlin, Jetpack Compose, and Material 3, designed to teach leadership excellence by contrasting Boss behaviors (command, control, blame) with Leader behaviors (inspire, coach, take responsibility).

## User Review & Critical Decisions

> [!IMPORTANT]
> The user confirmed the following preferences in Phase 1:
- **Visual Accent Theme**: Dynamic Boss Red (#D32F2F) and Leader Blue (#0288D1) gradient aesthetic with polished M3 surfaces.
- **Initial Screen**: Home dashboard featuring greeting, streak flame counter, progress ring, and "Lesson of the Day".
- **Quiz Structure**: Relaxed self-paced learning mode with instant explanations and immediate feedback.

- **Confirmed Decision 1**: SQLite persistence via custom `DatabaseHelper` (SQLiteOpenHelper) running on `Dispatchers.IO`, pre-seeded with 20 rich lessons and 30 quiz questions on first launch.
- **Confirmed Decision 2**: Signature 3-second animated splash screen with spring-animated crown and fading whistle icons, leading directly to the Home dashboard.
- **Confirmed Decision 3**: Custom animated top navigation bar with collapsing scroll behavior, shifting gradient background, and animated tab indicator pill.

## 1. Overview & Core Concept

- **What It Does**: Teaches users effective leadership principles through interactive lessons, side-by-side behavioral comparisons, self-paced quizzes, bookmarking, and streak/progress tracking.
- **Target Audience**: Aspiring managers, team leads, professionals, and anyone looking to transition from traditional boss mentalities to inspiring leadership.
- **Key Value**: Engaging micro-learning architecture with immediate practical takeaways, offline SQLite storage, and gorgeous M3 animations.

## 2. User Experience & Visual Design

- **Key User Flows**:
  1. **Splash Screen**: 3-second branded animation with progress bar -> Home dashboard.
  2. **Home Dashboard**: Daily greeting, streak counter with pulse, progress ring, lesson of the day, quick category access.
  3. **Lessons Catalog & Detail**: Categorized lazy column with read times and completion checkmarks; detail view with side-by-side Boss (red panel) vs Leader (blue panel) breakdown, scenarios, key takeaways, and bookmarking.
  4. **Compare Mode**: Swipeable horizontal pager of "Boss says vs Leader says" flip cards.
  5. **Quiz Mode**: Self-paced questions with instant feedback, explanations, score tallying, and persistent history saved to SQLite.
  6. **Profile & Stats**: Comprehensive learning analytics, bookmarks list, dark/light theme toggle, and progress reset option.
- **Visual Identity & Theme**:
  - *Aesthetic Direction*: Polished, premium micro-learning experience with warm Boss red accents, cool Leader blue/teal highlights, and deep charcoal/slate surfaces.
  - *Color Palette & Mood*: Material 3 ColorScheme with custom dynamic gradient headers (`rememberInfiniteTransition`), soft tonal elevation, and glassmorphic card overlays.
  - *Typography & Hierarchy*: Clean Material 3 typography scale utilizing `sp` units for fluid accessibility scaling.

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: SQLite via SQLiteOpenHelper vs Room**
  - *Chosen Approach*: Raw SQLite with explicit DAO-style repository wrapper and coroutines (`Dispatchers.IO`).
  - *Why*: Matches exact prompt requirements, avoids heavy annotation processors while providing robust, reliable local offline storage.
- **Decision 2: Custom Animated Top Navigation Bar**
  - *Chosen Approach*: Custom collapsible app bar with animated gradient background, sliding tab indicator pill, and cross-fading destination titles.
  - *Why*: Delivers the high-end polished experience specified in the app concept without generic template constraints.

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────┐
│                        UI Layer                        │
│  (Jetpack Compose, M3 Theme, Screens: Home, Lessons,   │
│   LessonDetail, Compare, Quiz, Profile, Splash)        │
└──────────────────────────┬─────────────────────────────┘
                           │ StateFlow / Actions
┌──────────────────────────▼─────────────────────────────┐
│                      ViewModel Layer                   │
│         (MainViewModel handling app state & events)    │
└──────────────────────────┬─────────────────────────────┘
                           │ Coroutines (Dispatchers.IO)
┌──────────────────────────▼─────────────────────────────┐
│                     Repository Layer                   │
│          (BossLeaderRepository for data operations)    │
└──────────────────────────┬─────────────────────────────┘
                           │ SQLiteOpenHelper queries
┌──────────────────────────▼─────────────────────────────┐
│                   Database Layer (SQLite)              │
│  tables: lessons, quiz_questions, quiz_results, stats  │
└────────────────────────────────────────────────────────┘
```

- **Data Model & State**:
  - `lessons`: id, title, category, boss_text, leader_text, scenario, takeaway, read_minutes, is_completed, is_bookmarked
  - `quiz_questions`: id, question, option_a, option_b, option_c, option_d, correct_option, explanation, category
  - `quiz_results`: id, score, total, taken_at
  - `user_stats`: id, streak_days, last_active_date, theme_mode
- **Interactive Component & State Mapping**:
  - ViewModel exposes `StateFlow` for lessons, bookmarks, quiz state, user stats, and current tab.
  - SQLite database seeds 20 lessons and 30 quiz questions on first launch (`onCreate`).
