package ru.socialnet.team29.config;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = AnnotationProcessorApplication.class)
@TestPropertySource("classpath:application.yml")
public class PropertiesConfigTest
{
}
