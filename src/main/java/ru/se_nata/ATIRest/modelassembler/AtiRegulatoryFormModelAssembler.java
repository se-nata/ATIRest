package ru.se_nata.ATIRest.modelassembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.se_nata.ATIRest.entity.RegulatoryForm;
import ru.se_nata.ATIRest.restcontroller.AtiRegulatoryFormRestController;

@Component
public class AtiRegulatoryFormModelAssembler implements RepresentationModelAssembler<RegulatoryForm, EntityModel<RegulatoryForm>>{

	@Override
	public EntityModel<RegulatoryForm> toModel(RegulatoryForm regulatoryform) {
		
		return EntityModel.of(regulatoryform,//
				linkTo(methodOn(AtiRegulatoryFormRestController.class).oneRegulatoryform(regulatoryform.getId())).withSelfRel(),//
				linkTo(methodOn(AtiRegulatoryFormRestController.class).allRegulatoryform()).withRel("regulatoryforms"));
	}

}
