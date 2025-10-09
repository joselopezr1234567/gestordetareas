package cl.jlopezr.multiplatform.feature.login.data.model

import cl.jlopezr.multiplatform.feature.login.domain.model.LoginResult
import cl.jlopezr.multiplatform.feature.login.domain.model.User


object LoginMapper {
    
    fun LoginResponseDto.toDomain(): LoginResult {
        return LoginResult(
            success = this.success,
            user = this.user?.toDomain(),
            message = this.message
        )
    }
    
    fun UserDto.toDomain(): User {
        return User(
            id = this.id,
            email = this.email,
            name = this.name,
            profilePicture = this.profilePicture
        )
    }
    
    fun LoginResult.toDto(): LoginResponseDto {
        return LoginResponseDto(
            success = this.success,
            user = this.user?.toDto(),
            message = this.message
        )
    }
    
    fun User.toDto(): UserDto {
        return UserDto(
            id = this.id,
            email = this.email,
            name = this.name,
            profilePicture = this.profilePicture
        )
    }
}