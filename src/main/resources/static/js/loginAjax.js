window.signupFlow = {
    submit: function () {
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
        }).fail(function (xhr) {
            var errorMessage = xhr.responseText || '알 수 없는 오류가 발생했습니다.';
            alert(errorMessage);
        });
    }
};
