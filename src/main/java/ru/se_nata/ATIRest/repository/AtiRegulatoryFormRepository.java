package ru.se_nata.ATIRest.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.se_nata.ATIRest.entity.RegulatoryForm;


public interface AtiRegulatoryFormRepository extends CrudRepository<RegulatoryForm, Integer>{
	@Modifying
    @Query("DELETE FROM RegulatoryForm a WHERE a.id = :id")
    void deleteById(@Param("id") Integer id);
}
