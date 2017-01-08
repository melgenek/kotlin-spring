package spring.kotlin.web.util

import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.ResourcePropertySource
import java.nio.charset.StandardCharsets

fun ConfigurableEnvironment.addPropertySource(location: String) {
    propertySources.addFirst(ResourcePropertySource(EncodedResource(ClassPathResource(location), StandardCharsets.UTF_8)))
}
