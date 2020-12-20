const users = []

const addUser = (id) => {
    users.push(id)
    return {id}

}


const getUser = (id) => {
    const user = users.find(uid => {
        return uid === id
    })
    return user
}

module.exports = {
    addUser,
    getUser
}