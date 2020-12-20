const clearBtn = document.querySelector('#clear')
const addBtn = document.querySelector('#add')

const deactivate = (obj, id) => {
    console.log(obj)
    const $el = $(obj).parent().parent().get(0).children[8].firstElementChild
    alertify.confirm('Are you sure?', 'Do you want to DEACTIVATE the account?', function () {
        axios.get('http://localhost:3000/users/deactivate/' + id)
            .then(response => {
                obj.textContent = '+ Active';
                obj.removeAttribute('onclick');
                obj.setAttribute('onclick','activate(this,'+id+')');
                obj.setAttribute('style','background-color: #00d1b2;color: #fff;');
                $el.setAttribute('style','background-color: #f14668;color: #fff;');
                $el.textContent = 'False';
            })
    },
        function () { })
        .autoCancel(10).set({ 'defaultFocus': 'cancel' });
}


const activate  = (obj,id) => {
    const $el = $(obj).parent().parent().get(0).children[8].firstElementChild
    alertify.confirm('Are you sure?', 'Do you want to ACTIVATE the account?', function () {
        axios.get('http://localhost:3000/users/activate/' + id)
            .then(response => {
                obj.textContent = '- Deactivate'
                obj.removeAttribute('onclick')
                obj.setAttribute('onclick','deactivate(this,'+id+')')
                obj.setAttribute('style','background-color: #ffdd57;color: #fff;');
                $el.setAttribute('style','background-color: #00d1b2;color: #fff;');
                $el.textContent = 'True';
                
            })
    },
        function () { })
        .autoCancel(10).set({ 'defaultFocus': 'cancel' });
}


const deleteUser = (obj,id) => {
    const $el = $(obj).parent().parent().get(0)
    console.log($el)
    alertify.confirm('Are you sure?', 'Do you want to DELETE the account?', function () {
        axios.delete('http://localhost:3000/users/'+ id)
            .then(response => {
                alertify.alert('Thành công', 'Xóa thành công').set('resizable', true).resizeTo(250, 250)
                $el.style.display = 'none'

                
            })
    },
        function () { })
        .autoCancel(10).set({ 'defaultFocus': 'cancel' });
    
}

const form = $('#detailForm')
form[0].style.display = 'none'
const detail = (obj,id) => {
    alertify.genericDialog || alertify.dialog('genericDialog',function(){
        return {
            main:function(content){
                console.log(content)
                this.setContent(content);
            },
            setup:function(){
                return {
                    focus:{
                        element:function(){
                            return this.elements.body.querySelector(this.get('selector'));
                        },
                        select:true
                    },
                    options:{
                        basic:true,
                        maximizable:false,
                        resizable:false,
                        padding:true,
                        center:true
                    }
                };
            },
            settings:{
                selector:undefined
            }
        };
    });
    //force focusing password box
    form[0].style.display = 'block'
    alertify.genericDialog ($('#detailForm')[0]);
}

clearBtn.addEventListener('click',(e) => {
    document.getElementById('reset_form').reset();
})


addBtn.addEventListener('click', (e) => {
    const email = $('#reset_form')[0][0].value
    const password = $('#reset_form')[0][1].value
    const firstname = $('#reset_form')[0][2].value
    const lastname = $('#reset_form')[0][3].value
    const phone = $('#reset_form')[0][4].value
    const dob = $('#reset_form')[0][5].value
    let type_account = $('#reset_form')[0][6].value
    if(type_account == "Admin"){
        type_account = 1
    }else{
        type_account = 0
    }

    const mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/

    if (!email.match(mailformat)) {
        alertify.alert('Error', 'Email không hợp lệ').set('resizable', true).resizeTo(250, 250)
        return;
    }

    if (phone.trim().length < 11 || phone.trim().length > 11) {
        alertify.alert('Error', 'Phone không hợp lệ').set('resizable', true).resizeTo(250, 250)
        return;
    }

    axios.post('http://localhost:3000/add/users',{
        email,
        password,
        firstname,
        lastname,
        phone,
        dob,
        type_account
    })
    .then(response => {
       alertify.alert('Thành công', 'Thêm tài khoản thành công').set('resizable', true).resizeTo(250, 250)
       document.getElementById('reset_form').reset();
    }).catch(err => {
        console.log(err)
    })

})