const db = require("../models/connect")
const { sendEmailValidCode } = require('../email/valid')
var moment = require('moment')
const jwt = require('jsonwebtoken')
const axios = require('axios')
const e = require("express")
const User = db.User
const Token = db.Token
const Op = db.Sequelize.Op
const Sequelize = db.Sequelize


const BASE_LINK = "http://module2-bv.us-east-2.elasticbeanstalk.com"  // module 2


exports.create = (async (req, resp) => {
    const email = req.body.email
    const otp = Math.floor(100000 + Math.random() * 900000)
    const user = new User({ email, otp })
    try {
       await sendEmailValidCode(email,otp)
        await user.save()
        resp.status(200).send(user)
    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})


// Signup phone
// post /phone -----
exports.phone = (async (req, resp) => {
    const trickMailCode = "support_" + Math.floor(100000 + Math.random() * 900000) + "_" + Math.floor(100000 + Math.random() * 900000) + "@bupvapechat.com"
    const phone = req.body.phone
    const email = trickMailCode
    console.log(email)
    const userFind = await User.findOne({ where: { phone } })
    if(userFind != null){
        return resp.status(500).send({ 'error': 'User exists' })
    }
    const user = new User({email,phone})
    try {
        await user.save()
        resp.status(200).send(user)
    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})


// Validate code email
// ----- /mail/:gm/:code -----
exports.validcode = (async (req, resp) => {
    const mailbox = req.params.gm
    const code = req.params.code
    try {
        const user = await User.findOne({ where: { 'email': mailbox } })
        // console.log(user._id.getTimestamp().getTime())
        if (user.otp === code) {
            if (parseInt(user.wrong) < 0) {
                return resp.status(500).send({ 'error': 'Tài khoản này đã bị khóa trước đây' })
            }
            user.otp = 'undefined'
            user.wrong = 'undefined'
            await user.save()
            return resp.status(200).send({ 'success': 'Hợp lệ' })
        } else {
            user.wrong = parseInt(user.wrong) - 1
            await user.save()

            if (parseInt(user.wrong) < 0) {
                return resp.status(500).send({ 'error': 'Tài khoản bị khóa' })
            }
            resp.status(500).send({ 'error': 'Không hợp lệ' })
        }

    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})


// Validate phone
// -----get  /phone/:num -----
exports.phoneValid = (async (req, resp) => {
    const number = req.params.num
    try {
        const user = await User.findOne({ where: { 'phone': number } })
            user.otp = 'undefined'
            user.wrong = 'undefined'
            await user.save()
            return resp.status(200).send({ 'success': 'Hợp lệ' })
    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})

// find user by phone number;
// get /user_phone/:phone
exports.findUserByPhoneNumber = (async (req, resp) => {
    const number = req.params.phone
    try {
        const user = await User.findOne({ where: { 'phone': number } })
        if(user){
            return resp.status(200).send({user}) 
        }
        else{
            return resp.status(404).send({"result":"not found"})
        }
    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})






// find user_custom by phone number;
// get /user_custom/:phone
exports.findListUserByPhoneNumber = (async (req, resp) => {
    let userCallAPI = ''
    let userID = ''
    const number = req.params.phone
    const listPhone = number.split(";");
    const listUserContact = [];
    console.log(listPhone);
    
    for(let i = 0; i<listPhone.length;i++){
        
        const current = listPhone[i].split("+_+")
        let curPhone = current[1].replace(/[^0-9]/g, "")

        

        if(curPhone.charAt(0) === '0'){
            curPhone = curPhone.split('');
            curPhone[0] = '84';
            curPhone = curPhone.join('');
        }
        console.log(curPhone)
        try {
            const user = await User.findOne({ where: { 'phone': curPhone,'active' : true } })
            if(i == 0){
                userCallAPI = curPhone
                userID = user.id
                continue
            }

            if(user && user.phone === userCallAPI){
                continue
            }
            

            if(user){
                const contact_from_to = await axios.get(BASE_LINK + `/contact/${userID}/${user.id}`) 
                const contact = {id : user.id,name : user.firstname + ' ' +  user.lastname,phone: user.phone,n_phone : current[0]};
                console.log(contact_from_to)
                if(contact_from_to.data){
                    contact.friend = true
                }else{
                    contact.friend = false
                }
                listUserContact.push(contact)         
            }
            else{
               // return resp.status(404).send({"result":"not found"})
            }
        } catch (error) {
            console.log(error.message)
            return resp.status(500).send({ 'error': error.message })
        }
    }
    return resp.status(200).send(listUserContact)
    
})





// add field then valid code email
// --------- POST METHOD /users --------------
exports.addmoreinfo = (async (req, resp) => {
    const updates = Object.keys(req.body)
    const mailbox = req.body.email
    const user = await User.findOne({ where: { 'email': mailbox } })

    const allowUpdates = ['phone', 'firstname', 'lastname', 'dob', 'password', 'email']
    const isValidOperation = updates.every((update) => {
        return allowUpdates.includes(update)
    })

    if (user.otp !== 'undefined') {
        return resp.status(400).send({ 'error': 'Tài khoản hiện đang bị khóa' })
    }
    if (!isValidOperation) {
        return resp.status(500).send({ 'error': 'Field not allow' })
    }

    try {
        updates.forEach((update) => {
            user[update] = req.body[update]
        })
        const token = await user.generateJsonWebToken(Token)
        await user.save()

        resp.status(200).send({ user, token })
    } catch (e) {
        console.log(e)
        resp.status(400).send({ 'error': e.message })
    }
})


exports.addNewUser = (async (req,resp) => {
    try {
        const user = await User.create({...req.body,otp: 'undefined',wrong:'undefined'})
        resp.status(200).send(user)
    } catch (error) {
        resp.status(500).send({'error' : error.message})
    }
    
})



// add field then valid code phone
// --------- POST METHOD /users_phone --------------
exports.addmoreinfo_phone = (async (req, resp) => {
    const updates = Object.keys(req.body)
    const phone = req.body.phone
    const user = await User.findOne({ where: { phone } })

    const allowUpdates = ['phone', 'firstname', 'lastname', 'dob', 'password', 'email']
    const isValidOperation = updates.every((update) => {
        return allowUpdates.includes(update)
    })

    if (user.otp !== 'undefined') {
        return resp.status(400).send({ 'error': 'Tài khoản hiện đang bị khóa' })
    }
    if (!isValidOperation) {
        return resp.status(500).send({ 'error': 'Field not allow' })
    }

    try {
        updates.forEach((update) => {
            user[update] = req.body[update]
        })
        const token = await user.generateJsonWebToken(Token)
        await user.save()

        resp.status(200).send({ user, token })
    } catch (e) {
        console.log(e)
        resp.status(400).send({ 'error': e.message })
    }
})


// Add user from app
// Post
exports.addNewUserApp = (async (req,resp) => {
    try {
        const user = await User.create({...req.body,otp: 'undefined',wrong:'undefined'})
        const token = await user.generateJsonWebToken(Token)
        resp.status(200).send({user,token})
    } catch (error) {
        resp.status(500).send({'error' : error.message})
    }
    
})



// router.post('/users/login')
// Login user
exports.login = (async (req, resp) => {
    try {
        const user = await User.findByCredentials(req.body.email, req.body.password, User, Token)
        const token = await user.generateJsonWebToken(Token)
        resp.send({ user, token })
    } catch (error) {
        resp.status(400).send({ 'error': error.message })
    }

})


// router.post('/users/login_phone')
// Login user phone
exports.login_phone = (async (req, resp) => {
    try {
        const user = await User.findByCredentialsWithPhone(req.body.phone, req.body.password, User, Token)
        const token = await user.generateJsonWebToken(Token)
        resp.send({user,token})
    } catch (error) {
        resp.status(400).send({ 'error': error.message })
    }

})

//router.get('/users/:mail')
// valid email exists
exports.checkvalid = (async (req, resp) => {
    const mailbox = req.params.mail
    const user = await User.findOne({ where: { 'email': mailbox } })

    if (!user) {
        resp.send({ 'valid': true })
    } else {
        resp.send({ 'valid': false })
    }
})


// check have otp
// get('/users/otp/:mail')
exports.haveotp = (async (req, resp) => {
    const mailbox = req.params.mail
    const user = await User.findOne({ where: { 'email': mailbox } })
    if (!user) {
        return resp.send({ 'created': false })
    }
    if (user.otp !== 'undefined') {
        resp.send({ 'created': true })
    } else {
        resp.send({ 'created': false })
    }
})

//router.get('/user/:mail')

// Get user by email
exports.useremail = (async (req, resp) => {
    const mailbox = req.params.mail
    const user = await User.findOne({ where: { 'email': mailbox } })
    if (user) {
        resp.send(user)
    } else {
        resp.send()
    }
})


//router.get('/user_row/:id')

// Get user by row id
exports.userrow = (async (req, resp) => {
    const row_id = req.params.id
    const user = await User.findOne({ where: { 'id': row_id } })
    if (user) {
        resp.send(user)
    } else {
        resp.send()
    }
})




// router.get('/user/:id/:token')
// for middle ware from client to get User when go to site page
exports.validtokenlogin = (async (req, resp) => {
    console.log('voday')
    const id = req.params.id
    const token = req.params.token
    const user = await Token.findOne({ where: { 'token': token }, include: [{ model: User, where: { 'id': id } }] })
    if (user) {
        resp.send(user)
    } else {
        resp.status(404).send()
    }
})


// router.get('app/user/:token')
// for middle ware from client to get User when go to site page
// code stupid
exports.validtokenlogin_app = (async (req, resp) => {
    try {
        const token = req.params.token
        const decoded = jwt.verify(token,'Ilearnnodejs')
        const user = await Token.findOne({ where: { 'token': token }, include: [{ model: User, where: { 'id': decoded._id } }] })
        if (user && user.user.active) {
            resp.send(user)
        } else {
            resp.status(404).send()
        }
    } catch (error) {
        resp.status(500).send()
    }
    
})

// Get all user 
// Get ('/users)

exports.getCountAllUser = (async (req, resp) => {
    const users = await User.findAll({})
    if (users) {
        resp.send(users)
    } else {
        resp.status(200).send({ 'list': [] })
    }
})

// Get all user register today
// Get ('/users/today)

exports.getUserRegisToday = (async (req, resp) => {
    const date = moment(new Date().getTime()).format('YYYY-MM-DD')
    const users = await User.findAll({
        where: {
            createdAt: {
                [Op.gte]: date
            }
        }
    })

    if (users) {
        resp.send(users)
    } else {
        resp.status(200).send({ 'list': [] })
    }
})


// get Top 5 account register
// get ('users/top/5')

exports.getTopFive = (async (req, resp) => {
    try {
        const users = await User.findAll({
            order: [[Sequelize.col('createdAt'), 'DESC']],
            limit: 5
        })

        if (users) {
            resp.send(users)
        } else {
            resp.status(200).send({ 'list': [] })
        }
    } catch (error) {
        resp.status(500).send({'error' : error.message})
    }
})


// Deactivate Account by id
// get ('users/deactivate/:id')

exports.deactivate = (async (req, resp) => {
    const id = req.params.id
    try {
        const users = await User.findOne({
            where : {
                id
            }
        })

        users.active = false
        await users.save()
        resp.send({success : true})
    } catch (error) {
        resp.status(500).send({'error' : error.message})
    }
})

// Activate Account by id
// get ('users/activate/:id')

exports.activate = (async (req, resp) => {
    const id = req.params.id
    try {
        const users = await User.findOne({
            where : {
                id
            }
        })

        users.active = true
        await users.save()
        resp.send({success : true})
    } catch (error) {
        resp.status(500).send({'error' : error.message})
    }
})

// Get user per page
// get ('users/page/:page')

exports.userperpage = (async (req, resp) => {
    const page = req.params.page
    try {
        const users = await User.findAll({
            limit: 5,
            offset: 5*(page - 1)
        })
        resp.send({users})
    } catch (error) {
        resp.status(500).send({'error' : error.message})
    }
})


// Delete user by id
// delete ('/users')

exports.deleteUser = (async (req, resp) => {
    const id = req.params.id
    try {
        const user = await User.destroy({
            where: {
                id
            }
        })
        resp.send({user})
    } catch (error) {
        resp.send({'error' : 'can\'t delete'})
    }
})


// Find user by id
// get ('/us/:id')

exports.findUserID = (async (req, resp) => {
    const id = req.params.id
    try {
        const user = await User.findOne({
            where: {
                id
            }
        })
        resp.send(user)
    } catch (error) {
        resp.send(error)
    }
})







//detail 131