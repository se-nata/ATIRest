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
import ru.se_nata.ATIRest.entity.ActHasForm;
import ru.se_nata.ATIRest.modelassembler.ActHasFormModelAssembler;
import ru.se_nata.ATIRest.repository.AtiActHasFormRepository;
import ru.se_nata.ATIRest.service.AtiServiceImpl;

@RestController
public class AtiActHasFormRestController {
	
	private AtiActHasFormRepository atiActHasFormRepository;
	private AtiServiceImpl atiServiceImpl;
	private ActHasFormModelAssembler assembler;

	public AtiActHasFormRestController(AtiActHasFormRepository atiActHasFormRepository, AtiServiceImpl atiServiceImpl,
			ActHasFormModelAssembler assembler) {
		super();
		this.atiActHasFormRepository = atiActHasFormRepository;
		this.atiServiceImpl = atiServiceImpl;
		this.assembler = assembler;
	}

	@GetMapping("/acthasforms/{id}")
    public EntityModel<ActHasForm> oneActHasForm(@PathVariable("id") Integer id){
		ActHasForm acthasform=atiActHasFormRepository.findById(id).orElseThrow(()->new ru.se_nata.ATIRest.exception.EntityNotFoundException(id));
    	return  assembler.toModel(acthasform);
    }
	
    @GetMapping("/acthasforms/")
	public CollectionModel<EntityModel<ActHasForm>> allActHasForm(){
		List <EntityModel<ActHasForm>> acthasforms=atiServiceImpl.findAllActHasForm().stream()
				.map(assembler::toModel).collect(Collectors.toList());
		return CollectionModel.of(acthasforms, linkTo(methodOn(AtiActHasFormRestController.class).allActHasForm()).withSelfRel());
		
	}
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/acthasforms/")
    public ResponseEntity<?> newActHasForm(@RequestBody ActHasForm newActHsaForm){
   
    	EntityModel<ActHasForm> entityModel=assembler.toModel(atiActHasFormRepository.save(newActHsaForm));
    	  return ResponseEntity //
    		      .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
    		      .body(entityModel);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/acthasforms/{id}")
	public ResponseEntity <?> updateActHasForm(@RequestBody ActHasForm newacthasform,@PathVariable Integer id){
		ActHasForm updateacthasform=atiActHasFormRepository.findById(id).//
				map(acthasform->{
					acthasform.setActId(newacthasform.getActId());
					acthasform.setFormId(newacthasform.getFormId());
					acthasform.setNote(newacthasform.getNote());
					return atiActHasFormRepository.save(acthasform);
				})
				 .orElseGet(() -> {
					 newacthasform.setId(id);
				        return atiActHasFormRepository.save(newacthasform);
				      });
		EntityModel<ActHasForm> entityModel=assembler.toModel(updateacthasform);
		return ResponseEntity.//
				created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).//
				body(entityModel);
				
	}
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/acthasforms/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id){
		atiActHasFormRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

	
}