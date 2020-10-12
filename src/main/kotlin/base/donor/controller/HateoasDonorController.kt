package base.donor.controller

import base.donor.assembler.DonorModelAssembler
import base.donor.exception.DonorNotFoundException
import base.donor.model.Donor
import base.donor.service.DonorRepository
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import javax.validation.constraints.NotNull

@RestController
class HateoasDonorController(private val donorRepository: DonorRepository,
                             private val donorModelAssembler: DonorModelAssembler) {
    @PostMapping("/hateoas/donor")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody donor: @NotNull Donor?): EntityModel<Donor> {
        val donor1 = donorRepository.save(donor!!)
        return donorModelAssembler.toModel(donor1)
    }

    @GetMapping("/hateoas/donor")
    fun view(): CollectionModel<EntityModel<Donor>> {
        val donors = donorRepository.findAll().stream()
                .map({donorModelAssembler.toModel(it!!)})
                .collect(Collectors.toList())
        return CollectionModel.of(donors,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HateoasDonorController::class.java).view()).withSelfRel())
    }

    /**
     * Use findByOrNull extension + '?:' (elvis operator) instead of Optional from java
     * https://typealias.com/guides/java-optionals-and-kotlin-nulls/
     */
    @GetMapping("/hateoas/donor/{id}")
    fun findById(@Parameter(description = "ID of book to be searched") @PathVariable id: Int): EntityModel<Donor> {
        val donor: Donor = donorRepository.findByIdOrNull(id) ?: throw DonorNotFoundException()
        return donorModelAssembler.toModel(donor)
    }

    @PutMapping("/hateoas/donor")
    @ResponseStatus(HttpStatus.CREATED)
    fun update(@RequestBody donor: Donor): EntityModel<Donor> {
        donorRepository.save(donor)
        return donorModelAssembler.toModel(donor)
    }

    @DeleteMapping("/hateoas/donor/{id}")
    fun delete(@PathVariable id: Int) {
        donorRepository.deleteById(id)
    }
}