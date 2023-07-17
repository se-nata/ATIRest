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
import ru.se_nata.ATIRest.entity.FormFrequency;
import ru.se_nata.ATIRest.exception.EntityNotFoundException;
import ru.se_nata.ATIRest.modelassembler.ActFormFrequencyModelAssembler;
import ru.se_nata.ATIRest.repository.AtiFormFrequencyRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiFormFrequencyRestController {

	private AtiServiceImpl atiServiceImpli;
	private AtiFormFrequencyRepository atiFormFrequencyRepository;
	private ActFormFrequencyModelAssembler assembler;
	public AtiFormFrequencyRestController(AtiServiceImpl atiServiceImpli,
			AtiFormFrequencyRepository atiFormFrequencyRepository, ActFormFrequencyModelAssembler assembler) {
		super();
		this.atiServiceImpli = atiServiceImpli;
		this.atiFormFrequencyRepository = atiFormFrequencyRepository;
		this.assembler = assembler;
	}
	@GetMapping("/formfrequencys/{id}")
	public EntityModel<FormFrequency> oneFormFrequency(@PathVariable Integer id){
		FormFrequency formFrequency=atiFormFrequencyRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id));
		return assembler.toModel(formFrequency);
	}
	@GetMapping("/formfrequencys/")
	public CollectionModel<EntityModel<FormFrequency>> allFormFrequency(){
		List <EntityModel<FormFrequency>> formfrequency=atiServiceImpli.findAllFormFrequency().stream().map(assembler::toModel).collect(Collectors.toList());
		return CollectionModel.of(formfrequency,linkTo(methodOn(AtiFormFrequencyRestController.class).allFormFrequency()).withSelfRel());
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/formfrequencys/")
	public ResponseEntity<?> newFormFrequency(@RequestBody FormFrequency newFormFrequency){
		EntityModel<FormFrequency> entityModel=assembler.toModel(atiFormFrequencyRepository.save(newFormFrequency));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/formfrequencys/{id}")
	public ResponseEntity<?>  updateFormFrequency(@RequestBody FormFrequency newFormFrequency,@PathVariable Integer id){
		FormFrequency updateFormFrequency=atiFormFrequencyRepository.findById(id).map(formFrequency->{
			formFrequency.setFrequencyCd(newFormFrequency.getFrequencyCd());
			formFrequency.setFrequencyNm(newFormFrequency.getFrequencyNm());
			return atiFormFrequencyRepository.save(formFrequency);
		}).orElseGet(()->{
			newFormFrequency.setId(id);
			return atiFormFrequencyRepository.save(newFormFrequency);
		});
		EntityModel<FormFrequency> entityModel=assembler.toModel(updateFormFrequency);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/formfrequencys/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		atiFormFrequencyRepository.deleteById(id);
		return ResponseEntity.ok().body("Успешно");
	}
}
