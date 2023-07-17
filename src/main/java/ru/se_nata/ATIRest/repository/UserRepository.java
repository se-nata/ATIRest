package ru.se_nata.ATIRest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.se_nata.ATIRest.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
	    @Query("SELECT u FROM User u WHERE u.username=:username")
		public User getUserByUsername(@Param("username")String username);
}
