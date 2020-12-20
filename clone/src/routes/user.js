const express = require('express')
const authen = require('../middleware/authen')
const router = new express.Router()
const path = require('path')
const axios = require('axios')
const { response } = require('express')

const publicPath = path.join(__dirname, '../../public')

const VALID_MAIL_EXIST = 'http://localhost:3000/users/'
const VALID_MAIL_EXISTOTP = 'http://localhost:3000/users/otp/'
const SEND_MAIL = 'http://localhost:3000/mail'
const VALID_MAIL_OTP = 'http://localhost:3000/mail/'
const GET_SINGLE_USER = 'http://localhost:3000/user/'
const GET_SINGLE_USER_PHONE = 'http://localhost:3000/user_phone/'


const BASE_LINK_USER = 'http://localhost:3000'
const BASE_LINK = 'http://localhost:3001'


router.get('/signup',authen,function (req, res) {
    // console.log(req.cookies['token'])
     if(req.user){
         return res.redirect('/home')
     }
     res.sendFile(publicPath + '/html/signup.html', {})
 })
 
router.post('/email-valid',authen, async (req, res) => {
     if(req.user){
         return res.redirect('/home')
     }
     const email = req.body.email
     
     console.log(email + "troi")
     try {
         const response = await axios.get(VALID_MAIL_EXIST + email)
         console.log(response + "troi")
         if (response.data.valid) {
             await axios.post(SEND_MAIL, {
                 email
             })
         }
         res.send(response.data)
     } catch (error) {
         res.send(error)
     }
 })
 
 router.get('/validate/:email',authen,async (req, res) => {
     if(req.user){
         return res.redirect('/home')
     }
     const mailbox = req.params.email
     if (!mailbox) {
         return res.redirect('/')
     }
     try {
         const response = await axios.get(VALID_MAIL_EXISTOTP + mailbox)
         if (response.data.created) {
             return res.render('validate',{
                 email : mailbox
             })
         } else {
             return res.redirect('/')
         }
     } catch (error) {
         return res.send({error})
     }
 
 })
 
 router.post('/hihi', async (req, res) => {
     console.log(req.body)
     const mail = req.body.email
     const code = req.body.code
     try {
         const response = await axios.get(VALID_MAIL_OTP + mail + '/' + code)
         res.send(response.data)
     } catch (error) {
         res.send(error.response.data)
     }
     
 })
 
 router.get('/info/:email',authen,async (req, res) => {
     if(req.user){
         return res.redirect('/home')
     }
     const email = req.params.email
     try {
         const user = await axios.get(GET_SINGLE_USER + email)
          console.log(user.data)
         if(!user.data){
             return res.redirect('/')
         }
         if(user.data.password){
             res.redirect('/')
         }else{
             res.render('info',{
                 email
             })
         }
     } catch (error) {
         res.send(error)
     }
     
 })


 router.get('/info_phone/:phone',authen,async (req, res) => {
    if(req.user){
        return res.redirect('/home')
    }
    const phone = req.params.phone
    try {
        const user = await axios.get(GET_SINGLE_USER_PHONE + phone)
        console.log(user.data)
        if(!user.data){
            return res.redirect('/')
        }
        if(user.data.password){
            res.redirect('/')
        }else{
            res.render('info_phone',{
                phone
            })
        }
    } catch (error) {
        return res.redirect('/')
    }
    
})
 
 
 //home page
 
 router.get('/home',authen,async(req, res) => {
     if(!req.user || !req.user.user.active){
         res.clearCookie('token')
         return res.redirect('/')
     }
     res.sendFile(publicPath+'/html/home.html') 
 })


  //contact page user
 
  router.get('/contact',authen,async(req, res) => {
    const listUserContact = []
    if(!req.user || !req.user.user.active){
        res.clearCookie('token')
        return res.redirect('/')
    }
    const userId = req.user.userId
    try {
        const listUser = await axios.get(BASE_LINK + `/contact/${userId}`)
        for(let i = 0; i < listUser.data.length;i++ ){
            const curUser = listUser.data[i]
            if(curUser.from_uid == userId){
                const us = await axios.get(BASE_LINK_USER + `/user_row/${curUser.to_uid}`)
                if(us.data){
                    listUserContact.push(us.data)
                }
                
            }else{
                const us = await axios.get(BASE_LINK_USER + `/user_row/${curUser.from_uid}`)
                listUserContact.push(us.data)
            }
        }

        console.log(listUser.data[0])
        res.render('contact',{
            listUserContact
        })
    } catch (error) {
        
    } 
})


 
 router.get('/',authen,async(req, res) => {
     if(req.user){
         return res.redirect('/home')
     }
     res.render('index')
 })


module.exports = router