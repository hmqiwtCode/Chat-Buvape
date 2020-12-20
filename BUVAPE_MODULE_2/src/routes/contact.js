module.exports = app => {
    const contacts = require("../controllers/contact");

    var router = require("express").Router();

    router.post("/contact", contacts.create)
    router.get("/contact/:from/:to", contacts.checkExist)
    router.get("/contact/:id", contacts.getList)
    router.get("/friend/:userId", contacts.getFriends)
    router.get("/friends/:userId/:name", contacts.getFriendsName)

    app.use('', router);
}
