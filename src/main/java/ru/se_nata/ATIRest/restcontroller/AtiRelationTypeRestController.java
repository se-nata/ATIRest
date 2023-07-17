package ru.se_nata.ATIRest.restcontroller;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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
import ru.se_nata.ATIRest.entity.RelationType;
import ru.se_nata.ATIRest.exception.EntityNotFoundException;
import ru.se_nata.ATIRest.modelassembler.AtiRelationTypeModelAssembler;
import ru.se_nata.ATIRest.repository.AtiRelationTypeRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiRelationTypeRestController {
	
	private AtiRelationTypeRepository atiRelationTypeRepository;
	private AtiServiceImpl atiServiceImpl;
	private AtiRelationTypeModelAssembler assembler;
	public AtiRelationTypeRestController(AtiRelationTypeRepository atiRelationTypeRepository,
			AtiServiceImpl atiServiceImpl, AtiRelationTypeModelAssembler assembler) {
		super();
		this.atiRelationTypeRepository = atiRelationTypeRepository;
		this.atiServiceImpl = atiServiceImpl;
		this.assembler = assembler;
	}
	@GetMapping("/relationtypes/{id}")
	public EntityModel<?> oneRelationType(@PathVariable Integer id){
		RelationType relationType =atiRelationTypeRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id));
		return assembler.toModel(relationType);
	}
	@GetMapping("/relationtypes/")
	public CollectionModel<?> allRelationType() {
		List <EntityModel<RelationType>> relationType=atiServiceImpl.findAllRelationType().stream().map(assembler::toModel).collect(Collectors.toList());
		return CollectionModel.of(relationType, linkTo(methodOn(AtiRelationTypeRestController.class).allRelationType()).withSelfRel());
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/relationtypes/")
	public ResponseEntity<?> newrelationType(@RequestBody RelationType newRelationType){
		EntityModel<RelationType> entityModel=assembler.toModel(atiRelationTypeRepository.save(newRelationType));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/relationtypes/{id}")
	public ResponseEntity<?> updateRelationType(@RequestBody RelationType newRelationType,@PathVariable Integer id){
		RelationType updateRelationType=atiRelationTypeRepository.findById(id).//
				map(relationType->{
					relationType.setDescription(newRelationType.getDescription());
					return atiRelationTypeRepository.save(relationType);
				}).orElseGet(()->{
					newRelationType.setId(id);
					return atiRelationTypeRepository.save(newRelationType);
				});
		EntityModel<RelationType> entityModel=assembler.toModel(updateRelationType);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@DeleteMapping("/relationtypes/{id}")
	public ResponseEntity<?> delete (@PathVariable Integer id){
		atiRelationTypeRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
