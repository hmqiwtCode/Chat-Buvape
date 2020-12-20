module.exports = app => {
    const messages = require("../controllers/message");
    const router = require("express").Router();
    router.post("/message", messages.create)
    router.get("/user/:id", messages.getAll)
    router.get("/message/:id", messages.getMessageByUser)
    router.get("/messages/:id", messages.getMessageById)
    router.patch("/message/:id", messages.updateMessageObject)
    router.patch("/message/unread/:id", messages.updateUnreadMessage)
    app.use('', router);
}
