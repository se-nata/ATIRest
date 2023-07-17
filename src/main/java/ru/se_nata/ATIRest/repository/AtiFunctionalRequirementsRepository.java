package ru.se_nata.ATIRest.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.se_nata.ATIRest.entity.FunctionalRequirements;


public interface AtiFunctionalRequirementsRepository extends CrudRepository<FunctionalRequirements, Integer>{
	 @Modifying
	    @Query("DELETE FROM FunctionalRequirements a WHERE a.id = :id")
	    void deleteById(@Param("id") Integer id);
}
