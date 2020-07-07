package com.lanmo.sbp.test;

import com.lanmo.sbp.SbpApplication;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = SbpApplication.class,properties = {"job.autorun.enabled=false"})
@Rollback(value = true)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@AutoConfigureWireMock(stubs = "classpath:/templates", port = 18777)
public class SbpTest {




}
