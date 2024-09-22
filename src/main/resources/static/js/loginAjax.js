var index = {
    init: function () {
        var _this = this;
        $('#submit-btn').on('click', function () {
            // 버튼의 텍스트를 확인
            var buttonText = $(this).text().trim();

            if (buttonText === '회원가입') {
                _this.signup();
            } else if (buttonText === '로그인') {
                console.log('에러발생')
//                _this.signin();
            }
        });
    },

    signup : function () {
        var data = {
            name : $('#name').val(),
            loginId : $('#loginId').val(),
            loginPassword : $("#loginPassword").val(),
            loginPassword_confirm : $("#loginPassword_confirm").val(),
            chooseRole : $("#chooseRole").val()
        };

        $.ajax({
            type: 'POST',
            url: '/login/signup',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            console.log('회원가입에 성공하였습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            console.log('에러발생');
            console.log(error);
        });
    },

//    signin : function () {
//            var data = {
//                loginId : $('#loginId').val(),
//            };
//
//            $.ajax({
//                type: 'POST',
//                url: '/login/signin',
//                contentType:'application/json; charset=utf-8',
//                async: false,
//                dataType:'json',
//                data: JSON.stringify(data)
//            }).done(function() {
//                console.log('로그인에 성공하였습니다.');
//                window.location.href = '/main';
//            }).fail(function (error) {
//                console.log('에러발생');
//                console.log(error);
//            });
//        },
};

index.init();