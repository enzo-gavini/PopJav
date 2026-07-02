document.addEventListener('DOMContentLoaded', function() {
    var burger = document.getElementById('nav-burger');
    var links = document.getElementById('nav-links');

    if (burger && links) {
        burger.addEventListener('click', function() {
            var isOpen = burger.classList.toggle('active');
            links.classList.toggle('active');
            burger.setAttribute('aria-expanded', isOpen ? 'true' : 'false');
        });
    }

    var admin = document.querySelector('.nav-admin');
    if (admin) {
        document.addEventListener('click', function(e) {
            if (!admin.contains(e.target)) admin.removeAttribute('open');
        });
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') admin.removeAttribute('open');
        });
    }
});
