package ru.se_nata.ATIRest.modelassembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.se_nata.ATIRest.entity.ActRelation;
import ru.se_nata.ATIRest.restcontroller.AtiActRelationRestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ActRelationModelAssembler implements RepresentationModelAssembler<ActRelation, EntityModel<ActRelation>>{

	@Override
	public EntityModel<ActRelation> toModel(ActRelation actrelation) {
		
		return EntityModel.of(actrelation,//
				linkTo(methodOn(AtiActRelationRestController.class).oneActRelation(actrelation.getId())).withSelfRel(),//
				linkTo(methodOn(AtiActRelationRestController.class).allActRelation()).withRel("actrelations"));
	}

}
