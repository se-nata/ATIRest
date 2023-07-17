package ru.se_nata.ATIRest.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import ru.se_nata.ATIRest.entity.FormHasFrequency;
import ru.se_nata.ATIRest.exception.EntityNotFoundException;
import ru.se_nata.ATIRest.modelassembler.AtiFormHasFrequencyModelAssembler;
import ru.se_nata.ATIRest.repository.AtiFormHasFrequencyRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiFormHasFrequencyRestController {
	
    private AtiServiceImpl  atiServiceImpl;
	private AtiFormHasFrequencyRepository atiFormHasFrequencyRepository ;
	private AtiFormHasFrequencyModelAssembler assembler;
	
	public AtiFormHasFrequencyRestController(AtiServiceImpl atiServiceImpl,
			AtiFormHasFrequencyRepository atiFormHasFrequencyRepository, AtiFormHasFrequencyModelAssembler assembler) {
		super();
		this.atiServiceImpl = atiServiceImpl;
		this.atiFormHasFrequencyRepository = atiFormHasFrequencyRepository;
		this.assembler = assembler;
	}
    @GetMapping("/formhasfrequencys/{id}")
	public EntityModel<?> oneFormHasFrequency(@PathVariable Integer id){
    	FormHasFrequency formHasFrequency = atiFormHasFrequencyRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id));
		return assembler.toModel(formHasFrequency);
	}
    @GetMapping("/formhasfrequencys/")
    public CollectionModel<?> allFormHasFrequency(){
    	List <EntityModel<FormHasFrequency>> formHasFrequency=atiServiceImpl.findAllFormHasFrequency().stream().map(assembler::toModel).collect(Collectors.toList());
    	return CollectionModel.of(formHasFrequency,linkTo(methodOn(AtiFormHasFrequencyRestController.class).allFormHasFrequency()).withSelfRel());
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/formhasfrequencys/")
    public ResponseEntity<?> newFormHasFrequency(@RequestBody FormHasFrequency newFormHasFrequency){
    	EntityModel<FormHasFrequency> formHasFrequency=assembler.toModel(atiFormHasFrequencyRepository.save(newFormHasFrequency));
    	return ResponseEntity.created(formHasFrequency.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(formHasFrequency);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/formhasfrequencys/{id}")
    public ResponseEntity<?> updateFormHasFrequency(@RequestBody FormHasFrequency newFormHasFrequency,@PathVariable Integer id){
    	FormHasFrequency updateformHasFrequency=atiFormHasFrequencyRepository.findById(id).map(formHasFrequency ->{
    		formHasFrequency.setFormId(newFormHasFrequency.getFormId());
    		formHasFrequency.setFrequencyId(newFormHasFrequency.getFrequencyId());
    		return atiFormHasFrequencyRepository.save(formHasFrequency );
    	}).orElseGet(()->{
    		newFormHasFrequency.setId(id);
    		return atiFormHasFrequencyRepository.save(newFormHasFrequency);
    	});
    	EntityModel<FormHasFrequency> entityModel=assembler.toModel(updateformHasFrequency);
    	return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/formhasfrequencys/{id}")
    public ResponseEntity<?> delete (@PathVariable Integer id){
    	atiFormHasFrequencyRepository.deleteById(id);
    	return ResponseEntity.ok().build();
    }
}
