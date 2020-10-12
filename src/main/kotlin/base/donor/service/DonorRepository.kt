package base.donor.service

import base.donor.model.Donor
import org.springframework.data.jpa.repository.JpaRepository

interface DonorRepository : JpaRepository<Donor?, Int?>