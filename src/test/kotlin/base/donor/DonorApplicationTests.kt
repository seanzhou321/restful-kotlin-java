package base.donor

import base.donor.controller.DonorController
import base.donor.controller.HateoasDonorController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors

@SpringBootTest
internal class DonorApplicationTests {
    @Autowired
    private val donorController: DonorController? = null

    @Autowired
    private val hateoasDonorController: HateoasDonorController? = null
    @Test
    fun contextLoads() {
        AssertionErrors.assertNotNull("donorController should hbe in context.", donorController)
        AssertionErrors.assertNotNull("hateoasDonorController should hbe in context.", hateoasDonorController)
    }
}