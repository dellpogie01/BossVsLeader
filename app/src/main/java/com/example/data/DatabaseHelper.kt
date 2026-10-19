package com.example.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.model.Lesson
import com.example.model.QuizQuestion
import com.example.model.QuizResult
import com.example.model.UserStats

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "boss_vs_leader.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_LESSONS = "lessons"
        const val TABLE_QUIZ = "quiz_questions"
        const val TABLE_RESULTS = "quiz_results"
        const val TABLE_STATS = "user_stats"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_LESSONS (
                id INTEGER PRIMARY KEY,
                title TEXT,
                category TEXT,
                boss_text TEXT,
                leader_text TEXT,
                scenario TEXT,
                takeaway TEXT,
                read_minutes INTEGER,
                is_completed INTEGER DEFAULT 0,
                is_bookmarked INTEGER DEFAULT 0
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_QUIZ (
                id INTEGER PRIMARY KEY,
                question TEXT,
                option_a TEXT,
                option_b TEXT,
                option_c TEXT,
                option_d TEXT,
                correct_option INTEGER,
                explanation TEXT,
                category TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_RESULTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                score INTEGER,
                total INTEGER,
                taken_at INTEGER
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLE_STATS (
                id INTEGER PRIMARY KEY,
                streak_days INTEGER,
                last_active_date TEXT,
                theme_mode TEXT
            )
        """)

        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LESSONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUIZ")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESULTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATS")
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // Initial User Stats
        db.insertOrThrow(TABLE_STATS, null, ContentValues().apply {
            put("id", 1)
            put("streak_days", 5)
            put("last_active_date", "2026-10-19")
            put("theme_mode", "SYSTEM")
        })

        // 20 Lessons spanning 6 categories
        val lessons = listOf(
            Triple(1, "Delegating Control vs Empowering Growth", "Accountability"),
            Triple(2, "Giving Blame vs Taking Responsibility", "Accountability"),
            Triple(3, "Micromanaging Every Step vs Setting Clear Boundaries", "Trust"),
            Triple(4, "Creating Fear vs Building Psychological Safety", "Trust"),
            Triple(5, "Demanding Compliance vs Inspiring Purpose", "Motivation"),
            Triple(6, "Financial Incentives Only vs Intrinsic Engagement", "Motivation"),
            Triple(7, "One-Way Dictation vs Two-Way Dialogue", "Communication"),
            Triple(8, "Silencing Feedback vs Welcoming Constructive Truth", "Communication"),
            Triple(9, "Deciding Alone vs Inclusive Collaborative Problem Solving", "Decision-Making"),
            Triple(10, "Reacting to Crises vs Proactive Strategic Coaching", "Decision-Making"),
            Triple(11, "Focusing on Mistakes vs Celebrating Growth & Learning", "Feedback"),
            Triple(12, "Annual Reviews as Punishment vs Continuous Feedforward", "Feedback"),
            Triple(13, "Hoarding Information vs Transparent Sharing", "Communication"),
            Triple(14, "Taking Credit for Team Success vs Elevating Team Members", "Accountability"),
            Triple(15, "Assuming Authority vs Earning Respect", "Trust"),
            Triple(16, "Pushing Harder vs Removing Roadblocks", "Motivation"),
            Triple(17, "Closed Door Policy vs Open Accessibility", "Communication"),
            Triple(18, "Rigid Process Adherence vs Creative Innovation", "Decision-Making"),
            Triple(19, "Punishing Failure vs Encouraging Calculated Risk", "Accountability"),
            Triple(20, "Managing Tasks vs Developing People", "Trust")
        )

        for (l in lessons) {
            val bossText = when(l.first) {
                1 -> "Tells employees exactly how to do every minute task and checks in constantly."
                2 -> "Blames team members publicly when targets are missed to protect own reputation."
                3 -> "Watches over shoulders and demands status reports every hour."
                4 -> "Rules with intimidation, making team members terrified of bringing up problems."
                5 -> "Uses threats of reprimand or termination to force compliance."
                6 -> "Believes money is the only thing people care about; ignores emotional connection."
                7 -> "Talks down to the team; gives orders without explanation or context."
                8 -> "Defends flaws aggressively and penalizes anyone who questions authority."
                9 -> "Makes all decisions in isolation and expects immediate uncritical obedience."
                10 -> "Panics during stress and shifts blame downwards."
                11 -> "Pounces on errors to reprimand and demean employees."
                12 -> "Saves up grievances for a terrifying annual performance review."
                13 -> "Keeps key metrics and company direction hidden from staff."
                14 -> "Accepts praise in executive meetings while ignoring team contributions."
                15 -> "Demands respect simply based on title and hierarchy."
                16 -> "Demands double output with half the resources."
                17 -> "Keeps office door shut; inaccessible to regular staff."
                18 -> "Stifles new ideas to maintain comfortable status quo."
                19 -> "Treats any mistake as a sackable offense."
                else -> "Treats people as replaceable cogs in a machine."
            }

            val leaderText = when(l.first) {
                1 -> "Sets clear outcomes and empowers team members to determine the path."
                2 -> "Steps up immediately to absorb pressure and analyses systemic causes."
                3 -> "Establishes clear guidelines and gives autonomy with robust support."
                4 -> "Encourages transparent discussion, curiosity, and fearless brainstorming."
                5 -> "Connects daily tasks to a compelling shared vision and mission."
                6 -> "Recognizes intrinsic motivators like mastery, autonomy, and purpose."
                7 -> "Engages in active listening, asks guiding questions, and shares context."
                8 -> "Welcomes dissent and treats feedback as a vital gift for improvement."
                9 -> "Facilitates collaborative sessions to harness collective intelligence."
                10 -> "Stays calm, evaluates options methodically, and guides the team forward."
                11 -> "Views setbacks as powerful learning opportunities and springboards."
                12 -> "Provides continuous, empathetic, actionable feedforward every single week."
                13 -> "Shares context and business realities openly with the entire team."
                14 -> "Shines the spotlight brightly on team achievements and elevates others."
                15 -> "Earns deep respect daily through integrity, empathy, and competence."
                16 -> "Acts as a servant leader by actively removing bureaucratic blockers."
                17 -> "Maintains an open-door, approachable presence."
                18 -> "Champions experimentation, agility, and continuous innovation."
                19 -> "Encourages safe experimentation and safe failure analysis."
                else -> "Invests deeply in mentoring and unlocking individual human potential."
            }

            val scenario = "A critical project deadline is approaching and the team hits an unexpected technical hurdle."
            val takeaway = "True leadership shifts focus from directing actions to empowering capability and removing obstacles."

            db.insertOrThrow(TABLE_LESSONS, null, ContentValues().apply {
                put("id", l.first)
                put("title", l.second)
                put("category", l.third)
                put("boss_text", bossText)
                put("leader_text", leaderText)
                put("scenario", scenario)
                put("takeaway", takeaway)
                put("read_minutes", 3)
                put("is_completed", if (l.first <= 3) 1 else 0)
                put("is_bookmarked", if (l.first == 1 || l.first == 5) 1 else 0)
            })
        }

        // 30 Quiz Questions
        val quizQuestions = listOf(
            Triple(1, "When a mistake happens, a Boss typically:", "Finds who is to blame"),
            Triple(2, "When a team succeeds, a Leader:", "Shines spotlight on the team"),
            Triple(3, "How does a Boss communicate?", "Issues top-down commands"),
            Triple(4, "How does a Leader build trust?", "Through consistency and transparency"),
            Triple(5, "When setting goals, a Boss focuses on:", "Strict compliance and metrics"),
            Triple(6, "When setting goals, a Leader focuses on:", "Shared vision and purpose"),
            Triple(7, "A Boss views employee feedback as:", "A threat to authority"),
            Triple(8, "A Leader views employee feedback as:", "An opportunity to grow"),
            Triple(9, "In decision making, a Boss:", "Decides alone in isolation"),
            Triple(10, "In decision making, a Leader:", "Collaborates with the team"),
            Triple(11, "How does a Boss handle stress?", "Panics and shifts pressure down"),
            Triple(12, "How does a Leader handle stress?", "Stays calm and protects the team"),
            Triple(13, "A Boss motivates primarily through:", "Fear and pressure"),
            Triple(14, "A Leader motivates primarily through:", "Purpose and inspiration"),
            Triple(15, "A Boss views training staff as:", "A waste of valuable time"),
            Triple(16, "A Leader views training staff as:", "An essential investment"),
            Triple(17, "When conflict arises, a Boss:", "Imposes a unilateral ruling"),
            Triple(18, "When conflict arises, a Leader:", "Facilitates constructive dialogue"),
            Triple(19, "A Boss asks:", "'Why didn't you do what I said?'"),
            Triple(20, "A Leader asks:", "'What is blocking your progress?'"),
            Triple(21, "A Boss takes credit for:", "All successful outcomes"),
            Triple(22, "A Leader takes responsibility for:", "Team failures"),
            Triple(23, "A Boss treats team members as:", "Replaceable subordinates"),
            Triple(24, "A Leader treats team members as:", "Valued partners"),
            Triple(25, "A Boss measures success by:", "Hours logged at desk"),
            Triple(26, "A Leader measures success by:", "Real value and outcomes delivered"),
            Triple(27, "A Boss's door is typically:", "Closed and locked"),
            Triple(28, "A Leader's door is typically:", "Open and welcoming"),
            Triple(29, "A Boss drives people by:", "Pushing from behind"),
            Triple(30, "A Leader leads people by:", "Pulling from the front")
        )

        for (q in quizQuestions) {
            val qText = q.second
            val correctAns = q.third
            val opts = mutableListOf(correctAns, "Ignores the issue entirely", "Punishes everyone equally", "Delegates punishment")
            opts.shuffle()
            val correctIdx = opts.indexOf(correctAns)

            db.insertOrThrow(TABLE_QUIZ, null, ContentValues().apply {
                put("id", q.first)
                put("question", qText)
                put("option_a", opts[0])
                put("option_b", opts[1])
                put("option_c", opts[2])
                put("option_d", opts[3])
                put("correct_option", correctIdx)
                put("explanation", "A true leader fosters trust, collaboration, and accountability rather than command and control.")
                put("category", "Leadership Principles")
            })
        }
    }

    // DAO Helper Methods
    fun getAllLessons(): List<Lesson> {
        val list = mutableListOf<Lesson>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_LESSONS", null)
        cursor.use {
            while (cursor.moveToNext()) {
                list.add(
                    Lesson(
                        id = cursor.getInt(0),
                        title = cursor.getString(1),
                        category = cursor.getString(2),
                        bossText = cursor.getString(3),
                        leaderText = cursor.getString(4),
                        scenario = cursor.getString(5),
                        takeaway = cursor.getString(6),
                        readMinutes = cursor.getInt(7),
                        isCompleted = cursor.getInt(8) == 1,
                        isBookmarked = cursor.getInt(9) == 1
                    )
                )
            }
        }
        return list
    }

    fun updateLessonCompletion(id: Int, completed: Boolean) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("is_completed", if (completed) 1 else 0)
        }
        db.update(TABLE_LESSONS, cv, "id = ?", arrayOf(id.toString()))
    }

    fun updateLessonBookmark(id: Int, bookmarked: Boolean) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("is_bookmarked", if (bookmarked) 1 else 0)
        }
        db.update(TABLE_LESSONS, cv, "id = ?", arrayOf(id.toString()))
    }

    fun getAllQuizQuestions(): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_QUIZ", null)
        cursor.use {
            while (cursor.moveToNext()) {
                list.add(
                    QuizQuestion(
                        id = cursor.getInt(0),
                        question = cursor.getString(1),
                        optionA = cursor.getString(2),
                        optionB = cursor.getString(3),
                        optionC = cursor.getString(4),
                        optionD = cursor.getString(5),
                        correctOption = cursor.getInt(6),
                        explanation = cursor.getString(7),
                        category = cursor.getString(8)
                    )
                )
            }
        }
        return list
    }

    fun saveQuizResult(score: Int, total: Int) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("score", score)
            put("total", total)
            put("taken_at", System.currentTimeMillis())
        }
        db.insert(TABLE_RESULTS, null, cv)
    }

    fun getQuizResults(): List<QuizResult> {
        val list = mutableListOf<QuizResult>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_RESULTS ORDER BY taken_at DESC", null)
        cursor.use {
            while (cursor.moveToNext()) {
                list.add(
                    QuizResult(
                        id = cursor.getInt(0),
                        score = cursor.getInt(1),
                        total = cursor.getInt(2),
                        takenAt = cursor.getLong(3)
                    )
                )
            }
        }
        return list
    }

    fun getUserStats(): UserStats {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT streak_days, last_active_date, theme_mode FROM $TABLE_STATS WHERE id = 1", null)
        cursor.use {
            if (cursor.moveToFirst()) {
                return UserStats(
                    streakDays = cursor.getInt(0),
                    lastActiveDate = cursor.getString(1) ?: "2026-10-19",
                    themeMode = cursor.getString(2) ?: "SYSTEM"
                )
            }
        }
        return UserStats(5, "2026-10-19", "SYSTEM")
    }

    fun updateUserStats(streak: Int, theme: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("streak_days", streak)
            put("theme_mode", theme)
        }
        db.update(TABLE_STATS, cv, "id = 1", null)
    }

    fun resetProgress() {
        val db = writableDatabase
        db.execSQL("UPDATE $TABLE_LESSONS SET is_completed = 0, is_bookmarked = 0")
        db.execSQL("DELETE FROM $TABLE_RESULTS")
        db.execSQL("UPDATE $TABLE_STATS SET streak_days = 1 WHERE id = 1")
    }
}
