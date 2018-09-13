package ffc.app.authen

import ffc.entity.User
import ffc.entity.util.generateTempId

interface Users {

    fun user(username: String, res: (User?, Throwable?) -> Unit)
}

private class UsersMock : Users {

    private val mockUser = User(
        generateTempId(),
        "ffc.mock",
        "12341234",
        User.Role.PROVIDER,
        User.Role.SURVEYOR)

    override fun user(username: String, res: (User?, Throwable?) -> Unit) {
        res(mockUser, null)
    }
}

fun users(): Users = UsersMock()
