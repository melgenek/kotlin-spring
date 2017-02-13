package spring.kotlin.web

import org.springframework.stereotype.Component
import spring.kotlin.web.entity.User

@Component
class UserService {

    fun findUser(id: String): User = User("name")

}

