var index = {
    init : function () {
        var _this = this;
        $('#submit-btn').on('click', function () {
            _this.signup();
        })
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
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        })
        .done(function() {
        })
        .fail(function(){
        });
    },
};

index.init();