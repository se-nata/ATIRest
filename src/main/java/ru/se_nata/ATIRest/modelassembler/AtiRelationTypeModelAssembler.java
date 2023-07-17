package ru.se_nata.ATIRest.modelassembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.se_nata.ATIRest.restcontroller.AtiRelationTypeRestController;

@Component
public class AtiRelationTypeModelAssembler implements RepresentationModelAssembler<ru.se_nata.ATIRest.entity.RelationType,EntityModel<ru.se_nata.ATIRest.entity.RelationType>>{

	@Override
	public EntityModel<ru.se_nata.ATIRest.entity.RelationType> toModel(ru.se_nata.ATIRest.entity.RelationType relationType) {
		
		return EntityModel.of(relationType,//
				linkTo(methodOn(AtiRelationTypeRestController.class).oneRelationType(relationType.getId())).withSelfRel(),//
				linkTo(methodOn(AtiRelationTypeRestController.class).allRelationType()).withRel("relationTypes"));
	}

}
