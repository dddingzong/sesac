document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector(".auth-form");
  const submitButton = document.getElementById("submit-btn");
  const signinButton = document.querySelector(".show-signin");
  const signupButton = document.querySelector(".show-signup");
  const title = document.querySelector(".auth-panel__title");
  const subtitle = document.querySelector(".auth-panel__subtitle");

  const switchMode = (mode) => {
    const isSignin = mode === "signin";
    form.classList.toggle("is-signin", isSignin);
    form.classList.toggle("is-signup", !isSignin);
    form.dataset.mode = isSignin ? "signin" : "signup";
    signinButton.classList.toggle("is-active", isSignin);
    signupButton.classList.toggle("is-active", !isSignin);
    submitButton.textContent = isSignin ? "로그인" : "회원가입";
    title.textContent = isSignin ? "오늘의 연결을 다시 시작해요" : "회복 여정을 함께 만들어요";
    subtitle.textContent = isSignin
      ? "저장된 계정으로 메인 대시보드에 들어갑니다."
      : "회원가입 시 관심 정보를 함께 저장해 맞춤형 화면을 구성합니다.";
  };

  signinButton.addEventListener("click", () => switchMode("signin"));
  signupButton.addEventListener("click", () => switchMode("signup"));

  form.addEventListener("submit", (event) => {
    if (form.dataset.mode !== "signin") {
      event.preventDefault();
      if (window.signupFlow) {
        window.signupFlow.submit();
      }
    }
  });

  switchMode("signin");
});
