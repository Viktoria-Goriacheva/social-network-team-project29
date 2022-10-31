package ru.socialnet.team29.config;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AnnotationProcessorApplication.class)
@TestPropertySource("classpath:application.yml")
public class PropertiesConfigTest
{


}
