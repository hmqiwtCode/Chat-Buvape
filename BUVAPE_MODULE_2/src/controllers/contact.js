const db = require("../models/connect")
const axios = require("axios")
const Contact = db.Contact
const Op = db.Sequelize.Op
const Sequelize = db.Sequelize

const BASE_LINK_USER = 'http://module1-bv.us-east-2.elasticbeanstalk.com' // module 1


// add Contact
// post(/contact)
exports.create = (async (req, resp) => {
    const from = req.body.from
    const to = req.body.to
    const checkContact = await Contact.findOne({where:{[Op.or]: [{from_uid: from,to_uid:to}, {from_uid: to,to_uid:from}]}})
    if(checkContact){
        resp.status(500).send()
    }else{
        const contact = new Contact({ 'from_uid' : from, 'to_uid' : to })
        try {
            await contact.save()
            resp.status(200).send(contact)
        } catch (error) {
            resp.status(500).send({ 'error': error.message })
        }
    }
    
})



// check exist contact
// post(/contact/:from/:to)
exports.checkExist = (async (req, resp) => {
    const from = req.params.from
    const to = req.params.to
    try {
        //const contact = await Contact.findOne({where:{from_uid: from,to_uid:to} })
        const contact = await Contact.findOne({where:{[Op.or]: [{from_uid: from,to_uid:to}, {from_uid: to,to_uid:from}]}})
        if(contact){
            resp.status(200).send(contact)
        }else{
            resp.status(200).send()
        }
    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})


// get listUserContact to ID
// get(/contact/:id)
exports.getList = (async (req, resp) => {
    const id = req.params.id
    try {
        const listUser = await Contact.findAll({where:{[Op.or]: [{from_uid: id}, {to_uid:id}]}})
        if(listUser){
            resp.status(200).send(listUser)
        }else{
            resp.status(200).send()
        }
    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})

// get Friend to ID
// get(/friend/:userId)
exports.getFriends = (async (req, resp) => {
    const userId = req.params.userId
    const listUserContact = []
    try {
        const listUser = await Contact.findAll({where:{[Op.or]: [{from_uid: userId}, {to_uid:userId}]}})
        if(listUser){
            for(let i = 0; i < listUser.length;i++ ){
                const curUser = listUser[i]
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
            resp.status(200).send(listUserContact)
        }else{
            resp.status(200).send()
        }
    } catch (error) {
        resp.status(500).send({ 'error': error.message })
    }
})


// get Friend to ID | name
// get(/friends/:userId/:name)
exports.getFriendsName = (async (req, resp) => {
    const userId = req.params.userId
    const nameSearch = req.params.name
    const listUserContact = []
    try {
        const listUser = await Contact.findAll({where:{[Op.or]: [{from_uid: userId}, {to_uid:userId}]}})
        if(listUser){
            for(let i = 0; i < listUser.length;i++ ){
                const curUser = listUser[i]
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

            

            resp.status(200).send(listUserContact.filter(contact =>{
                return contact.firstname.toLowerCase().includes(nameSearch.toLowerCase()) || contact.lastname.toLowerCase().includes(nameSearch.toLowerCase())
            }))
        }else{
            resp.status(200).send()
        }
    } catch (error) {
        console.log(error)
        resp.status(500).send({ 'error': error.message })
    }
})