$(document).ready(function() {
  // 마우스가 mission-box에 들어갈 때
  $('.mission-box').mouseenter(function() {
    // 해당 미션 박스가 클리어 상태인지 확인
    if ($(this).find('form').length) { // 클리어 상태일 경우 form이 있으므로
      $(this).css({
        'background-color': '#bcf7ed', // 배경색 변경
        'color': '#ffffff', // 글자색 변경
        'transform': 'scale(1.03)', // 크기 확대
        'transition': '0.3s' // 전환 효과
      });
    }
  });

  // 미션 성공 처리 (예시)
      $('.mission-box').click(function() {
          // 여기에 미션 성공 로직 추가
          const isSuccess = true; // 성공 여부를 확인하는 로직을 추가해야 함

          if (isSuccess) {
              $(this).addClass('success'); // 성공 클래스 추가
          }
      });

  // 마우스가 mission-box에서 나갈 때
  $('.mission-box').mouseleave(function() {
    // 해당 미션 박스가 클리어 상태인지 확인
    if ($(this).find('form').length) { // 클리어 상태일 경우 form이 있으므로
      $(this).css({
        'background-color': '#e8faf7', // 원래 배경색으로 복원
        'color': '#000000', // 원래 글자색으로 복원
        'transform': 'scale(1)', // 원래 크기로 복원
      });
    }
  });
});
// progress bar js
$(document).ready(function() {
  const progressText = document.querySelector('.progress-text');
  const circle = document.getElementById('circleProgress');
  const radius = 40; // 원의 반지름
  const circumference = 2 * Math.PI * radius;

  function createCircleProgress(percent) {
    const offset = circumference - (percent / 100 * circumference);
    circle.style.strokeDasharray = `${circumference} ${circumference}`;
    circle.style.strokeDashoffset = offset;
  }

  function updateProgressFromText() {
    const progressValue = parseFloat(progressText.textContent);
    if (!isNaN(progressValue)) {
      createCircleProgress(progressValue);
    }
  }

  createCircleProgress(0); // 초기값 설정

  const observer = new MutationObserver(updateProgressFromText);
  observer.observe(progressText, { childList: true, characterData: true, subtree: true });

  updateProgressFromText();
});