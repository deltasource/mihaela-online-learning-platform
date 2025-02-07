document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.getElementById('searchInput');
  const categoryButtons = document.querySelectorAll('.category-btn');
  const courseCards = document.querySelectorAll('.course-card');

  searchInput.addEventListener('input', function(e) {
      const searchTerm = e.target.value.toLowerCase();
      
      courseCards.forEach(card => {
          const text = card.textContent.toLowerCase();
          card.style.display = text.includes(searchTerm) ? 'grid' : 'none';
      });
  });

  categoryButtons.forEach(button => {
      button.addEventListener('click', function() {
          categoryButtons.forEach(btn => btn.classList.remove('active'));
          this.classList.add('active');
          courseCards.forEach(card => {
              card.style.opacity = '0';
              setTimeout(() => {
                  card.style.opacity = '1';
              }, 300);
          });
      });
  });

  document.querySelectorAll('button').forEach(button => {
      button.addEventListener('click', function(e) {
          if (this.getAttribute('href')) {
              e.preventDefault();
              document.querySelector(this.getAttribute('href')).scrollIntoView({
                  behavior: 'smooth'
              });
          }
      });
  });

  courseCards.forEach(card => {
      card.addEventListener('mouseenter', function() {
          this.style.transform = 'translateY(-5px)';
      });

      card.addEventListener('mouseleave', function() {
          this.style.transform = 'translateY(0)';
      });
  });
});
