window.onload = function() {
  window.recaptchaVerifier = new firebase.auth.RecaptchaVerifier('recaptcha-container',{
    'size': 'invisible'
  });
}

const nextStep = document.querySelector('#next')
const selectPick = document.querySelector('#select_pick')
const signupMail = document.querySelector('#mail')
//const signupPhone = document.querySelector('#phonez')
//document.cookie = "username=John Doe";

const signupPhone = document.querySelector('#regis_p')
const signupPhoneNumber = document.querySelector('#phonez')


signupPhone.style.display = 'none'

selectPick.addEventListener('change', e => {
  if (e.target.value === 'Email') {
    signupMail.style.display = 'block'
    signupPhone.style.display = 'none'
  } else {
    signupMail.style.display = 'none'
    signupPhone.style.display = 'block'
  }
})

nextStep.addEventListener('click', (e) => {
  e.preventDefault()
  const mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/
  const mail = document.querySelector('#mail')
  if (selectPick.value === 'Email') {
    if (mail.value.match(mailformat)) {
      async function getUser() {
        try {
          const response = await axios.post('/email-valid', {
            'email': mail.value
          })
          console.log(response)
          if (response.data.valid) {
            location.href = `/validate/${mail.value}`
          } else {
            alertify.alert('Error', '<img style="width:20px;height:20px" src="assets/error.png" > Email này đã được sử dụng')
          }
        } catch (error) {
          console.error(error);
        }
      }
      getUser()
    } else {
      alertify.alert('Error', '<img style="width:20px;height:20px" src="assets/error.png" > Email không hợp lệ')
      return false
    }
  } else {
    if(signupPhoneNumber.value.length < 9 || signupPhoneNumber.value.length > 9){
      alertify.alert('Error', 'Số điện thoại không hợp lệ')
      return;
    }


    alertify.alert('Please wait...','Kiểm tra tin nhắn')
    const number = "+84" + signupPhoneNumber.value
    const phoneAdd = "84" + signupPhoneNumber.value
    firebase.auth().signInWithPhoneNumber(number, window.recaptchaVerifier).then(function (confirmationResult) {
      //s is in lowercase
      window.confirmationResult = confirmationResult;
      coderesult = confirmationResult;
      console.log(coderesult);
      axios.post('http://localhost:3000/phone', {
        'phone': phoneAdd
      }).then(res => {
        alertify.prompt('Nhập OTP', 'OTP', ''
               , function(evt, value) { 
                    const code= value;
                    coderesult.confirm(code).then(function (result) {
                      axios.get('http://localhost:3000/phone/' +phoneAdd )
                      .then(res => {
                        alertify.alert('Success','Xác nhận OTP thành công!',function(){ location.href ="/info_phone/" + phoneAdd })
                      })
                        var user=result.user;
                        console.log(user);
                    }).catch(function (error) {
                      alertify.alert('Error','OTP không chính xác')
                    });
            
                }
               , function() {  });
      })
    }).catch(function (error) {
      alertify.alert('Error','Số điện thoại đã sử dụng')
    });

  }

})