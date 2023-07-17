package ru.se_nata.ATIRest.modelassembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import ru.se_nata.ATIRest.entity.FormFrequency;
import ru.se_nata.ATIRest.restcontroller.AtiFormFrequencyRestController;

@Component
public class ActFormFrequencyModelAssembler implements RepresentationModelAssembler<FormFrequency, EntityModel<FormFrequency>>{

	@Override
	public EntityModel<FormFrequency> toModel(FormFrequency formFrequency) {
		
		return EntityModel.of(formFrequency,//
				linkTo(methodOn(AtiFormFrequencyRestController.class).oneFormFrequency(formFrequency.getId())).withSelfRel(),//
				linkTo(methodOn(AtiFormFrequencyRestController.class).allFormFrequency()).withRel("formfrequency"));
	}

}
