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
import ru.se_nata.ATIRest.entity.ActRelation;
import ru.se_nata.ATIRest.exception.EntityNotFoundException;
import ru.se_nata.ATIRest.modelassembler.ActRelationModelAssembler;
import ru.se_nata.ATIRest.repository.AtiActRelationRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiActRelationRestController {
	
    private AtiServiceImpl  atiServiceImpl;
	private AtiActRelationRepository atiActRelationRepository;
	private ActRelationModelAssembler assembler;

	public AtiActRelationRestController(ru.se_nata.ATIRest.service.AtiServiceImpl atiServiceImpl,
			AtiActRelationRepository atiActRelationRepository, ActRelationModelAssembler assembler) {
		super();
		this.atiServiceImpl = atiServiceImpl;
		this.atiActRelationRepository = atiActRelationRepository;
		this.assembler = assembler;
	}
	@GetMapping("/actrelations/{id}")
	public EntityModel<ActRelation> oneActRelation(@PathVariable Integer id) {
		ActRelation actRelation =atiActRelationRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id));
		return assembler.toModel(actRelation);
	}
	@GetMapping("/actrelations/")
	public CollectionModel<EntityModel<ActRelation>> allActRelation(){
		List <EntityModel<ActRelation>> actrelation=atiServiceImpl.findAllActRelation().stream().map(assembler::toModel).collect(Collectors.toList());
		return CollectionModel.of(actrelation,linkTo(methodOn(AtiActRelationRestController.class).allActRelation()).withSelfRel());
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/actrelations/")
	public ResponseEntity<?> newActHasForm(@RequestBody ActRelation actRelation){
		EntityModel<ActRelation> entityModel=assembler.toModel(atiActRelationRepository.save(actRelation));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/actrelations/{id}")
	public ResponseEntity<?> updateActRelation(@RequestBody ActRelation newactRelation,@PathVariable Integer id){
		ActRelation updateactrelation=atiActRelationRepository.findById(id).map(actrelation->{
			actrelation.setLeftActId(newactRelation.getLeftActId());
			actrelation.setRightActId(newactRelation.getRightActId());
			actrelation.setRelationTypeId(newactRelation.getRelationTypeId());
			return atiActRelationRepository.save(actrelation);
		})
				.orElseGet(()->{
					newactRelation.setId(id);
					return atiActRelationRepository.save(newactRelation);
				});
		EntityModel<ActRelation> entityModel=assembler.toModel(updateactrelation);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).//
				body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/actrelations/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		atiActRelationRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
