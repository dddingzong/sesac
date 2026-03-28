document.addEventListener("DOMContentLoaded", () => {
  document.body.classList.add("is-ready");

  const hoverables = document.querySelectorAll(
    ".surface-card, .metric-chip, .mini-stat, .feed-card, .participant-list__item, .auth-feature, .spotlight-tile"
  );

  hoverables.forEach((element) => {
    element.classList.add("is-hoverable");
  });
});
