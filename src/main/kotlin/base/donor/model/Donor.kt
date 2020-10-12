package base.donor.model

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class Donor(var firstName: @Size(min = 0, max = 30) String, var lastName: @Size(min = 0, max = 30) String) : Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Donor {
        return super.clone() as Donor
    }
}