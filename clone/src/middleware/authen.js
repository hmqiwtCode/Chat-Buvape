const jwt = require('jsonwebtoken')
const cookieParser = require('cookie-parser')
const axios = require('axios')

const URL_VALID_TOKEN = 'http://localhost:3000/user/'
const authen = async (req,resp,next) => {
    try {
        const token = req.cookies['token'].replace('Bearer ','')
        const decoded = jwt.verify(token,'Ilearnnodejs')
        console.log(decoded)
        const user = await axios.get(URL_VALID_TOKEN + decoded._id + '/' + token)
        req.user = user.data
        next()
    } catch (error) {
       // resp.status(401).send({'error' :'please authenticate'})
       next()
    }
}


module.exports = authen