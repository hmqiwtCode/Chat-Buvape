
const Token = (sequelize, Sequelize) => {
    const tokenSchema = sequelize.define("token", {
        'token': {
            type: Sequelize.STRING
        }
    }, {
        modelName: 'token',
        timestamps: false
    })
 //   tokenSchema.removeAttribute('id')
    return tokenSchema

}

module.exports = Token