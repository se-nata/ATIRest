package ru.se_nata.ATIRest.modelassembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ru.se_nata.ATIRest.entity.RegulatoryAct;
import ru.se_nata.ATIRest.restcontroller.AtiRegulatoryActRestController;

@Component
public class ActRegulatoryActModelAssembler implements RepresentationModelAssembler<RegulatoryAct, EntityModel<RegulatoryAct>>{

	@Override
	public EntityModel<RegulatoryAct> toModel(RegulatoryAct regulatoryAct) {
		
		return EntityModel.of(regulatoryAct,//
				linkTo(methodOn(AtiRegulatoryActRestController.class).oneRegulatoryAct(regulatoryAct.getId())).withSelfRel(),//
				linkTo(methodOn(AtiRegulatoryActRestController.class).allRegulatoryAct()).withRel("regulatoryacts")); 
	}

}
