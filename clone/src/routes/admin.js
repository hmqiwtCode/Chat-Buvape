const express = require('express')
const authen = require('../middleware/authen')
const axios = require('axios')
const router = new express.Router()

const GET_ALL_USER = 'http://localhost:3000/users'
const GET_USER_REGIS_TODAY = 'http://localhost:3000/users/find/today'
const GET_USER_TOP_FIVE = 'http://localhost:3000/users/top/5'
const GET_USER_PER_PAGE = 'http://localhost:3000/users/page/' ///users/page/:page

router.get('/admin', authen, async (req, res) => {
    if (req.user) {
        if (req.user.user.type_account === 0 || req.user.user.active === 0) {
            return res.redirect('/home')
        }
    }
    res.render('auth-signin')
})

router.get('/dashboard', authen, async (req, res) => {
    if (!req.user || req.user.user.active === 0 || req.user.user.type_account === 0) {
        return res.redirect('/admin')
    }
    const users = await axios.get(GET_ALL_USER)
    const count = users.data.length
    const users_today = await axios.get(GET_USER_REGIS_TODAY)
    const user_today_count = users_today.data.length
    const getPercent = Math.round((users_today.data.length / count) * 100)
    const getTop = await axios.get(GET_USER_TOP_FIVE)
    const topFive = getTop.data
    const currentUser = req.user

    res.render('dashboard', {
        users,
        count,
        user_today_count,
        getPercent,
        topFive,
        currentUser
    })
})


router.get('/users', authen, async (req, res) => {
    if (!req.user || req.user.user.active === 0) {
        return res.redirect('/admin')
    }
    const page = req.query.p || 1
    const users = await axios.get(GET_ALL_USER)
    const userPerPage = await axios.get(GET_USER_PER_PAGE + page)
    console.log(userPerPage.data.users)

    const currentUser = req.user
    res.render('manage_user', {
        currentUser,
        userPage: userPerPage.data.users,
        pagination: {
            page,
            pageCount: Math.ceil(users.data.length / 5)
        }
    })
})



module.exports = router