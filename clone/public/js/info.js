const formElement = document.querySelectorAll('.inputp')
const nextElement = document.querySelector('#next')
const email = document.querySelector('#email-vl')

const INSERT_USER = 'http://localhost:3000/users'
nextElement.addEventListener('click',(e) => {
    e.preventDefault()
    for(let i = 0; i< formElement.length;i++){
        if(formElement[i].value.trim() === ''){
            alertify.alert('Có lỗi xảy ra',`Bạn chưa điền trường ${formElement[i].attributes[2].value}`).set('resizable',true).resizeTo(250,250)
            return
        }
    }
    if(formElement[2].value.trim().length < 11 || formElement[2].value.trim().length > 11 ){
        alertify.alert('Có lỗi xảy ra','Phone Number không hợp lệ').set('resizable',true).resizeTo(250,250)
            return
    }

    if(formElement[4].value.trim().length < 7 || formElement[5].value.trim().length > 10 ){
        alertify.alert('Có lỗi xảy ra','Password (7-10) ký tự').set('resizable',true).resizeTo(250,250)
        return
    }
    if(formElement[4].value.trim() !== formElement[5].value.trim()){
        alertify.alert('Có lỗi xảy ra','Password không khớp').set('resizable',true).resizeTo(250,250)
        return
    }else{
        const data = {
            'email' : email.value,
            'firstname' : formElement[0].value.trim(),
            'lastname' : formElement[1].value.trim(),
            'phone' : formElement[2].value.trim(),
            'dob' : formElement[3].value.trim(),
            'password' : formElement[4].value.trim()
        }
        $.ajax({
            contentType: "application/json; charset=utf-8",
            url: INSERT_USER,
            type: 'post',
            data: JSON.stringify(data),
            success: function (data, textStatus, jqXHR) {
                if(data.token){
                    alertify.alert('Thành công','Thêm tài khoản thành công',function(){ location.href='/home' }).set('resizable',true).resizeTo(250,250)
                    localStorage.setItem("token", 'Bearer ' + data.token)
                    const token = 'Bearer ' + data.token
                    var day = new Date()
                    day.setTime(day.getTime() + (7 * 24 * 60 * 60 * 1000))
                    document.cookie = 'token='+token+'; expires='+day.toUTCString()+'; path=/;'
                    addStorage(data.user)
                }
                else{
                    alertify.alert('Thất bại',`${data.error}`).set('resizable',true).resizeTo(250,250)
                }
                console.log(data);
                
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR)
                alertify.alert('Thất bại',`${jqXHR.responseJSON.error}  mã 84`).set('resizable',true).resizeTo(250,250)
                console.log("Có lỗi xảy ra thử lại");
            }


        })
    }

})

const addStorage = (data) => {
    localStorage.setItem("id", data.id)
    localStorage.setItem("email", data.email)
    localStorage.setItem("phone", data.phone)
    localStorage.setItem("firstname", data.firstname)
    localStorage.setItem("lastname", data.lastname)
}