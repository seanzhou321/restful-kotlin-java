package base.donor.controller;

import base.donor.exception.DonorNotFoundException;
import base.donor.exception.InvalidDonorException;
import base.donor.service.DonorRepository;
import base.donor.model.Donor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DonorController {

  @Autowired
  DonorRepository donorRepository;

  @PostMapping("/donor")
  @ResponseStatus(HttpStatus.CREATED)
  public Donor create(@NotNull @RequestBody Donor donor) {
    return donorRepository.save(donor);
  }

  @GetMapping("/donor")
  public Iterable<Donor> view() {
    return donorRepository.findAll();
  }

  @Operation(summary = "Get a Donor by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the donor.",
      content = {@Content(mediaType = "application/json",
      schema = @Schema(implementation = Donor.class))}),
      @ApiResponse(responseCode = "400", description = "Invalid ID supplied.",
      content = @Content),
      @ApiResponse(responseCode = "404", description = "Donor not found.",
      content = @Content)
  })
  @GetMapping("/donor/{id}")
  public Donor findById(@Parameter(description = "ID of book to be searched") @PathVariable int id) {
    return donorRepository.findById(id).orElseThrow(() -> new DonorNotFoundException());
  }

  @PutMapping("/donor")
  @ResponseStatus(HttpStatus.CREATED)
  public Donor update(@RequestBody Donor donor) {
    if (donor==null || donor.getId()==null) {
      throw new InvalidDonorException();
    }
    return donorRepository.save(donor);
  }

  @DeleteMapping("/donor/{id}")
  public void delete(@PathVariable Integer id) {
    donorRepository.deleteById(id);
  }
}
