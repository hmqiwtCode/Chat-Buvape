module.exports = app => {
    const users = require("../controllers/user");

    var router = require("express").Router();
    router.post("/mail", users.create)
    router.post("/phone", users.phone)
    router.get("/mail/:gm/:code", users.validcode)
    router.get("/phone/:num", users.phoneValid)
    router.post("/users", users.addmoreinfo)
    router.get("/user_phone/:phone", users.findUserByPhoneNumber)
    router.post("/users_phone", users.addmoreinfo_phone)
    router.post("/users/login", users.login)
    router.post("/users/login_phone", users.login_phone)
    router.get("/users/:mail", users.checkvalid)
    router.get("/users/otp/:mail", users.haveotp)
    router.get("/user/:mail", users.useremail)

    router.get("/user_row/:id", users.userrow)


    router.get("/user/:id/:token", users.validtokenlogin)
    router.get("/app/user/:token", users.validtokenlogin_app)
    router.get("/users", users.getCountAllUser)
    router.get("/users/find/today", users.getUserRegisToday)
    router.get("/users/top/5", users.getTopFive)
    router.get("/users/deactivate/:id", users.deactivate)
    router.get("/users/activate/:id", users.activate)
    router.get("/users/page/:page", users.userperpage)
    router.get("/user_custom/:phone", users.findListUserByPhoneNumber)


    router.post("/add/users", users.addNewUser)
    router.post("/add/app/users", users.addNewUserApp)
    // router.delete("/users/page/:page", users.userperpage)
    router.delete("/users/:id", users.deleteUser)
    router.get("/us/:id", users.findUserID)


    app.use('', router);
}
