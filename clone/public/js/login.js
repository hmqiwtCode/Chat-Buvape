const loginEmail = document.querySelector('#login_email')
const loginPhone = document.querySelector('#login_phone')
const selectPick = document.querySelector('#select_pick')
const btn_login = document.querySelector('#login')

const email_format = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/

loginEmail.style.display = 'none'



selectPick.addEventListener('change', e => {
    if (e.target.value === 'Email') {
        loginEmail.style.display = 'block'
        loginPhone.style.display = 'none'
    } else if(e.target.value === 'Phone') {
        loginEmail.style.display = 'none'
        loginPhone.style.display = 'block'
    }else{
        location.href = "/html/qrcode.html"
    }
})

btn_login.addEventListener('click', (e) => {
    e.preventDefault()
    console.log('voday')
    if(selectPick.value === "Email"){
    const email = document.querySelector('#email').value
    const password = document.querySelector('#password').value

    if (email.trim() === '' || password.trim() === '') {
        alertify.alert('Thất bại', 'Email và Password không được để trống').set('resizable', true).resizeTo(200, 200)
        return
    }

    if (!email.match(email_format)) {
        alertify.alert('Thất bại', 'Email không hợp lệ ').set('resizable', true).resizeTo(200, 200)
        return
    }

    axios.post('http://localhost:3000/users/login', {
        email,
        password
    })
        .then(function (response) {
            if (response.status === 200) {
                if(response.data.user.active){
                    addStorage(response.data.user)
                    const token = 'Bearer ' + response.data.token
                    var day = new Date()
                    day.setTime(day.getTime() + (7 * 24 * 60 * 60 * 1000))
                    document.cookie = 'token=' + token + '; expires=' + day.toUTCString() + '; path=/;'
                    location.href = '/home'
                }else{
                    alertify.alert('Thất bại', 'Tài khoản của bạn bị khóa').set('resizable', true).resizeTo(250, 250)
                }
                
            }
            else{
                alertify.alert('Thất bại', 'Tài khoản mật khẩu không chính xác').set('resizable', true).resizeTo(250, 250)
            }
        })
        .catch(function (error) {
            alertify.alert('Thất bại', 'Tài khoản mật khẩu không chính xác').set('resizable', true).resizeTo(250, 250)
            console.log(error);
        });
    }else{
        const phone = document.querySelector('#phone').value
        const password = document.querySelector('#password_phone').value

        axios.post('http://localhost:3000/users/login_phone', {
            phone,
            password
        })
        .then(function (response) {
            if (response.status === 200) {
                if(response.data.user.active){
                    console.log(response.data.user)
                    addStorage(response.data.user)
                    const token = 'Bearer ' + response.data.token
                    var day = new Date()
                    day.setTime(day.getTime() + (7 * 24 * 60 * 60 * 1000))
                    document.cookie = 'token=' + token + '; expires=' + day.toUTCString() + '; path=/;'
                    location.href = '/home'
                }else{
                    alertify.alert('Thất bại', 'Tài khoản của bạn bị khóa').set('resizable', true).resizeTo(250, 250)
                }
                
            }
            else{
                alertify.alert('Thất bại', 'Tài khoản mật khẩu không chính xác').set('resizable', true).resizeTo(250, 250)
            }
        })
        .catch(function (error) {
            alertify.alert('Thất bại', 'Tài khoản mật khẩu không chính xác').set('resizable', true).resizeTo(250, 250)
            console.log(error);
        });
    }

})

const addStorage = (data) => {
    localStorage.setItem("id", data.id)
    localStorage.setItem("email", data.email)
    localStorage.setItem("phone", data.phone)
    localStorage.setItem("firstname", data.firstname)
    localStorage.setItem("lastname", data.lastname)
}



