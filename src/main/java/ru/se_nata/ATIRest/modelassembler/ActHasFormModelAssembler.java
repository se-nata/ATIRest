package ru.se_nata.ATIRest.modelassembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ru.se_nata.ATIRest.entity.ActHasForm;
import ru.se_nata.ATIRest.restcontroller.AtiActHasFormRestController;

@Component
public class ActHasFormModelAssembler implements RepresentationModelAssembler<ActHasForm, EntityModel<ActHasForm>>{

	@Override
	public EntityModel<ActHasForm> toModel(ActHasForm acthasform) {
		
		return EntityModel.of(acthasform,//
				linkTo(methodOn(AtiActHasFormRestController.class).oneActHasForm(acthasform.getId())).withSelfRel(),//
				linkTo(methodOn(AtiActHasFormRestController.class).allActHasForm()).withRel("acthasforms"));
	}

}
