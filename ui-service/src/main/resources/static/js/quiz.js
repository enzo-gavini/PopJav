document.addEventListener('DOMContentLoaded', function() {
    const questions = document.querySelectorAll('.quiz-question');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const submitBtn = document.getElementById('submit-btn');
    const counter = document.getElementById('question-counter');
    let current = 0;

    function showQuestion(index) {
        questions.forEach((q, i) => {
            q.style.display = i === index ? 'block' : 'none';
        });
        counter.textContent = (index + 1) + ' / ' + questions.length;
        prevBtn.style.display = index === 0 ? 'none' : 'inline-block';
        if (index === questions.length - 1) {
            nextBtn.style.display = 'none';
            submitBtn.style.display = 'inline-block';
        } else {
            nextBtn.style.display = 'inline-block';
            submitBtn.style.display = 'none';
        }
    }

    prevBtn.addEventListener('click', function() {
        if (current > 0) { current--; showQuestion(current); }
    });

    nextBtn.addEventListener('click', function() {
        if (current < questions.length - 1) { current++; showQuestion(current); }
    });

    showQuestion(0);
});