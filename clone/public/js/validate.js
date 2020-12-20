const numberCoder = document.querySelector('#code_number')
const email = document.querySelector('#email')


numberCoder.addEventListener('keyup', (e) => {
    if (numberCoder.value.length === 6) {
        const data = {'email': email.value, 'code': numberCoder.value}
        alertify.alert('Please wait...', 'Wating...').set('resizable',true).resizeTo(100,100); 
        $.ajax({
            contentType: "application/json; charset=utf-8",
            url: '/hihi',
            type: 'post',
            data: JSON.stringify(data),
            success: function (data, textStatus, jqXHR) {
                if(data.success){
                    alertify.alert('Thành công','Xác nhận tài khoản thành công',function(){ location.href=`/info/${email.value}` }).set('resizable',true).resizeTo(250,250)
                }

                else{
                    alertify.alert('Thất bại',`${data.error}`).set('resizable',true).resizeTo(250,250)
                }
                console.log(data);
                
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Có lỗi xảy ra thử lại");
            }


        })
    }
})