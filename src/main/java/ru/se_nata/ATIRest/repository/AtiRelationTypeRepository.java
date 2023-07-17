package ru.se_nata.ATIRest.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.se_nata.ATIRest.entity.RelationType;



public interface AtiRelationTypeRepository extends CrudRepository<RelationType, Integer>{
	@Modifying
    @Query("DELETE FROM RelationType a WHERE a.id = :id")
    void deleteById(@Param("id") Integer id);
}
