$(document).ready(function () {
	fetchQuizzes();
});

function fetchQuizzes() {
	$.ajax({
		url: "/api/quizzes",
		method: "GET",
		headers: {
			"Authorization": `Bearer ${TokenStorage.getToken()}`
		},
		dataType: "json",
		success: (quizSummaryDtos) => {
			console.log(quizSummaryDtos);
			displayQuizzes(quizSummaryDtos);
		},
		error: () => {
			
		}
	});
}

function displayQuizzes(quizSummaryDtos) {
	const groupedQuizzes = quizSummaryDtos.reduce((acc, quiz) => {
        const category = quiz.category; // assuming quiz has a 'category' field
        if (!acc[category]) {
            acc[category] = [];
        }
        acc[category].push(quiz);
        return acc;
    }, {});
	
	for (const category in groupedQuizzes) {
        const quizzes = groupedQuizzes[category];
        $("#dashboard-content").append(CardTemplates.createCategorySlider(category, quizzes));
    }
}


function clearContent() {
	$("#dashboard-dashboard-content").empty();
}