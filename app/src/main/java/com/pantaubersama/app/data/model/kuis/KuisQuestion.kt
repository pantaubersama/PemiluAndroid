package com.pantaubersama.app.data.model.kuis

import com.google.gson.annotations.SerializedName

data class KuisQuestions(val questions: List<Question>, val answered: Int, val total: Int)

data class KuisQuestionResponse(
    @SerializedName("data") val data: KuisQuestionData
)

data class KuisQuestionData(
    @SerializedName("quiz_participation") val quizParticipation: QuizParticipation,
    @SerializedName("answered_questions") val answeredQuestions: List<Question>,
    @SerializedName("questions") val questions: List<Question>,
    @SerializedName("meta") val meta: KuisQuestionMeta
)

data class QuizParticipation(
    @SerializedName("id") val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("participated_at") val participatedAt: ParticipatedAt
)

data class ParticipatedAt(
    @SerializedName("iso_8601") val iso8601: String,
    @SerializedName("en") val en: String,
    @SerializedName("id") val id: String
)

data class Question(
    @SerializedName("id") val id: String,
    @SerializedName("content") val content: String,
    @SerializedName("answers") val answers: List<Answer>
)

data class Answer(
    @SerializedName("id") val id: String,
    @SerializedName("content") val content: String
)

data class KuisQuestionMeta(
    @SerializedName("quizzes") val quizzes: QuestionCount
)

data class QuestionCount(
    @SerializedName("quiz_questions_count") val quizQuestionsCount: Int,
    @SerializedName("answered_questions_count") val answeredQuestionsCount: Int
)