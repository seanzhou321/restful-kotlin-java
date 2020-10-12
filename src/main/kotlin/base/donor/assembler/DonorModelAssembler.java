package base.donor.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import base.donor.controller.HateoasDonorController;
import base.donor.model.Donor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class DonorModelAssembler implements RepresentationModelAssembler<Donor, EntityModel<Donor>> {

  @Override
  public EntityModel<Donor> toModel(Donor donor) {
    return EntityModel.of(donor,
        linkTo(methodOn(HateoasDonorController.class).findById(donor.getId())).withSelfRel(),
        linkTo(methodOn(HateoasDonorController.class).view()).withRel("donors"));
  }
}
