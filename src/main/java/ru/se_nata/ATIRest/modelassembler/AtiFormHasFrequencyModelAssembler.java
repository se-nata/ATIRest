package ru.se_nata.ATIRest.modelassembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import ru.se_nata.ATIRest.entity.FormHasFrequency;
import ru.se_nata.ATIRest.restcontroller.AtiFormHasFrequencyRestController;

@Component
public class AtiFormHasFrequencyModelAssembler implements RepresentationModelAssembler<FormHasFrequency, EntityModel<FormHasFrequency>> {

	@Override
	public EntityModel<FormHasFrequency> toModel(FormHasFrequency formHasFrequency) {
		
		return EntityModel.of(formHasFrequency,//
				linkTo(methodOn(AtiFormHasFrequencyRestController.class).oneFormHasFrequency(formHasFrequency.getId())).withSelfRel(),//
				linkTo(methodOn(AtiFormHasFrequencyRestController.class).allFormHasFrequency()).withRel("formhasfrequencys"));
	}

}
