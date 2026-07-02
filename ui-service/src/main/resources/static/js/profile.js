document.addEventListener('DOMContentLoaded', function() {
    var percentEl = document.querySelector('.profile-progress-percent');
    var fillEl = document.querySelector('.profile-progress-fill');

    if (percentEl && fillEl) {
        var passed = parseInt(percentEl.getAttribute('data-passed'), 10) || 0;
        var total = parseInt(percentEl.getAttribute('data-total'), 10) || 0;
        var percent = total > 0 ? Math.round((passed * 100) / total) : 0;

        percentEl.textContent = percent + '%';
        requestAnimationFrame(function() {
            fillEl.style.width = percent + '%';
        });
    }
});