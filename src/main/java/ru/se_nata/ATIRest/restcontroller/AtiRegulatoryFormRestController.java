package ru.se_nata.ATIRest.restcontroller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
import ru.se_nata.ATIRest.entity.RegulatoryForm;
import ru.se_nata.ATIRest.exception.EntityNotFoundException;
import ru.se_nata.ATIRest.modelassembler.AtiRegulatoryFormModelAssembler;
import ru.se_nata.ATIRest.repository.AtiRegulatoryFormRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiRegulatoryFormRestController {

	private AtiRegulatoryFormRepository atiRegulatoryFormRepository;
	private AtiServiceImpl atiServiceImpl;
	private AtiRegulatoryFormModelAssembler assembler;
	
	public AtiRegulatoryFormRestController(AtiRegulatoryFormRepository atiRegulatoryFormRepository,
			AtiServiceImpl atiServiceImpl, AtiRegulatoryFormModelAssembler assembler) {
		super();
		this.atiRegulatoryFormRepository = atiRegulatoryFormRepository;
		this.atiServiceImpl = atiServiceImpl;
		this.assembler = assembler;
	}
	@GetMapping("/regulatoryforms/{id}")
	public EntityModel<RegulatoryForm> oneRegulatoryform(@PathVariable Integer id){
		RegulatoryForm regulatoryForm=atiRegulatoryFormRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id));
		return assembler.toModel(regulatoryForm);
	}
	@GetMapping("/regulatoryforms/")
	public CollectionModel<?> allRegulatoryform() {
	List <EntityModel<RegulatoryForm>> regulatoryForm=atiServiceImpl.findAllRegulatoryForm().stream().map(assembler::toModel).collect(Collectors.toList());
	return CollectionModel.of(regulatoryForm,linkTo(methodOn(AtiRegulatoryFormRestController.class).allRegulatoryform()).withSelfRel());
			
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/regulatoryforms/{id}")
	public ResponseEntity<?> newRegulatoryForm(@RequestBody RegulatoryForm newRegulatoryForm){
		EntityModel<RegulatoryForm> entityModel=assembler.toModel(atiRegulatoryFormRepository.save(newRegulatoryForm));
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/regulatoryforms/{id}")
	public ResponseEntity<?> updateRegulatoryForm(@RequestBody RegulatoryForm newRegulatoryForm,@PathVariable Integer id){
		RegulatoryForm updateRegulatoryForm=atiRegulatoryFormRepository.findById(id).//
				map(regulatoryform->{
					regulatoryform.setAffDt(newRegulatoryForm.getAffDt());
					regulatoryform.setEffDt(newRegulatoryForm.getEffDt());
					regulatoryform.setEndDt(newRegulatoryForm.getEndDt());
					regulatoryform.setFormNm(newRegulatoryForm.getFormNm());
					regulatoryform.setFormNote(newRegulatoryForm.getFormNote());
					regulatoryform.setOkudCd(newRegulatoryForm.getOkudCd());
					return atiRegulatoryFormRepository.save(regulatoryform);
				}).orElseGet(()->{
					newRegulatoryForm.setId(id);
					return atiRegulatoryFormRepository.save(newRegulatoryForm);
				});
		EntityModel<RegulatoryForm> entityModel=assembler.toModel(updateRegulatoryForm);
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/regulatoryforms/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		atiRegulatoryFormRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
