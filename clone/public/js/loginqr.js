const socket = io()
const imgQR = document.querySelector('#qrcode')
const info_scan = document.querySelector('#info-scan')
const qr_code_scan = document.querySelector('#qr_code_scan')
const gt = document.querySelector('#gt')
socket.emit('getIMG', "testing")

socket.on("IMGQR",(data) =>{
    imgQR.src = data
})

socket.on("getReload",(data) =>{
    console.log(data)
    //location.href = "http://google.com"
    qr_code_scan.style.display = "none"
    info_scan.children[0].src = `https://avatar.oxro.io/avatar.svg?name=${data[1]}&caps=1`
    info_scan.children[0].classList.add("img-radius");
    info_scan.children[1].textContent = data[1]
    info_scan.children[2].textContent = "Quét mã thành công. Vui lòng chọn \"Đăng nhập\" trên thiết bị di động của bạn."
})

socket.on('save_token',(data) =>{
    console.log(data)
    const token = 'Bearer ' + data.token
    const day = new Date()
    day.setTime(day.getTime() + (7 * 24 * 60 * 60 * 1000))
    document.cookie = 'token=' + token + '; expires=' + day.toUTCString() + '; path=/;'
    location.href = '/home'

    addStorage(data.data)
})

socket.on('reload_browser',(data) => {
    location.href = '/'
})

const addStorage = (data) => {
    localStorage.setItem("id", data.id)
    localStorage.setItem("email", data.email)
    localStorage.setItem("phone", data.phone)
    localStorage.setItem("firstname", data.firstname)
    localStorage.setItem("lastname", data.lastname)
}

