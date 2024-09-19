$(document).ready(function() {
  // 마우스가 mission-box에 들어갈 때
  $('.mission-box').mouseenter(function() {
    $(this).css({
      'background-color': '#bcf7ed', // 배경색 변경
      'color': '#ffffff', // 글자색 변경
      'transform': 'scale(1.03)', // 크기 확대
      'transition': '0.3s' // 전환 효과
    });
  });

  // 마우스가 mission-box에서 나갈 때
  $('.mission-box').mouseleave(function() {
    $(this).css({
      'background-color': '#e8faf7', // 원래 배경색으로 복원
      'color': '#000000', // 원래 글자색으로 복원
      'transform': 'scale(1)', // 원래 크기로 복원
    });
  });
});

$(document).ready(function() {
  let progress = 0;
  const interval = setInterval(() => {
    if (progress >= 100) {
      clearInterval(interval);
    } else {
      progress++;
      $('#percentage').text(progress + '%');
      const circumference = 2 * Math.PI * 60; // 반지름을 사용하여 원주 계산
      const offset = circumference - (progress / 100) * circumference;
      $('#progressCircle').css('stroke-dasharray', `${circumference} ${circumference}`);
      $('#progressCircle').css('stroke-dashoffset', offset);
    }
  }, 100); // 100ms 간격으로 증가
});
