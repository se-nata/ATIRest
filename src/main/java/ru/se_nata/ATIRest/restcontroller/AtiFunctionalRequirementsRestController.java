package ru.se_nata.ATIRest.restcontroller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.se_nata.ATIRest.entity.FunctionalRequirements;
import ru.se_nata.ATIRest.exception.EntityNotFoundException;
import ru.se_nata.ATIRest.modelassembler.AtiFunctionalRequirementsModelAssembler;
import ru.se_nata.ATIRest.repository.AtiFunctionalRequirementsRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiFunctionalRequirementsRestController {
	
  private AtiFunctionalRequirementsRepository atiFunctionalRequirementsRepository;
  private AtiServiceImpl atiServiceImpl;
  private AtiFunctionalRequirementsModelAssembler assembler;
  
public AtiFunctionalRequirementsRestController(AtiFunctionalRequirementsRepository atiFunctionalRequirementsRepository,
		AtiServiceImpl atiServiceImpl, AtiFunctionalRequirementsModelAssembler assembler) {
	super();
	this.atiFunctionalRequirementsRepository = atiFunctionalRequirementsRepository;
	this.atiServiceImpl = atiServiceImpl;
	this.assembler = assembler;
}
  @GetMapping("/functionalrequirements/{id}")
  public EntityModel<?> oneFunctionalRequirements(@PathVariable Integer id){
	  FunctionalRequirements functionalRequirements=atiFunctionalRequirementsRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id));
	  return assembler.toModel(functionalRequirements);
  }
  @GetMapping("/functionalrequirements/")
  public CollectionModel<?> allFunctionalRequirements(){
	  List <EntityModel<FunctionalRequirements>> functionalRequirements=atiServiceImpl.findAllFunctionalRequirements().stream().map(assembler::toModel).collect(Collectors.toList());
	  return CollectionModel.of(functionalRequirements, linkTo(methodOn(AtiFunctionalRequirementsRestController.class).allFunctionalRequirements()).withSelfRel());
	  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/functionalrequirements/")
  public ResponseEntity<?> newFunctionalRequirements(@RequestBody FunctionalRequirements newFunctionalRequirements){
	  EntityModel<FunctionalRequirements> entityModel=assembler.toModel(atiFunctionalRequirementsRepository.save(newFunctionalRequirements));
	  return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/functionalrequirements/{id}")
  public ResponseEntity<?> updateFunctionalRequirements(@RequestBody FunctionalRequirements newFunctionalRequirements,@PathVariable Integer id){
	  FunctionalRequirements updateFunctionalRequirements=atiFunctionalRequirementsRepository.findById(id).map(functionalRequirements->{
		  functionalRequirements.setDate(newFunctionalRequirements.getDate());
		  functionalRequirements.setDescription(newFunctionalRequirements.getDescription());
		  functionalRequirements.setName(newFunctionalRequirements.getName());
		  functionalRequirements.setNumber(newFunctionalRequirements.getNumber());
		 return atiFunctionalRequirementsRepository.save(functionalRequirements);
	  }).orElseGet(()->{
		  newFunctionalRequirements.setId(id);
		  return atiFunctionalRequirementsRepository.save(newFunctionalRequirements);
	  });
	  EntityModel<FunctionalRequirements> entityModel=assembler.toModel(updateFunctionalRequirements);
	  return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/functionalrequirements/{id}")
  public ResponseEntity<?> delete(@PathVariable Integer id){
	  atiFunctionalRequirementsRepository.deleteById(id);
	  return ResponseEntity.ok().build();
  }
  
}
