const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
const validator = require('validator')

const User = (sequelize, Sequelize) => {
  const userSchema = sequelize.define("users", {
    'email': {
      'type': Sequelize.STRING,
      'unique': true,
      'allowNull': false,
      set: function (val) {
        this.setDataValue('email', val.trim().toLowerCase())
      },
      validate: {
        isEmail: true
      }
    },
    'phone': {
      'type': Sequelize.STRING,
      set: function (val) {
        this.setDataValue('phone', val.trim())
      },
      validate: {
        isPhone: function (val) {
          if (!validator.isMobilePhone(val, 'vi-VN')) {
            throw new Error('Số điện thoại không hợp lệ (10 số, không chứa kí tự)')
          }
        }
      }

    },
    'firstname': {
      'type': Sequelize.STRING,
      set: function (val) {
        this.setDataValue('firstname', val.trim())
      }
    },
    'lastname': {
      'type': Sequelize.STRING,
      set: function (val) {
        this.setDataValue('lastname', val.trim())
      }

    },
    'dob' : {
      'type': Sequelize.STRING,
       set: function (val) {
        this.setDataValue('dob', val.trim())
      }
    },
    'password': {
      'type': Sequelize.STRING,
      set: function (val) {
        this.setDataValue('password', val.trim())
      },
      validate : {
        len: [7,100]
      }
    },
    'otp' : {
      'type': Sequelize.STRING,
    },
    'wrong' : {
      'type' : Sequelize.STRING,
      'defaultValue' : '3'
    },
    'type_account' : {
      'type' : Sequelize.INTEGER,
      'defaultValue' : 0  // 0 is regular account | 1 is admin account
    },
    'active' : {
      'type' : Sequelize.BOOLEAN,
      'defaultValue' : true // true is active | false is deactivate
    }
  });


  userSchema.addHook('beforeSave', async (user, options) => {
    if(user.changed('password')){
      user.password = await bcrypt.hash(user.password,10)
    }
    console.log(user.password)
  })

  // Instance method
  userSchema.prototype.generateJsonWebToken = async function(Token){
    const user = this
    const token = jwt.sign({_id : user.id.toString()},'Ilearnnodejs')
    const token_user = new Token({token,'userId' : user.id})
    await token_user.save()
    return token
}

// Class method
userSchema.findByCredentials = async function (email,password,User,Token) {
  const user = await User.findOne({where : {'email' : email},include: [Token]})
    if(!user){
        throw new Error('Unable to login')
    }

    console.log(user)
    const isMatch = await bcrypt.compare(password, user.password)

    if(!isMatch){
        throw new Error('Unable to login')
    }
    return user
}



userSchema.findByCredentialsWithPhone = async function (phone,password,User,Token) {
  const user = await User.findOne({where : {phone},include: [Token]})
    if(!user){
        throw new Error('Unable to login')
    }

    console.log(user)
    const isMatch = await bcrypt.compare(password, user.password)

    if(!isMatch){
        throw new Error('Unable to login')
    }
    return user
}



  return userSchema
}

module.exports = User