const QuizGame = {
    currentQuestionIndex: 0,
    score: 0,
    questionIds: [],
    userAnswers: [],
    currentQuestion: null,
    answeredQuestions: new Set(),
    selectedAnswers: [],

    init(quizGameDto) {
		console.log(quizGameDto.questionIds);
        this.questionIds = quizGameDto.questionIds;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.userAnswers = [];
        this.answeredQuestions.clear(); // Clear previously answered questions
        this.loadQuestion();
    },

    loadQuestion() {
        if (this.currentQuestionIndex >= this.questionIds.length) {
            this.endGame();
            return;
        }

        const questionId = this.questionIds[this.currentQuestionIndex];
        this.selectedAnswers = []; // Clear selected answers for this question
        this.fetchQuestion(questionId);
    },

    fetchQuestion(questionId) {
        $.ajax({
            url: `/api/questions/${questionId}`,
            method: "GET",
            dataType: "json",
            success: (questionDto) => {
                this.currentQuestion = questionDto;
                this.displayQuestion();
            },
            error: () => {
                console.error(`Error retrieving question with id ${questionId}`);
            }
        });
    },

    displayQuestion() {
        if (!this.currentQuestion) return;

        $("#question-text").text(this.currentQuestion.text);
        const answerGroup = $("#answer-btn-group").empty();

        this.currentQuestion.answerDtos.forEach((answer) => {
            let btn = QuizTemplates.answerButton(answer);
            btn.attr("id", answer.id);
            
            if (this.answeredQuestions.has(this.currentQuestion.id)) {
                btn.prop("disabled", true); // Disable button if question is answered
                if (this.selectedAnswers.includes(answer.id)) {
                    btn.addClass("selected"); // Show selected answer
                }
            } else {
                btn.on("click", () => this.toggleAnswerSelection(answer.id, btn));
            }

            answerGroup.append(btn);
        });

        // Add Submit Button only if the current question is not answered
        if (!this.answeredQuestions.has(this.currentQuestion.id)) {
            this.addSubmitButton();
        }
    },

    addSubmitButton() {
        const submitBtn = $("<button>")
            .text("Submit Answer")
            .addClass("submit-btn")
            .on("click", () => this.submitAnswer());

        $("#answer-btn-group").append(submitBtn);
    },

    toggleAnswerSelection(answerId, btn) {
        if (this.selectedAnswers.includes(answerId)) {
            this.selectedAnswers = this.selectedAnswers.filter(id => id !== answerId);
            btn.removeClass("selected"); // Unhighlight
        } else {
            this.selectedAnswers.push(answerId);
            btn.addClass("selected"); // Highlight
        }
    },

    submitAnswer() {
        if (!this.currentQuestion) return;

        const correctAnswers = new Set(
            this.currentQuestion.answerDtos.filter(a => a.correct).map(a => a.id)
        );

        // Check if the user's selected answers are correct
        const isCorrect = this.checkSelectedAnswers(correctAnswers);

        if (isCorrect) {
            this.score++;
        }

        this.userAnswers.push({
            questionId: this.currentQuestion.id,
            selectedAnswers: this.selectedAnswers,
            correct: isCorrect
        });

        // Mark the current question as answered
        this.answeredQuestions.add(this.currentQuestion.id);

        // Provide feedback to the user
        this.showAnswerFeedback(isCorrect);

        // Move to the next question after a brief delay
        setTimeout(() => {
            this.currentQuestionIndex++;
            this.loadQuestion();
        }, 1000); // Wait for 1 second before moving to the next question
    },

    checkSelectedAnswers(correctAnswers) {
        // Ensure every selected answer is correct and no wrong answers are selected
        return this.selectedAnswers.every(id => correctAnswers.has(id)) &&
            correctAnswers.size === this.selectedAnswers.length;
    },

    showAnswerFeedback(isCorrect) {
        const feedback = isCorrect ? "Correct!" : "Incorrect!";
        $("#answer-feedback").text(feedback).show().fadeOut(1000);
    },

    endGame() {
        alert(`Game over! Your score: ${this.score}`);
    },

    // Additional methods for question navigation
    goToNextQuestion() {
        if (this.currentQuestionIndex < this.questionIds.length - 1) {
            this.currentQuestionIndex++;
            this.loadQuestion();
        }
    },

    goToPreviousQuestion() {
        if (this.currentQuestionIndex > 0) {
            this.currentQuestionIndex--;
            this.loadQuestion();
        }
    }
};



// ----------------- Document ready setup ------------------
$(document).ready(function () {
    $.ajax({
        url: "/api/quizzes/1",
        method: "GET",
        dataType: "json",
        success: (quizDto) => QuizGame.init(quizDto),
        error: (response) => {
            console.log(`Issue retrieving quiz... ${response.responseText}`);
        }
    });

    $("#play-btn").off("click").on("click", () => {
        QuizGame.loadQuestion();
    });

    // Navigation buttons for next and previous questions
    $("#next-btn").on("click", () => QuizGame.goToNextQuestion());
    $("#prev-btn").on("click", () => QuizGame.goToPreviousQuestion());
});




// ----------------- TEMPLATES -------------------
const QuizTemplates = {
    answerButton: function (answer) {
        return $("<button>")
            .attr("type", "button")
            .addClass("question-btn question-answer-btn")
            .text(answer.text);
    }
};
