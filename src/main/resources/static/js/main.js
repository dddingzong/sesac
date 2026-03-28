document.addEventListener("DOMContentLoaded", () => {
  const missionCards = document.querySelectorAll(".mission-card");
  missionCards.forEach((card) => {
    if (card.classList.contains("is-complete")) {
      return;
    }

    card.addEventListener("mouseenter", () => {
      card.style.transform = "translateY(-3px)";
      card.style.boxShadow = "0 18px 28px rgba(61, 49, 36, 0.12)";
    });

    card.addEventListener("mouseleave", () => {
      card.style.transform = "";
      card.style.boxShadow = "";
    });
  });

  const progressText = document.querySelector(".progress-text");
  const circle = document.getElementById("circleProgress");
  if (!progressText || !circle) {
    return;
  }

  const radius = 52;
  const circumference = 2 * Math.PI * radius;
  circle.style.strokeDasharray = `${circumference} ${circumference}`;

  const renderProgress = () => {
    const progressValue = parseFloat(progressText.textContent);
    const percent = Number.isNaN(progressValue) ? 0 : Math.max(0, Math.min(100, progressValue));
    const offset = circumference - (percent / 100) * circumference;
    circle.style.strokeDashoffset = offset;
  };

  renderProgress();
});
