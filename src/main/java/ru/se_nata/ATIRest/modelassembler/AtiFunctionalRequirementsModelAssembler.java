package ru.se_nata.ATIRest.modelassembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import ru.se_nata.ATIRest.entity.FunctionalRequirements;
import ru.se_nata.ATIRest.restcontroller.AtiFunctionalRequirementsRestController;

@Component
public class AtiFunctionalRequirementsModelAssembler implements RepresentationModelAssembler<FunctionalRequirements, EntityModel<FunctionalRequirements>>{

	@Override
	public EntityModel<FunctionalRequirements> toModel(FunctionalRequirements functionalRequirements) {
		
		return EntityModel.of(functionalRequirements,//
				linkTo(methodOn(AtiFunctionalRequirementsRestController.class).oneFunctionalRequirements(functionalRequirements.getId())).withSelfRel(),//
				linkTo(methodOn(AtiFunctionalRequirementsRestController.class).allFunctionalRequirements()).withRel("FunctionalRequirements"));
	}

}
