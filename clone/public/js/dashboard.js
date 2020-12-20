const detail = (obj, id) => {

}
const deactivate = (obj, id) => {
    console.log(obj)
    const $el = $(obj).parent().parent().get(0).children[2].firstElementChild.firstElementChild
    alertify.confirm('Are you sure?', 'Do you want to DEACTIVATE the account?', function () {
        axios.get('http://localhost:3000/users/deactivate/' + id)
            .then(response => {
                $el.classList.remove('fas', 'fa-circle', 'text-c-green', 'f-10' ,'m-r-15');
                $el.classList.add('fas', 'fa-circle', 'text-c-red', 'f-10' ,'m-r-15');
                obj.classList.remove('label', 'theme-bg' ,'text-white' ,'f-12');
                obj.classList.add('label', 'theme-bg2' ,'text-white' ,'f-12');
                obj.textContent = 'Active';
                obj.removeAttribute('onclick');
                obj.setAttribute('onclick','activate(this,'+id+')');
                console.log(obj)
            })
    }, 
        function () { })
        .autoCancel(10).set({ 'defaultFocus': 'cancel' });
}


const activate  = (obj,id) => {
    const $el = $(obj).parent().parent().get(0).children[2].firstElementChild.firstElementChild
    alertify.confirm('Are you sure?', 'Do you want to ACTIVATE the account?', function () {
        axios.get('http://localhost:3000/users/activate/' + id)
            .then(response => {
                $el.classList.remove('fas', 'fa-circle', 'text-c-red', 'f-10' ,'m-r-15');
                $el.classList.add('fas', 'fa-circle', 'text-c-green', 'f-10' ,'m-r-15');
                obj.classList.remove('label', 'theme-bg2' ,'text-white' ,'f-12')
                obj.classList.add('label', 'theme-bg' ,'text-white' ,'f-12')
                obj.textContent = 'Deactivate'
                obj.removeAttribute('onclick')
                obj.setAttribute('onclick','deactivate(this,'+id+')')
                
            })
    },
        function () { })
        .autoCancel(10).set({ 'defaultFocus': 'cancel' });
}

const logout = () => {
    localStorage.clear()
    document.cookie = "token" +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    location.reload()
}
