const module_three = "http://localhost:3002/"
const socket = io(module_three)

const chat_content = document.getElementById('chat_content')
const chat_infor = document.getElementById('chat_infor')
const friend_item_bar = document.getElementById('friend_item_bar')
const chat_item_bar = document.getElementById('chat_item_bar')
const friend_search = document.getElementById('friend_search')
const gioithieu = document.getElementById('gioithieu')
const entersend = document.getElementById('sendMessageEnter')
const btnEmoji = document.querySelector('#emoji-button')

const module_two = 'http://localhost:3001/'
const module_one = 'http://localhost:3000/'

const testrd = document.getElementById('headermbh').innerHTML


const cookie = key => ((new RegExp((key || '=') + '=(.*?); ', 'gm')).exec(document.cookie + '; ') || ['', null])[1]


chat_content.style.display = "none"
chat_infor.style.display = "none"
friend_item_bar.style.display = "none"

friend_search.addEventListener('input', (e) => {
    // chat_item_bar.style.display = 'none'
    // friend_item_bar.style.display = "block"
    if (friend_search.value.trim().length > 0) {
        axios.get(module_two + "friends/" + jwt_decode(cookie('token').replace('Bearer ', ''))._id + `/${friend_search.value}`).then(data => {
            const handlebar = Handlebars.compile(testrd);
            const datadd = handlebar({ objects: data.data })
            const html = document.getElementById('friends_search_fine').innerHTML = datadd
            friend_item_bar.style.display = "block"
            console.log(data.data.length)
            if (data.data.length > 0) {
                chat_item_bar.style.display = 'none'
            }
        })
    } else {
        chat_item_bar.style.display = 'block'
        friend_item_bar.style.display = "none"
    }
})

const getFriendGroup = async () =>{
    const listUserContact = []
    const listUser = await axios.get(module_two + `/contact/${localStorage.getItem('id')}`)
        for(let i = 0; i < listUser.data.length;i++ ){
            const curUser = listUser.data[i]
            if(curUser.from_uid == localStorage.getItem('id')){
                const us = await axios.get(module_one + `/user_row/${curUser.to_uid}`)
                if(us.data){
                    listUserContact.push(us.data)
                }
                
            }else{
                const us = await axios.get(module_one + `/user_row/${curUser.from_uid}`)
                listUserContact.push(us.data)
            }
        }
    const handlebar = Handlebars.compile(document.getElementById('friend_group').innerHTML);
    const datadd = handlebar({ objects:  listUserContact})
    const fr = document.getElementById('list_friend_add')
    fr.innerHTML = datadd
    
}


getFriendGroup()


const getName  = () => {
    const tennhom = document.getElementById('tennhom')
    if(tennhom.value.trim() === ''){
        alert('Nhập tên nhóm')
        tennhom.focus()
        return;
    }
    const checkboxes = document.getElementsByName("friend"); 
    const arrFriend = [];
    for(let i = 0; i < checkboxes.length;i++){
        if(checkboxes[i].checked){
            arrFriend.push({id:checkboxes[i].id,name:checkboxes[i].value})
        }
    }

    if(arrFriend.length <= 0){
        alert('Chọn bạn bè')
        return;
    }

    socket.emit('createGroup', {
        sender: jwt_decode(cookie('token').replace('Bearer ', ''))._id,
        receiver : arrFriend,
        name : tennhom.value.trim(),
        time : moment(new Date()).format('hh:mm')
    })


    modal.style.display = "none";

    console.log(arrFriend)
}


    const picker=new EmojiButton({
        position: 'top-end'
    });

    picker.on('emoji', emoji => {
        textMessage.value += emoji;
        textMessage.focus()
    });

  btnEmoji.addEventListener('click', () => {
    picker.pickerVisible ? picker.hidePicker() : picker.showPicker(btnEmoji);
  });



const scrollNicer = () => {
    const $messages = document.getElementById('content_user_chat')
    const newMessageHeight = $messages.lastElementChild.offsetHeight + parseInt(getComputedStyle($messages.lastElementChild).marginBottom) + 100
    console.log(typeof newMessageHeight)
    const toltalMessagesHeight = $messages.scrollHeight
    const heightCurrentVisiblity = $messages.offsetHeight // always
    const heightOfTop = heightCurrentVisiblity + $messages.scrollTop

    if (toltalMessagesHeight - newMessageHeight <= heightOfTop) {
        $messages.scrollTop = toltalMessagesHeight
    }
}



const chatMessage = (el, id, name) => {
    $(el).siblings().removeClass("active_current_friend");
    $(el).addClass("active_current_friend");
    axios.get(module_three + `message/${jwt_decode(cookie('token').replace('Bearer ', ''))._id}?id=${id}`).then(data => {
        let handlebar = Handlebars.compile(document.getElementById('chatchatco_retrieve').innerHTML);
        //  const chat = {...data.data[0].messages,current : localStorage.getItem("id")}
        console.log(data.data)
        let datadd;
        if (data.data) {
            data.data.messages.forEach(element => {
                element.current = localStorage.getItem("id")
                console.log(element)
            });
            datadd = handlebar({ objects: data.data.messages })
        } else {
            datadd = handlebar({ objects: null })
        }

        let user_chat = document.getElementById('content_user_chat')
        let html = user_chat.innerHTML = datadd



        handlebar = Handlebars.compile(document.getElementById('name_dialog_header').innerHTML);
        datadd = handlebar({ name: el.getAttribute('name') })
        html = document.getElementById('dialog_header_name').innerHTML = datadd

        gioithieu.style.display = "none"
        chat_content.style.width = 'calc(100% - 1px)'
        chat_content.style.display = "block"
        user_chat.scrollTop = user_chat.scrollHeight;

        chat_infor.style.display = "none"
        localStorage.setItem("id_click", id)


    }).catch(error => {
        console.log(error)
    })
}


const textMessage = document.getElementById("textMessage");
textMessage.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        event.preventDefault();
        entersend.click();
    }
})


const getAllItemMessage = () => {
    console.log("not works")
    axios.get(module_three + `user/${localStorage.getItem("id")}`).then(async data => {
        console.log(data)
        let handlebar = Handlebars.compile(document.getElementById('item_chat_now').innerHTML);

        let datadd;
        if (data.data) {
            //module_one + `us/${element.receiver}`
            const hihihi = await Promise.all(data.data.map(async (element) => {
                    console.log("Day la element", element)
                    const receiverID = await axios.get(module_one + `us/${element.receiver}`)
                    const mssLength = element.messages.length - 1
                    element.messages = element.messages[mssLength]
                    element.messages.current = localStorage.getItem("id")
                    if(element.type === undefined){
                        if(element.sender === localStorage.getItem("id"))
                            element.messages.receiverName = receiverID.data.firstname + " " + receiverID.data.lastname
                        else{
                            const receiverID = await axios.get(module_one + `us/${element.sender}`)
                            element.messages.receiverName = receiverID.data.firstname + " " + receiverID.data.lastname
                        }
                            
                    }else{
                        element.messages.receiverName = element.name
                    }   
            }))

            console.log(data.data)
            data.data.sort((a, b) => b.un_read - a.un_read);
            datadd = handlebar({ objects: data.data })
        } else {
            datadd = handlebar({ objects: null })
        }

        let user_chat = document.getElementById('chat_item_bar')
        let html = user_chat.innerHTML = datadd
    }).catch(e => {
        console.log(e)
    })
}


getAllItemMessage()


entersend.addEventListener('click', (e) => {

    if (textMessage.value.trim().length > 0) {
        
        // display ui
        const time = moment(new Date()).format('hh:mm')
        const avatar = `https://avatar.oxro.io/avatar.svg?name=${localStorage.getItem("lastname")}&caps=1`
        const user = localStorage.getItem("lastname")
        const handlebar = Handlebars.compile(document.getElementById('chatchatco_realtime').innerHTML);
        const datadd = handlebar({ date: moment(new Date()).format('MMM DD'), time: time, avatar: avatar, user: user, message: textMessage.value })
        document.getElementById('content_user_chat').innerHTML += datadd

        // check type of chat (1-1 or group)
        if(!localStorage.getItem("id_click").includes('object')){ 
            socket.emit('addRoom', {
                sender: jwt_decode(cookie('token').replace('Bearer ', ''))._id,
                id: localStorage.getItem("id_click"),
                idMessage : localStorage.getItem('id_message'),
                message: textMessage.value,
                time,
                avatar,
                user
            })
        }else{
            socket.emit('addGroup', {
                sender: jwt_decode(cookie('token').replace('Bearer ', ''))._id,
                idMessage : localStorage.getItem('id_message'),
                message: textMessage.value,
                user,
                time
            })
        }
        textMessage.value = ""
        scrollNicer()
    }
    else{
        alert("Nhập nội dung")
    }
})



socket.emit('addJoin', jwt_decode(cookie('token').replace('Bearer ', ''))._id)

socket.on('message', data => {
    if(data.idMessage === localStorage.getItem("id_message")){
        const handlebar = Handlebars.compile(document.getElementById('chatchatco_realtime_rv').innerHTML);
        const datadd = handlebar({ time: data.time, avatar: data.avatar, user: data.user, message: data.message })
        const html = document.getElementById('content_user_chat').innerHTML += datadd
        scrollNicer()
    }
    

})

// setInterval(() =>{
//     getAllItemMessage()
// },2000)


const displayChat = (el, idMessage) => {
    localStorage.setItem("id_click", el.lastElementChild.textContent)
    localStorage.setItem("id_message", idMessage)
    updateUnread(idMessage)
    // console.log(el.lastElementChild.textContent)
    let handlebar = Handlebars.compile(document.getElementById('chatchatco_retrieve').innerHTML);
    axios.get(module_three + `messages/${idMessage}`).then(data => {
        let datadd;
        if (data.data) {
            data.data.nameOnline = localStorage.getItem("lastname")
            data.data.messages.forEach(element => {
                element.current = localStorage.getItem("id")
                element.nameS = localStorage.getItem("lastname")
                element.nameR = el.firstElementChild.getAttribute("alt")
                console.log(element)
            });
            datadd = handlebar({ objects: data.data.messages })

            
        } else {
            datadd = handlebar({ objects: null })
        }

        let user_chat = document.getElementById('content_user_chat')
        let html = user_chat.innerHTML = datadd

        gioithieu.style.display = "none"
        chat_content.style.width = 'calc(100% - 391px)'
        chat_content.style.display = "block"
        if (data.data.type === undefined) {
            chat_infor.style.display = "block"
            handlebar = Handlebars.compile(document.getElementById('chat_info_type_two').innerHTML);
            datadd = handlebar({ objects: data.data.nameOnline,name: el.firstElementChild.getAttribute("alt") })
            chat_infor.innerHTML = datadd
        }else{
            chat_infor.style.display = "block"
            handlebar = Handlebars.compile(document.getElementById('chat_info_type_group').innerHTML);
            datadd = handlebar({ objects: data.data.receiver,member : data.data.receiver.length+1,name: data.data.name })
            chat_infor.innerHTML = datadd
        }

        user_chat.scrollTop = user_chat.scrollHeight;
    })
}

const updateUnread = (id) => {
    axios.patch(module_three + `message/unread/${id}`)
} 

const readURL = (input) => {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('.profile-pic').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}

$(".file-upload").on('change', function(){
    readURL(this);
});

$("#btnFile").on('click',function(){
    $(".file-upload").click()
})

$("#name_user").val(localStorage.getItem('firstname') + " " + localStorage.getItem('lastname'))

$('#logout').on('click', () => {
    localStorage.clear()
    document.cookie = "token" +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    location.reload()
    
    

})


// add group
const modal = document.getElementById("myModal");
const btn = document.getElementById("create_group");
const span = document.getElementsByClassName("close")[0];
// add friend
const modal_friend = document.getElementById("modal_friend");
const btnAddFriend = document.getElementById("add_new_friend");
const span_friend = document.getElementsByClassName("close")[1];

// modal user
const btnUser = document.getElementById("btnUser");
const modal_user = document.getElementById("modal_user");
const span_user = document.getElementsByClassName("close")[2];

btn.onclick = function() {
  modal.style.display = "block";
}

btnAddFriend.onclick = function() {
    modal_friend.style.display = "block";
  }

span.onclick = function() {
  modal.style.display = "none";
}
span_friend.onclick = function() {
    modal_friend.style.display = "none";
}

window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

// modal user
btnUser.onclick = function() {
    modal_user.style.display = "block";
}

span_user.onclick = function() {
    modal_user.style.display = "none";
}

const addFriend = async () => {
    const inpSdt = document.getElementById("sdt_friend")
    if(inpSdt.value === ""){
        alert("Nhập số điện thoại")
        return
    }
    if(inpSdt.value.length < 10 || inpSdt.value.length > 11){
        alert("Số điện thoại không hợp lệ")
        return
    }

    axios.get(module_one + `user_phone/${inpSdt.value}`).then(data => {
        if(data.data){
            axios.post(module_two + `contact`,{
                from : localStorage.getItem("id"),
                to : data.data.user.id
            }).then(data => {
                alert("Thêm thành công")
                modal_friend.style.display = "none";
            }).catch(e => {
                alert("Xảy ra lỗi")
            })
        }
    }).catch(e => {
        alert("Không tìm thấy")
    })
    
}






Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {
    switch (operator) {
        case '==':
            return (v1 == v2) ? options.fn(this) : options.inverse(this);
        case '===':
            return (v1 === v2) ? options.fn(this) : options.inverse(this);
        case '!=':
            return (v1 != v2) ? options.fn(this) : options.inverse(this);
        case '!==':
            return (v1 !== v2) ? options.fn(this) : options.inverse(this);
        case '<':
            return (v1 < v2) ? options.fn(this) : options.inverse(this);
        case '<=':
            return (v1 <= v2) ? options.fn(this) : options.inverse(this);
        case '>':
            return (v1 > v2) ? options.fn(this) : options.inverse(this);
        case '>=':
            return (v1 >= v2) ? options.fn(this) : options.inverse(this);
        case '&&':
            return (v1 && v2) ? options.fn(this) : options.inverse(this);
        case '||':
            return (v1 || v2) ? options.fn(this) : options.inverse(this);
        default:
            return options.inverse(this);
    }
})


