document.addEventListener('DOMContentLoaded', function() {
    const deleteLinks = document.querySelectorAll('.card-btn-danger');

    deleteLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            if (!confirm('Êtes-vous sûr de vouloir supprimer ?')) {
                event.preventDefault();
            }
        });
    });
});