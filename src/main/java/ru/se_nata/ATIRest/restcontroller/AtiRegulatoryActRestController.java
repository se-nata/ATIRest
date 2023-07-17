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
import ru.se_nata.ATIRest.entity.RegulatoryAct;
import ru.se_nata.ATIRest.exception.EntityNotFoundException;
import ru.se_nata.ATIRest.modelassembler.ActRegulatoryActModelAssembler;
import ru.se_nata.ATIRest.repository.AtiRegulatoryActRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiRegulatoryActRestController{
	
	private AtiServiceImpl atiServiceImpl;
	private AtiRegulatoryActRepository atiRegulatoryActRepository;
	private ActRegulatoryActModelAssembler assembler;
	
	public AtiRegulatoryActRestController(AtiServiceImpl atiServiceImpl,
			AtiRegulatoryActRepository atiRegulatoryActRepository, ActRegulatoryActModelAssembler assembler) {
		super();
		this.atiServiceImpl = atiServiceImpl;
		this.atiRegulatoryActRepository = atiRegulatoryActRepository;
		this.assembler = assembler;
	}
	@GetMapping("/regulatoryacts/{id}")
	public EntityModel<?> oneRegulatoryAct(@PathVariable Integer id){
		RegulatoryAct regulatoryAct=atiRegulatoryActRepository.findById(id).orElseThrow(()->
			new EntityNotFoundException(id));
		return assembler.toModel(regulatoryAct);
	}
	@GetMapping("/regulatoryacts/")
	public CollectionModel<?> allRegulatoryAct(){
	List <EntityModel<RegulatoryAct>> regulatoryAct=atiServiceImpl.findAllRegulatoryAct().stream().map(assembler::toModel).collect(Collectors.toList());
		return CollectionModel.of(regulatoryAct,linkTo(methodOn(AtiRegulatoryActRestController.class).allRegulatoryAct()).withSelfRel());
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/regulatoryacts/")
	public ResponseEntity<?> newRegulatoryAct(@RequestBody RegulatoryAct newRegulatoryAct){
		EntityModel<RegulatoryAct> entityModel=assembler.toModel(atiRegulatoryActRepository.save(newRegulatoryAct));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/regulatoryacts/{id}")
	public ResponseEntity<?> updateRegulatoryAct(@RequestBody RegulatoryAct newRegulatoryAct,@PathVariable Integer id){
		RegulatoryAct updateRegulatoryAct=atiRegulatoryActRepository.findById(id).//
				map(regulatoryAct->{
					regulatoryAct.setDate(newRegulatoryAct.getDate());
					regulatoryAct.setDescription(newRegulatoryAct.getDescription());
					regulatoryAct.setName(newRegulatoryAct.getName());
					regulatoryAct.setNumber(newRegulatoryAct.getNumber());
					return atiRegulatoryActRepository.save(regulatoryAct);
				}).orElseGet(()->{
					newRegulatoryAct.setId(id);
					return atiRegulatoryActRepository.save(newRegulatoryAct);
				});
				EntityModel<RegulatoryAct> entityModel=assembler.toModel(updateRegulatoryAct);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/regulatoryacts/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		atiRegulatoryActRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
